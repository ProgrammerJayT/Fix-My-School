package com.fixmyschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Registration extends AppCompatActivity {

    //Firebase Declarations
    private FirebaseAuth mAuth;
    EditText enterEmail, enterPassword, enterPhone, enterSurname, enterNames;
    Button signUp;
    ProgressBar register;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Firebase implementations
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        //Casting views
        //register = findViewById(R.id.progressBar_registration);
        signUp = findViewById(R.id.button_submitNewUser);
        enterEmail = findViewById(R.id.input_email);
        enterPassword = findViewById(R.id.input_password);
        enterNames = findViewById(R.id.input_forenames);
        enterPhone = findViewById(R.id.input_phoneNumber);
        enterSurname = findViewById(R.id.input_surname);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = enterEmail.getText().toString().trim();
                final String password = enterPassword.getText().toString().trim();
                final String surname = enterSurname.getText().toString().trim();
                final String forenames = enterNames.getText().toString().trim();
                final String phoneNo = enterPhone.getText().toString().trim();

                //Set error messages for empty fields
                if (TextUtils.isEmpty(forenames))
                {
                    enterNames.setError("Name(s) required");
                    return;
                }

                if (TextUtils.isEmpty(surname))
                {
                    enterSurname.setError("Surname required");
                    return;
                }

                if (TextUtils.isEmpty(phoneNo))
                {
                    enterPhone.setError("Phone number is required");
                    return;
                }

                if (TextUtils.isEmpty(email))
                {
                    enterEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password))
                {
                    enterPassword.setError("Password is required");
                    return;
                }

                if (password.length() < 8)
                {
                    enterPassword.setError("Password too short. Must be a minimum of eight characters");
                    return;
                }

                //register.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {

                            UserRegForm principalInfo = new UserRegForm(
                                    forenames, surname, phoneNo, email, password
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Personal")
                                    .setValue(principalInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(Registration.this, "User registered successfully", Toast.LENGTH_LONG).show();

                                    //register.setVisibility(View.GONE);
                                    Thread myThread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {
                                                Thread.sleep(3000);

                                                Intent gotoHome = new Intent(Registration.this, HomeScreen.class);
                                                startActivity(gotoHome);
                                                finish();

                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    });

                                    myThread.start();
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(Registration.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

}