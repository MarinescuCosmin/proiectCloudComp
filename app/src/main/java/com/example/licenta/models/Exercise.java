package com.example.licenta.models;

import java.io.Serializable;

public class Exercise implements Comparable<Exercise>, Serializable {

    String Activitate;
    double MET;
    double CaloriiArse;
    double Timp;


    public Exercise() {

    }


    public Exercise(String Activitate, double MET) {
        this.Activitate = Activitate;
        this.MET = MET;
    }

    public String getActivitate() {
        return Activitate;
    }

    public void setActivitate(String activitate) {
        this.Activitate = activitate;
    }

    public double getMET() {
        return MET;
    }

    public void setMET(double MET) {
        this.MET = MET;
    }

    public double getCaloriiArse() {
        return CaloriiArse;
    }

    public void setCaloriiArse(double caloriiArse) {
        CaloriiArse = caloriiArse;
    }

    public double getTimp() {
        return Timp;
    }

    public void setTimp(double timp) {
        Timp = timp;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "Activitate='" + Activitate + '\'' +
                ", MET=" + MET +
                '}';
    }

    public Exercise deepCopy() {
        Exercise e = new Exercise();
        e.setMET(this.MET);
        e.setActivitate(this.Activitate);
        e.setCaloriiArse(this.CaloriiArse);
        return e;
    }
    @Override
    public int compareTo(Exercise exercise) {
        String first = this.getActivitate();
        String second = exercise.getActivitate();
        return first.compareToIgnoreCase(second);
    }
}
