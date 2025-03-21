package com.noureddine.WriteFlow.fragments;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.DataCoverter;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.model.User;


public class SubsecribeFragment extends Fragment {

    LinearLayout linearLayoutPlans ;
    LinearLayout linearLayoutPremium ;
    LinearLayout linearLayoutMonthly ;
    LinearLayout linearLayoutAnnul ;
    LinearLayout linearLayoutBasic ;
    LinearLayout linearLayoutPro ;
    TextView textView ;
    Button pay ;
    DataCoverter dataCoverter = new DataCoverter();
    EncryptedPrefsManager prefs;
    private DatabaseReference databaseReference;
    User user;



    public SubsecribeFragment() {}

    public static SubsecribeFragment newInstance() {
        SubsecribeFragment fragment = new SubsecribeFragment();
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
        View v = inflater.inflate(R.layout.fragment_subsecribe,container,false);

        linearLayoutPlans = v.findViewById(R.id.linearLayoutPlans);
        linearLayoutPremium = v.findViewById(R.id.linearLayoutPremium);
        linearLayoutMonthly = v.findViewById(R.id.linearLayoutMonthly);
        linearLayoutAnnul = v.findViewById(R.id.linearLayoutfilter);
        linearLayoutBasic = v.findViewById(R.id.linearLayoutBasic);
        linearLayoutPro = v.findViewById(R.id.linearLayoutPro);
        textView = v.findViewById(R.id.textView17);
        pay = v.findViewById(R.id.button2);
        pay.setVisibility(View.GONE);

        prefs = EncryptedPrefsManager.getInstance(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initUI();


        linearLayoutMonthly.setOnClickListener(v1 -> {
            linearLayoutMonthly.setSelected(true);
            linearLayoutAnnul.setSelected(false);
            linearLayoutBasic.setSelected(false);
            linearLayoutPro.setSelected(false);
            pay.setVisibility(View.VISIBLE);
            pay.setText("Continue With Plan Basic");
        });

        linearLayoutAnnul.setOnClickListener(v1 -> {
            linearLayoutMonthly.setSelected(false);
            linearLayoutAnnul.setSelected(true);
            linearLayoutBasic.setSelected(false);
            linearLayoutPro.setSelected(false);
            pay.setVisibility(View.VISIBLE);
            pay.setText("Continue With Plan Pro");
        });

        linearLayoutBasic.setOnClickListener(v1 -> {
            linearLayoutMonthly.setSelected(false);
            linearLayoutAnnul.setSelected(false);
            linearLayoutBasic.setSelected(true);
            linearLayoutPro.setSelected(false);
            pay.setVisibility(View.VISIBLE);
            pay.setText("Continue With Pack Basic");
        });

        linearLayoutPro.setOnClickListener(v1 -> {
            linearLayoutMonthly.setSelected(false);
            linearLayoutAnnul.setSelected(false);
            linearLayoutBasic.setSelected(false);
            linearLayoutPro.setSelected(true);
            pay.setVisibility(View.VISIBLE);
            pay.setText("Continue With Pack Pro");
        });

        pay.setOnClickListener(v1 -> {
            Toast.makeText(getContext(), "pay", Toast.LENGTH_SHORT).show();
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        initUI();

    }


    private void initUI() {

        user = prefs.getUser();

        if (user.getMembership().equals(FREE_PLAN_NAME)){
            linearLayoutPlans.setVisibility(View.VISIBLE);
            linearLayoutPremium.setVisibility(View.GONE);
        }else {
            linearLayoutPlans.setVisibility(View.GONE);
            linearLayoutPremium.setVisibility(View.VISIBLE);
            textView.setText("Enjoy exclusive features and priority support. Thank you for choosing us. Your subscription remains active until : \n"+dataCoverter.longToDataWithNameMonthe(user.getEndSubscription())+".");
        }

    }


}