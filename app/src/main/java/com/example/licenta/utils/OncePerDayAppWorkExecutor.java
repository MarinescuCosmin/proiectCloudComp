package com.example.licenta.utils;

import android.util.Log;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class OncePerDayAppWorkExecutor {

    private static final String TAG = "Executor";

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private final String name;
   // private final UpdateBurnOrEatCalories appWork;

    private final int targetHour;
    private final int targetMin;
    private final int targetSec;

    private volatile boolean isBusy = false;
    private volatile ScheduledFuture<?> scheduledTask = null;

    private AtomicInteger completedTasks = new AtomicInteger(0);

    public OncePerDayAppWorkExecutor(
            String name,
           // UpdateBurnOrEatCalories appWork,
            int targetHour,
            int targetMin,
            int targetSec
    ) {
        this.name = "Executor [" + name + "]";
    //    this.appWork = appWork;

        this.targetHour = targetHour;
        this.targetMin = targetMin;
        this.targetSec = targetSec;
    }

    public void start() {
        scheduleNextTask(doTaskWork());
    }

    private Runnable doTaskWork() {
        return () -> {
            Log.d(TAG,name + " [" + completedTasks.get() + "] start: " +  Calendar.getInstance().getTime());
            try {
                isBusy = true;
                Utils.doWork();
                Log.d(TAG,name + " finish work in " + Calendar.getInstance().getTime());
            } catch (Exception ex) {
                Log.e(TAG,name + " throw exception in " +  Calendar.getInstance().getTime(), ex);
            } finally {
                isBusy = false;
            }
            scheduleNextTask(doTaskWork());
            Log.d(TAG,name + " [" + completedTasks.get() + "] finish: " + Calendar.getInstance().getTime());
            Log.d(TAG,name + " completed tasks: " + completedTasks.incrementAndGet());
        };
    }

    private void scheduleNextTask(Runnable task) {
        Log.d(TAG,name + " make schedule in " + Calendar.getInstance().getTime());
        long delay = computeNextDelay(targetHour, targetMin, targetSec);
        Log.d(TAG,name + " has delay in " + delay);
        scheduledTask = executorService.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    private static long computeNextDelay(int targetHour, int targetMin, int targetSec) {
        Calendar startTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, targetHour);
        startTime.set(Calendar.MINUTE, targetMin);
        startTime.set(Calendar.SECOND, targetSec);
        startTime.set(Calendar.MILLISECOND, 0);

        if(startTime.before(now) || startTime.equals(now))
        {
            startTime.add(Calendar.MINUTE, 3);
        }
        long duration = startTime.getTimeInMillis()-now.getTimeInMillis();
        return duration;
    }

//    public static ZonedDateTime minskDateTime() {
//        return ZonedDateTime.now(ZoneId.of("Europe/Minsk"));
//    }

    public void stop() {
        Log.d(TAG,name + " is stopping.");
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
        executorService.shutdown();
        Log.d(TAG,name + " stopped.");
        try {
            Log.d(TAG,name + " awaitTermination, start: isBusy [ " + isBusy + "]");
            // wait one minute to termination if busy
            if (isBusy) {
                executorService.awaitTermination(1, TimeUnit.MINUTES);
            }
        } catch (InterruptedException ex) {
            Log.e(TAG,name + " awaitTermination exception", ex);
        } finally {
            Log.d(TAG,name + " awaitTermination, finish");
        }
    }

}