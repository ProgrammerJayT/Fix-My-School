package com.fixmyschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        mAuth = FirebaseAuth.getInstance();

        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);


                    if (mAuth.getCurrentUser() != null)
                    {
                        Intent gotoHome = new Intent(MainActivity.this, HomeScreen.class);
                        startActivity(gotoHome);
                        finish();
                    }
                    else {
                        Intent gotoChat = new Intent(MainActivity.this, Login.class);
                        startActivity(gotoChat);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        myThread.start();
    }
}