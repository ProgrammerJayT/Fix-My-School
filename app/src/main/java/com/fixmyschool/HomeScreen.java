package com.fixmyschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeScreen extends AppCompatActivity {

    DatabaseReference databaseReference;
    TextView logout, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Casting views
        logout = findViewById(R.id.textView_logoutTemp);
        userName = findViewById(R.id.textView_userNameDisplay);

        //Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fetchName, fetchSurname;

                fetchName = dataSnapshot.child("Personal").child("UserSurname").getValue(String.class);
                fetchSurname = dataSnapshot.child("Personal").child("userName").getValue(String.class);
                userName.setText(fetchSurname + " " + fetchName);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(HomeScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(HomeScreen.this, Login.class);
                startActivity(login);
                finish();
            }
        });


    }

}