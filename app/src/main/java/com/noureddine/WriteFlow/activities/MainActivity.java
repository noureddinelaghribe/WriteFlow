package com.noureddine.WriteFlow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.repositorys.FirebaseRepository;

public class MainActivity extends AppCompatActivity {

    private EncryptedPrefsManager prefs;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = EncryptedPrefsManager.getInstance(this);

        // Use a Handler to delay the transition to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefs.getUser() == null){
                    intent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);



    }



}