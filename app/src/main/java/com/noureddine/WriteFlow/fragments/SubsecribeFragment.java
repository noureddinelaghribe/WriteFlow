package com.noureddine.WriteFlow.fragments;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_ID;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_ID;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_LINK_CHECK;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_LINK_PAY;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PROCESS_LINK_CHECK;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PROCESS_LINK_PAY;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_ID;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_ID;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_LINK_CHECK;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_LINK_PAY;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PROCESS_LINK_CHECK;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PROCESS_LINK_PAY;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PROCESS_LIMIT;
import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noureddine.WriteFlow.R;

import com.noureddine.WriteFlow.Utils.DataCoverter;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.activities.MainActivity;
import com.noureddine.WriteFlow.activities.PayPalPayActivity;
import com.noureddine.WriteFlow.model.User;



//public class SubsecribeFragment extends Fragment implements BillingManager.PurchaseCallback {
public class SubsecribeFragment extends Fragment{

    private static final String TAG = "SubsecribeFragment";

    private LinearLayout linearLayoutPlans, linearLayoutPremium, linearLayoutMonthlyBasic, linearLayoutMonthlyPro, linearLayoutBasic, linearLayoutPro;
    private TextView textView ;
    private ViewPager2 viewPager;
    private Button pay ;
    private DataCoverter dataCoverter = new DataCoverter();
    private EncryptedPrefsManager prefs;
    private DatabaseReference databaseReference;
    private User user;
    //private BillingManager billingManager;
    //private BillingManagerPaypal billingManagerPaypal;
    //private SubscriptionBillingManager subscriptionBillingManager;




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
        linearLayoutMonthlyBasic = v.findViewById(R.id.linearLayoutMonthly);
        linearLayoutMonthlyPro = v.findViewById(R.id.linearLayoutfilter);
        linearLayoutBasic = v.findViewById(R.id.linearLayoutBasic);
        linearLayoutPro = v.findViewById(R.id.linearLayoutPro);
        textView = v.findViewById(R.id.textView17);
        pay = v.findViewById(R.id.button2);
        pay.setVisibility(View.GONE);

        prefs = EncryptedPrefsManager.getInstance(getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //billingManager = new BillingManager(getActivity(),this);

        initUI();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewPager = requireActivity().findViewById(R.id.viwepager);
                viewPager.setCurrentItem(0, true);
            }
        });

        linearLayoutMonthlyBasic.setOnClickListener(v1 -> {
            linearLayoutMonthlyBasic.setSelected(true);
            linearLayoutMonthlyPro.setSelected(false);
            linearLayoutBasic.setSelected(false);
            linearLayoutPro.setSelected(false);
            pay.setVisibility(View.VISIBLE);
            pay.setText("Continue With Plan Basic");
        });

        linearLayoutMonthlyPro.setOnClickListener(v1 -> {
            linearLayoutMonthlyBasic.setSelected(false);
            linearLayoutMonthlyPro.setSelected(true);
            linearLayoutBasic.setSelected(false);
            linearLayoutPro.setSelected(false);
            pay.setVisibility(View.VISIBLE);
            pay.setText("Continue With Plan Pro");
        });

        linearLayoutBasic.setOnClickListener(v1 -> {
            linearLayoutMonthlyBasic.setSelected(false);
            linearLayoutMonthlyPro.setSelected(false);
            linearLayoutBasic.setSelected(true);
            linearLayoutPro.setSelected(false);
            pay.setVisibility(View.VISIBLE);
            pay.setText("Continue With Pack Basic");
        });

        linearLayoutPro.setOnClickListener(v1 -> {
            linearLayoutMonthlyBasic.setSelected(false);
            linearLayoutMonthlyPro.setSelected(false);
            linearLayoutBasic.setSelected(false);
            linearLayoutPro.setSelected(true);
            pay.setVisibility(View.VISIBLE);
            pay.setText("Continue With Pack Pro");
        });

        pay.setOnClickListener(v1 -> {

//            if (linearLayoutMonthlyBasic.isSelected()){
//                billingManager.launchPurchaseFlow(SUB_ID_1);
//            }else if (linearLayoutMonthlyPro.isSelected()){
//                billingManager.launchPurchaseFlow(SUB_ID_2);
//            }else if (linearLayoutBasic.isSelected()){
//                billingManager.launchPurchaseFlow(PRODUCT_ID_1);
//            }else if (linearLayoutPro.isSelected()){
//                billingManager.launchPurchaseFlow(PRODUCT_ID_2);
//            }


            Intent intent = new Intent(getContext(), PayPalPayActivity.class);

            if (linearLayoutMonthlyBasic.isSelected()){
                intent.putExtra("payment_url", BASIC_PLAN_LINK_PAY);
                intent.putExtra("plan", BASIC_PLAN_NAME);
            }else if (linearLayoutMonthlyPro.isSelected()){
                intent.putExtra("payment_url", PRO_PLAN_LINK_PAY);
                intent.putExtra("plan", PRO_PLAN_NAME);
            }else if (linearLayoutBasic.isSelected()){
                intent.putExtra("payment_url", BASIC_PROCESS_LINK_PAY);
                intent.putExtra("plan", BASIC_NAME);
            }else if (linearLayoutPro.isSelected()){
                intent.putExtra("payment_url", PRO_PROCESS_LINK_PAY);
                intent.putExtra("plan", PRO_NAME);
            }

            startActivity(intent);


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

//    private void upgradePlan(String plan){
//
//        User user = prefs.getUser();
//
//        switch (plan){
//            case BASIC_PLAN_NAME:
//                user.setEndSubscription(endSubscription());
//                user.setMembership(BASIC_PLAN_NAME);
//                user.setWordPremium(BASIC_PLAN_PROCESS_LIMIT);
//                break;
//            case PRO_PLAN_NAME:
//                user.setEndSubscription(endSubscription());
//                user.setMembership(PRO_PLAN_NAME);
//                user.setWordPremium(PRO_PLAN_PROCESS_LIMIT);
//                break;
//            case BASIC_NAME:
//                long totalProcess1 = prefs.getUser().getWordProcessing()+BASIC_PROCESS_LIMIT;
//                user.setWordProcessing(totalProcess1);
//                break;
//            case PRO_NAME:
//                long totalProcess2 = prefs.getUser().getWordProcessing()+PRO_PROCESS_LIMIT;
//                user.setWordProcessing(totalProcess2);
//                break;
//            default:
//                Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//
//        prefs.saveUser(user);
//        databaseReference.child("Users").child(user.getUid()).setValue(user)
//                .addOnSuccessListener(aVoid -> {
//                    Log.d(TAG, "Data saved successfully endSubscription ");
//
//                    if (isAdded()) {
//                        Intent intent = new Intent(getContext(), MainActivity.class);
//                        requireContext().startActivity(intent);
//                    } else {
//                        Log.w(TAG, "Fragment not attached; skipping navigation");
//                    }
//
//                    })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "Error saving data endSubscription ", e);
//                });
//
//    }


//    private long endSubscription(){
//        long endSubscription = prefs.getLong("currentTime",0)+(86400*30);
//        Log.d(TAG, "endSubscription: "+endSubscription);
//        return endSubscription;
//    }










//    @Override
//    public void onPurchaseResult(boolean success, String message) {
//        Log.d(TAG, "success : " + success + " message : " + message);
//
//        if (message.equals(BASIC_PLAN_ID)){
//            upgradePlan(BASIC_PLAN_NAME);
//        }else if (message.equals(PRO_PLAN_ID)){
//            upgradePlan(PRO_PLAN_NAME);
//        }else if (message.equals(BASIC_ID)){
//            upgradePlan(BASIC_NAME);
//        }else if (message.equals(PRO_ID)){
//            upgradePlan(PRO_NAME);
//        }
//
//    }
}