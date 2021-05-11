package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.adapters.SearchAdapterFood;
import com.example.licenta.models.Exercise;
import com.example.licenta.models.Food;
import com.example.licenta.utils.Calculations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddFoodActivity extends AppCompatActivity implements SearchAdapterFood.OnFoodClickListener {
    SearchView searchViewFood;
    RecyclerView recyclerViewFood;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<Food> lista_search_mancare;
    SearchAdapterFood searchAdapterFood;
    ProgressBar progressBar;
    ArrayList<Food> myList = new ArrayList<>();
    FirebaseFirestore dbfirestore;
    String userId;
    Button addImageScan;
    String mancare_cautata;


    private static final int CAMERA_REQUEST_CODE = 200;
    public static final int STORAGE_REQUEST_CODE = 400;
    public static final int IMAGE_PICK_GALLERY_CODE = 1000;
    public static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;
    public static boolean SEARCH_PRESSED = false;
    private CameraSource mCameraSource;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SEARCH_PRESSED = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ActionBar actionBar = getSupportActionBar();


        lista_search_mancare = new ArrayList<>();
        progressBar = findViewById(R.id.progressBarAddFood);
        searchViewFood = findViewById(R.id.searchViewFood);
        recyclerViewFood = findViewById(R.id.recyclerViewFood);
        addImageScan = findViewById(R.id.add_image_scan);

        //camera permission
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //storage permission
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbfirestore = FirebaseFirestore.getInstance();

        searchAdapterFood = new SearchAdapterFood(AddFoodActivity.this, LoginActivity.lista_search_mancare, this);
//        if (MainActivity.InfoDatabaseGot == false) {
//            FirebaseFirestore dbfirestore = FirebaseFirestore.getInstance();
//            dbfirestore.collection("mancaruri").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                            Food food_temp = documentSnapshot.toObject(Food.class);
//
//                            LoginActivity.lista_search_mancare.add(food_temp);
//                        }
//                        LoginActivity.lista_search_mancare.sort(Comparator.comparing(Food::getAliment));
//
//                        progressBar.setVisibility(View.INVISIBLE);
//                        MainActivity.InfoDatabaseGot = true;
//
//                        recyclerViewFood.setAdapter(searchAdapterFood);
//
//                    }
//                }
//            });
//        } else {
//            searchAdapterFood.notifyDataSetChanged();
//            recyclerViewFood.setAdapter(searchAdapterFood);
//            progressBar.setVisibility(View.INVISIBLE);
//
//        }


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerViewFood.setHasFixedSize(true);
        recyclerViewFood.setLayoutManager(new LinearLayoutManager((this)));
        recyclerViewFood.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        searchViewFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SEARCH_PRESSED = true;
                mancare_cautata = query;
                search(query);

                Toast.makeText(AddFoodActivity.this, "." + lista_search_mancare.size() + "." + LoginActivity.lista_search_mancare.size(), Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SEARCH_PRESSED = true;
                mancare_cautata = newText;
                search(newText);

                return true;
            }
        });

        if (MainActivity.InfoDatabaseGot == false) {
            databaseReference.child("Mancaruri").child("0").child("mancaruri").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String aliment = snapshot.child("Aliment").getValue().toString();
//                    Double calorii = Double.valueOf(snapshot.child("Calorii").getValue().toString());
//                    Double carbo = Double.valueOf(snapshot.child("Carbohidrati").getValue().toString());
//                    Double lipide = Double.valueOf(snapshot.child("Lipide").getValue().toString());
//                    Double proteine = Double.valueOf(snapshot.child("Proteine").getValue().toString());

//                    if(aliment.contains(string)){
//                        Food temp_Food = new Food(aliment,calorii,carbo,lipide,proteine);
//                        lista_search_mancare.add(temp_Food);
//
//                    }
                        LoginActivity.lista_search_mancare.add(snapshot.getValue(Food.class));


                        searchAdapterFood.notifyDataSetChanged();
                        recyclerViewFood.setAdapter(searchAdapterFood);

                    }
                    Collections.sort(LoginActivity.lista_search_mancare);
                    progressBar.setVisibility(View.INVISIBLE);
                    MainActivity.InfoDatabaseGot = true;


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            searchAdapterFood.notifyDataSetChanged();
            recyclerViewFood.setAdapter(searchAdapterFood);
            progressBar.setVisibility(View.INVISIBLE);
        }


        addImageScan.setOnClickListener(v -> {
            showImageImportDialog();
        });


    }

    private void showImageImportDialog() {
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Select Iamge");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //camera option clicked
                    if (!checkCameraPermission()) {
                        //camera permission not allowed
                        requestCameraPermission();
                    } else {
                        pickCamera();
                    }
                }
                if (which == 1) {
                    //gallery option clicked
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickGallery();
                    }

                }
            }
        });
        dialog.create().show();
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickCamera();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();


                ImageView imageView = new ImageView(this);
                imageView.setImageURI(resultUri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        stringBuilder.append(myItem.getValue());
                        stringBuilder.append(" ");
                    }
                    searchViewFood.setQuery(stringBuilder.toString(), false);


                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void search(String str) {
        myList.clear();

        for (Food f : LoginActivity.lista_search_mancare) {
            if (f.getAliment().toLowerCase().startsWith(str.toLowerCase())) {
                myList.add(f);
            }
        }

        for (Food f : LoginActivity.lista_search_mancare) {
            if (f.getAliment().toLowerCase().startsWith(str.toLowerCase())) {

            } else if (f.getAliment().toLowerCase().contains(str.toLowerCase())) {
                myList.add(f);
            }
        }
        SearchAdapterFood searchAdapterFood = new SearchAdapterFood(AddFoodActivity.this, myList, this);
        recyclerViewFood.setAdapter(searchAdapterFood);


    }


    @Override
    public void onFoodItemClick(int position) {
        searchViewFood.clearFocus();

        final Double[] proteine = new Double[1];
        final Double[] lipide = new Double[1];
        final Double[] carbohidrati = new Double[1];
        final Double[] calorii = new Double[1];
        final Double[] cantitate = new Double[1];
        Food food;
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(10);
        if (SEARCH_PRESSED) {
            food = (myList.get(position)).deepCopy();

        } else {
            food = (LoginActivity.lista_search_mancare.get(position)).deepCopy();
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddFoodActivity.this,R.style.MyDialogTheme);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_food, null);
        EditText editText_cantitate_aliment = (EditText) mView.findViewById(R.id.editTextCantitate_aliment_dialog);
        TextView nume_aliment_dialog = (TextView) mView.findViewById(R.id.nume_aliment_dialog);
        TextView proteine_aliment_dialog = (TextView) mView.findViewById(R.id.proteine_aliment_dialog);
        TextView lipide_aliment_dialog = (TextView) mView.findViewById(R.id.lipide_aliment_dialog);
        TextView carbohidrati_aliment_dialog = (TextView) mView.findViewById(R.id.carbohidrati_aliment_dialog);
        TextView calorii_aliment_dialog = (TextView) mView.findViewById(R.id.calorii_aliment_dialog);
        Button buttonPreviewValoriDialog = (Button) mView.findViewById(R.id.buttonPreviewValoriDialogFood);

        nume_aliment_dialog.setText(food.getAliment());
        buttonPreviewValoriDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText_cantitate_aliment.getText().toString().isEmpty()) {

                    proteine[0] = food.getProteine() * Double.valueOf(editText_cantitate_aliment.getText().toString()) / 100;
                    lipide[0] = food.getLipide() * Double.valueOf(editText_cantitate_aliment.getText().toString()) / 100;
                    carbohidrati[0] = food.getCarbohidrati() * Double.valueOf(editText_cantitate_aliment.getText().toString()) / 100;
                    calorii[0] = food.getCalorii() * Double.valueOf(editText_cantitate_aliment.getText().toString()) / 100;
                    cantitate[0] = Double.valueOf(editText_cantitate_aliment.getText().toString());
                    proteine_aliment_dialog.setText(String.format("%.2f", proteine[0]));
                    lipide_aliment_dialog.setText(String.format("%.2f", lipide[0]));
                    carbohidrati_aliment_dialog.setText(String.format("%.2f", carbohidrati[0]));
                    calorii_aliment_dialog.setText(String.format("%.2f", calorii[0]));


                } else {
                    showToast("Introduceti cantitatea!");
                }
            }
        });


        mBuilder.setView(mView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!editText_cantitate_aliment.getText().toString().isEmpty()) {
                    food.setCalorii(calorii[0]);
                    food.setCarbohirati(carbohidrati[0]);
                    food.setProteine(proteine[0]);
                    food.setLipide(lipide[0]);
                    food.setCantitate(cantitate[0]);
                    MainActivity.AllFoods.add(food);
                    Calculations.ScadeCalorii(food.getCalorii());
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                    String formattedDate = df.format(date);
                    String dataHardCodata = "18-Jun-2020";
                    HashMap<String, ArrayList<Food>> map = new HashMap<>();
                    map.put("mancaruri", MainActivity.AllFoods);
                    dbfirestore.collection("users").document(userId).collection("MancaruriSiExercitii").document(formattedDate).set(map, SetOptions.merge());


                } else {
                    showToast("Nu ati introdus cantitatea pentru alimentul respectiv!");
                }
            }
        }).setNegativeButton("CANCEL", (dialog, which) -> {
            //search(mancare_cautata);

        });

        AlertDialog dialog = mBuilder.create();


        dialog.show();

        MainActivity.foodsAdapter.notifyDataSetChanged();

    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


}
