package com.noureddine.WriteFlow.activities;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_PROCESS_LIMIT;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.noureddine.WriteFlow.Utils.DataCoverter;
import com.noureddine.WriteFlow.Utils.DialogLoading;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.adapter.AdapterPager;
import com.noureddine.WriteFlow.fragments.HistoryFragment;
import com.noureddine.WriteFlow.fragments.SettingFragment;
import com.noureddine.WriteFlow.fragments.SubsecribeFragment;
import com.noureddine.WriteFlow.fragments.ToolsFragment;
import com.noureddine.WriteFlow.model.Close;
import com.noureddine.WriteFlow.model.MyTab;
import com.noureddine.WriteFlow.model.TimeResponse;
import com.noureddine.WriteFlow.model.Update;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.viewModels.SettingsViewModel;
import com.noureddine.WriteFlow.viewModels.TimeViewModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomeActivity extends AppCompatActivity {

    private static final Logger log = LogManager.getLogger(HomeActivity.class);
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private AdapterPager adapter;
    private FirebaseAuth auth;
    private EncryptedPrefsManager prefs;
    private DatabaseReference databaseReference;
    private DialogLoading dialogLoading;
    private TimeViewModel timeViewModel;
    private long endSubscription;
    private static final String TAG = "TAGHomeActivity";
    private SettingsViewModel settingsViewModel;



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
        timeViewModel = new ViewModelProvider(this).get(TimeViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        dialogLoading = new DialogLoading(this);
        dialogLoading.loadingProgressDialog("Loading...");
        dialogLoading.showLoadingProgressDialog();

        initUser();
        fetchSettings();

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


    @SuppressLint("NotifyDataSetChanged")
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

                Log.d(TAG, "onCreate:  "+
                        user.getName()+" "+user.getEmail()+" "+user.getUid()+" "+user.getEndSubscription()+" "+
                        user.getMembership()+" "+user.getWordPremium()+" "+user.getWordProcessing()
                );

                //fetchServerTime();

                initTime();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    private void initTime() {

        // Observe the LiveData
        timeViewModel.getTimeZoneData().observe(this, new Observer<TimeResponse>() {
            @Override
            public void onChanged(TimeResponse response) {
                if (response != null) {
                    endSubscription = DataCoverter.dataToLong(response.getYear(),response.getMonth(),response.getDay(),response.getHour(),response.getMinute(),response.getSeconds());
                    prefs.saveLong("currentTime",endSubscription);
                    Log.d(TAG, "onChanged: "+endSubscription);
                    if (prefs.getUser().getEndSubscription() <= endSubscription){
                        User user = prefs.getUser();
                        user.setMembership(FREE_PLAN_NAME);
                        user.setEndSubscription(0);
                        user.setWordPremium(FREE_PLAN_PROCESS_LIMIT);
                        prefs.saveUser(user);
                        databaseReference.child("Users").child(user.getUid()).setValue(user)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Data saved successfully endSubscription ");
                                    loadFragment();
                                    dialogLoading.dismissLoadingProgressDialog();//send text
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error saving data endSubscription ", e);
                                });
                    }else {
                        loadFragment();
                        dialogLoading.dismissLoadingProgressDialog();//send text
                    }
                } else {
                    Log.d(TAG, "onChanged: "+"Failed to load time data "+response);
                    dialogLoading.dismissLoadingProgressDialog();//send text
                    showRestartDialog();
                }
            }
        });

    }


    public void fetchServerTime() {
        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long offset = snapshot.getValue(Long.class);
                if (offset != null) {
                    Log.d("ServerTime", "offset "+offset);
                    long estimatedServerTimeMs = System.currentTimeMillis() + offset;
                    Log.d("ServerTime", ""+estimatedServerTimeMs);
                    Log.d("ServerTime", ""+prefs.getLong("currentTime",0));

                    endSubscription = estimatedServerTimeMs;
                    prefs.saveLong("currentTime",endSubscription);
                    if (prefs.getUser().getEndSubscription() <= endSubscription){
                        Log.d(TAG, "onDataChange: prefs.getUser().getEndSubscription() <= endSubscription");
                        User user = prefs.getUser();
                        user.setMembership(FREE_PLAN_NAME);
                        user.setEndSubscription(0);
                        user.setWordPremium(FREE_PLAN_PROCESS_LIMIT);
                        prefs.saveUser(user);
                        databaseReference.child("Users").child(user.getUid()).setValue(user)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Data saved successfully endSubscription ");
                                    loadFragment();
                                    dialogLoading.dismissLoadingProgressDialog();//send text
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error saving data endSubscription ", e);
                                });
                    }else {
                        Log.d(TAG, "onDataChange: else");
                        loadFragment();
                        dialogLoading.dismissLoadingProgressDialog();//send text
                    }

                } else {
                    Log.w("ServerTime", "offset.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("ServerTime", "error offset", error.toException());
            }
        });
    }

    public void fetchSettings() {

        settingsViewModel.getSettings().observe(this, settings -> {
            if (settings.getClose().isCloseEnabled()){
                showMaintenanceClosureDialog(settings.getClose());
            }else if(settings.getUpdate().isUpdateEnabled() && !getVersionName(HomeActivity.this).equals(settings.getUpdate().getVerstion())){
                showUpdateDialog(settings.getUpdate());
            }

            prefs.saveToolPreferences(settings.getToolPreferences());

        });

    }



    private void showRestartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connection error")
                .setMessage("An error occurred while connecting to the server. Please restart the application.")
                .setCancelable(false)
                .setPositiveButton("Restart", (dialog, which) -> {
                    // إعادة تشغيل التطبيق
                    Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        // إضافة تأخير قصير قبل إنهاء العملية
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @SuppressLint("MissingInflatedId")
    private void showUpdateDialog(Update update) {

        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_new_verstion, null);

        // Get reference to the EditText in the dialog layout
        final Button btnLater = dialogView.findViewById(R.id.btn_later);
        final Button btnUpdate = dialogView.findViewById(R.id.btn_update);
        final TextView tvVerstion = dialogView.findViewById(R.id.update_verstion);
        final TextView tvWwhatsNew = dialogView.findViewById(R.id.whats_new);
        String url;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();;

        if (update != null){
            tvVerstion.setText(update.getVerstion());
            tvWwhatsNew.setText(update.getWhatsNew());
            url = update.getLink();
        } else {
            url = "";
        }

        btnUpdate.setOnClickListener(V->{
            if (!url.isEmpty()){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        btnLater.setOnClickListener(V->{
            dialog.dismiss();
        });

        // Show the dialog
        dialog.setCancelable(false);
        dialog.show();

    }


    @SuppressLint("MissingInflatedId")
    private void showMaintenanceClosureDialog(Close close) {

        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_maintenance_closure, null);

        // Get reference to the EditText in the dialog layout
        final Button btnReminder = dialogView.findViewById(R.id.btn_reminder);
        final Button btnClose = dialogView.findViewById(R.id.btn_close);
        final TextView tvDate = dialogView.findViewById(R.id.maintenance_time);
        final TextView tvContact = dialogView.findViewById(R.id.contact_info);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();;

        if (close != null){
            tvDate.setText(close.getCompletionTime());
            tvContact.setText(close.getContact());
        }

        btnReminder.setOnClickListener(V->{
            Toast.makeText(this, "We'll notify you when the app is back online", Toast.LENGTH_LONG).show();
        });

        btnClose.setOnClickListener(V->{
            finishAffinity();
        });

        // Show the dialog
        dialog.setCancelable(false);
        dialog.show();

    }


    public String getVersionName(Context context) {
        try {
            // Retrieve PackageInfo for this app (holds versionName, versionCode, etc.)
            PackageInfo pInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // Fallback in the unlikely event your own package isn’t found
            return "Unknown";
        }
    }



}