package com.noureddine.WriteFlow.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.DialogLoading;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.adapter.AdapterPager;
import com.noureddine.WriteFlow.fragments.HistoryFragment;
import com.noureddine.WriteFlow.fragments.SettingFragment;
import com.noureddine.WriteFlow.fragments.SubsecribeFragment;
import com.noureddine.WriteFlow.fragments.ToolsFragment;
import com.noureddine.WriteFlow.model.MyTab;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.repositorys.FirebaseRepository;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    AdapterPager adapter;
    FirebaseAuth auth;
    EncryptedPrefsManager prefs;
    DatabaseReference databaseReference;
    DialogLoading dialogLoading;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viwepager);

        viewPager2.setUserInputEnabled(false);
        auth = FirebaseAuth.getInstance();
        prefs = EncryptedPrefsManager.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        dialogLoading = new DialogLoading(this);
        dialogLoading.loadingProgressDialog("Loading...");
        dialogLoading.showLoadingProgressDialog();

        initUser();



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (pos == 0) {
                    tab.setIcon(R.drawable.home_selected);
                } else if (pos == 1) {
                    tab.setIcon(R.drawable.history_selected);
                } else if (pos == 2) {
                    tab.setIcon(R.drawable.crown_selected);
                } else if (pos == 3) {
                    tab.setIcon(R.drawable.settings_selected);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (pos == 0) {
                    tab.setIcon(R.drawable.home_unselected);
                } else if (pos == 1) {
                    tab.setIcon(R.drawable.history_unselected);
                } else if (pos == 2) {
                    tab.setIcon(R.drawable.crown_unselected);
                } else if (pos == 3) {
                    tab.setIcon(R.drawable.settings_unselected);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });




    }

    public ViewPager2 getViewPager2() {
        return viewPager2;
    }


    private void loadFragment(){

        adapter = new AdapterPager(getSupportFragmentManager(), getLifecycle());
        adapter.addTab(new MyTab("Home", ToolsFragment.newInstance()));
        adapter.addTab(new MyTab("History", HistoryFragment.newInstance()));
        adapter.addTab(new MyTab("Premium", SubsecribeFragment.newInstance()));
        adapter.addTab(new MyTab("Setting", SettingFragment.newInstance()));
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {

            tab.setText(adapter.getTabName(position));
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.home_selected);
                    break;
                case 1:
                    tab.setIcon(R.drawable.history_unselected);
                    break;
                case 2:
                    tab.setIcon(R.drawable.crown_unselected);
                    break;
                case 3:
                    tab.setIcon(R.drawable.settings_unselected);
                    break;
            }

            tab.setText(adapter.getTabName(position));
        }).attach();

        adapter.notifyDataSetChanged();

    }


    private void initUser() {

        databaseReference.child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                prefs.saveUser(user);
                loadFragment();
                dialogLoading.dismissLoadingProgressDialog();//send text

                Log.d("TAG", "onCreate:  "+
                        user.getName()+" "+user.getEmail()+" "+user.getUid()+" "+user.getEndSubscription()+" "+
                        user.getMembership()+" "+user.getWordPremium()+" "+user.getWordProcessing()
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }



}