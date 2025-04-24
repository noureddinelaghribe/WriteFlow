package com.noureddine.WriteFlow.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.fragments.LoginFragment;
import com.noureddine.WriteFlow.fragments.RegisterFragment;

public class AuthActivity extends AppCompatActivity {


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        if (savedInstanceState == null) {
            // عرض شاشة الدخول عند بدء التطبيق
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.auth_container, new RegisterFragment())
                    .commit();
        }


    }

    // دالة لتبديل الـ Fragment الحالي بآخر جديد
    public void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_container, fragment)
                .addToBackStack(null) // إضافة العملية إلى BackStack للسماح بالرجوع
                .commit();
    }

    public void toHome(){
        Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
        startActivity(intent);
    }


}