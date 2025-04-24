package com.noureddine.WriteFlow.fragments;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_PROCESS_LIMIT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.DataCoverter;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.Utils.NumberFormat;
import com.noureddine.WriteFlow.activities.HomeActivity;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.repositorys.FirebaseRepository;


public class SettingFragment extends Fragment {

    private HomeActivity activity ;
    private ViewPager2 viewPager ;
    private LinearLayout linearLayoutUpgrade, linearLayoutPremium;
    private Button upgradeButton ;
    private TextView rateGooglrPlay, privacyPolicy, ternsofservice, contactUs, name, email, membership, endSubscribtion, wordPremium, wordProcessing;
    private DataCoverter dataCoverter = new DataCoverter();
    private NumberFormat numberFormat = new NumberFormat();
    private EncryptedPrefsManager prefs;
    private User user ;


    public SettingFragment() {}

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_setting,container,false);

        linearLayoutUpgrade = v.findViewById(R.id.linearLayoutUpgrade);
        linearLayoutPremium = v.findViewById(R.id.linearLayoutPremium);
        upgradeButton = v.findViewById(R.id.button3);
        rateGooglrPlay = v.findViewById(R.id.textView13);
        privacyPolicy = v.findViewById(R.id.textView14);
        ternsofservice = v.findViewById(R.id.textView15);
        contactUs = v.findViewById(R.id.textView19);
        name = v.findViewById(R.id.textView10);
        email = v.findViewById(R.id.textView11);
        membership = v.findViewById(R.id.textView12);
        endSubscribtion = v.findViewById(R.id.textView16);
        wordPremium = v.findViewById(R.id.textView17);
        wordProcessing = v.findViewById(R.id.textView18);

        prefs = EncryptedPrefsManager.getInstance(getContext());
        initUI();

        // Get the ViewPager2 from the activity
        activity = (HomeActivity) getActivity();
        viewPager = activity.getViewPager2(); // You'll need to create this method
        viewPager.setCurrentItem(3, true); // true for smooth scroll animation

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewPager = requireActivity().findViewById(R.id.viwepager);
                viewPager.setCurrentItem(0, true);
            }
        });

        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inside your fragment
                ViewPager2 viewPager = requireActivity().findViewById(R.id.viwepager);
                viewPager.setCurrentItem(2, true);
            }
        });

        rateGooglrPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://play.google.com/store/apps/details?id=com.noureddine.WriteFlow";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://bit.ly/4hW3sa3";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        ternsofservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://bit.ly/42e2iAv";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String url = "https://link.com/";
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:")); // هذا يحدد أن النية لإرسال بريد إلكتروني فقط
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"WordLoom0@gmail.com"});
                startActivity(emailIntent);


            }
        });



        return v;
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();

        initUI();

    }


    private void initUI() {

        user = prefs.getUser();

        if (!user.getMembership().equals(FREE_PLAN_NAME)){
            linearLayoutUpgrade.setVisibility(View.GONE);
            Log.d("TAG", "initUI: "+user.getEndSubscription());
            Log.d("TAG", "initUI: "+System.currentTimeMillis());
            Log.d("TAG", "initUI: "+dataCoverter.longToDataWithNameMonthe(user.getEndSubscription()));
            endSubscribtion.setText(dataCoverter.longToDataWithNameMonthe(user.getEndSubscription()));
            linearLayoutUpgrade.setVisibility(View.GONE);
            linearLayoutPremium.setVisibility(View.VISIBLE);
        }else{
            endSubscribtion.setText("--/--/----");
            linearLayoutUpgrade.setVisibility(View.VISIBLE);
            linearLayoutPremium.setVisibility(View.GONE);
        }

        name.setText(user.getName());
        email.setText(user.getEmail());
        membership.setText(user.getMembership());

        switch (user.getMembership()){
            case FREE_PLAN_NAME :
                wordPremium.setText(numberFormat.String(user.getWordPremium())+" / "+numberFormat.String(FREE_PLAN_PROCESS_LIMIT) +" word");
                break;
            case BASIC_PLAN_NAME :
                wordPremium.setText(numberFormat.String(user.getWordPremium())+" / "+ numberFormat.String(BASIC_PLAN_PROCESS_LIMIT) +" word");
                break;
            case PRO_PLAN_NAME :
                wordPremium.setText(numberFormat.String(user.getWordPremium())+" / "+ numberFormat.String(PRO_PLAN_PROCESS_LIMIT) +" word");
                break;
        }

        wordProcessing.setText(numberFormat.String(user.getWordProcessing())+" word");

    }


}