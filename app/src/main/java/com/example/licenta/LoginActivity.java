package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.licenta.models.Exercise;
import com.example.licenta.models.Food;
import com.example.licenta.models.UserDetails;
import com.example.licenta.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
   public static ArrayList<Food> lista_search_mancare= new ArrayList<>();
   public static ArrayList<Exercise> lista_search_exercitii= new ArrayList<>();
    EditText mPassword,mEmail;
    ProgressBar progressBar;
    FirebaseUser user;
    FirebaseAuth fauth;
    ConstraintLayout loginLayout;
    AnimationDrawable animationDrawablelayout;
    AnimationDrawable animationDrawablebutton;
    Button login , register;

    @Override
    protected void onStart() {
        super.onStart();
        user =fauth.getCurrentUser();
        if(user!=null){
            finish();
            Utils.getCurrentUserDetails(LoginActivity.this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mPassword=findViewById(R.id.editTextPassword);
        mEmail=findViewById(R.id.editTextEmail);
        progressBar=findViewById(R.id.progressBarLogin);
        fauth=FirebaseAuth.getInstance();
        login = (Button)findViewById(R.id.buttonLogin) ;
        register= (Button) findViewById(R.id.buttonRegister);
        mPassword.setText("cosmin");
        mEmail.setText("cosmin@yahoo.com");
        progressBar.setVisibility(View.INVISIBLE);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email= mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if(isValid(email,password)){
                    progressBar.setVisibility(View.VISIBLE);
                    fauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(LoginActivity.this,"Succesfully Logged In",Toast.LENGTH_SHORT).show();
                                Utils.getCurrentUserFoods();
                                Utils.getCurrentUserExercises();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.getCurrentUserDetails(LoginActivity.this);

                                    }
                                }, 2000);
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                            else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this,"Error !" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }





            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));


            }
        });
    }




    private boolean isValid(String email, String password){

        if(email.isEmpty()){
            mEmail.setError(getString(R.string.emailEmpty));
            return false;
        }
        if(password.isEmpty()){
            mPassword.setError(getString(R.string.passwordEmpty));
            return false;
        }

        return true;
    }
}
