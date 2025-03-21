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


    HomeActivity activity ;
    ViewPager2 viewPager ;
    LinearLayout linearLayoutUpgrade ;
    LinearLayout linearLayoutPremium ;
    Button upgradeButton ;
    TextView rateGooglrPlay ;
    TextView ternsAndPrivacy ;
    TextView contactUs ;
    TextView name ;
    TextView email ;
    TextView membership ;
    TextView endSubscribtion ;
    TextView wordPremium ;
    TextView wordProcessing ;
    DataCoverter dataCoverter = new DataCoverter();
    NumberFormat numberFormat = new NumberFormat();
    EncryptedPrefsManager prefs;
    User user ;



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
        ternsAndPrivacy = v.findViewById(R.id.textView14);
        contactUs = v.findViewById(R.id.textView15);
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
                String url = "https://link.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        ternsAndPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://link.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://link.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
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