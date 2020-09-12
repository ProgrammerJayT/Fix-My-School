package com.fixmyschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {

    TextView newAccount;
    ProgressBar load;
    Button login;
    TextView enterEmailLogin, enterPassword;

    //Firebase declarations
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase implementations
        mAuth = FirebaseAuth.getInstance();

        enterEmailLogin = findViewById(R.id.input_loginEmail);
        enterPassword = findViewById(R.id.input_loginPassword);


        //xmls
        load = findViewById(R.id.progressBar_login);
        load.setVisibility(View.GONE);

        login = findViewById(R.id.button_signin);

        newAccount = findViewById(R.id.textView_newAccount);

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent createNewAccount = new Intent(Login.this, Registration.class);
                startActivity(createNewAccount);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = enterEmailLogin.getText().toString().trim();
                final String password = enterPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    enterEmailLogin.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(email))
                {
                    enterEmailLogin.setError("Password is required");
                    return;
                }

                if (password.length() < 8)
                {
                    enterPassword.setError("Password too short. Must be a minimum of eight characters");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            load.setVisibility(View.VISIBLE);

                            Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT).show();

                            Intent home = new Intent(Login.this, HomeScreen.class);
                            startActivity(home);
                            finish();

                        }
                        else
                        {
                            Toast.makeText(Login.this, "Error ! "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}