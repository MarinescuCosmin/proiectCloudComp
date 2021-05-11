package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText mPassword,mEmail;
    Button mCreate;
    ProgressBar progressBar;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mPassword= findViewById(R.id.editTextPasswordRegister);
        mEmail= findViewById(R.id.editTextEmailRegister);
        mCreate= findViewById(R.id.buttonCreate);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        fauth=FirebaseAuth.getInstance();

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if(isValid(email,password)){
                    progressBar.setVisibility(View.VISIBLE);

                    //register user in firebase

                    fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"User Created",Toast.LENGTH_SHORT).show();

                                finish();
                            }
                            else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this,"Error !" +task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
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
