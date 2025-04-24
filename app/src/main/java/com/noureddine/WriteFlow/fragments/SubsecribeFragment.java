package com.noureddine.WriteFlow.fragments;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_ID;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_ID;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRODUCT_ID_1;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRODUCT_ID_2;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_ID;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_ID;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.SUB_ID_1;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.SUB_ID_2;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PROCESS_LIMIT;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import androidx.webkit.internal.ApiFeature;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.BillingManager;
import com.noureddine.WriteFlow.Utils.DataCoverter;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.activities.MainActivity;
import com.noureddine.WriteFlow.model.TimeResponse;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.viewModels.TimeViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class SubsecribeFragment extends Fragment implements BillingManager.PurchaseCallback {

    private static final String TAG = "SubsecribeFragment";

    private LinearLayout linearLayoutPlans, linearLayoutPremium, linearLayoutMonthlyBasic, linearLayoutMonthlyPro, linearLayoutBasic, linearLayoutPro;
    private TextView textView ;
    private ViewPager2 viewPager;
    private Button pay ;
    private DataCoverter dataCoverter = new DataCoverter();
    private EncryptedPrefsManager prefs;
    private DatabaseReference databaseReference;
    private User user;
    private BillingManager billingManager;
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
//        billingManager = new BillingManager(getActivity(), new BillingManager.PurchaseCallback() {
//            @Override
//            public void onPurchaseResult(boolean success, String message) {
//                // إنشاء كائن BillingManager مع تمرير رد الاتصال الذي يقوم بمعالجة نتائج الشراء
//                billingManager = new BillingManager(getActivity(), new BillingManager.PurchaseCallback() {
//                    @Override
//                    public void onPurchaseResult(boolean success, String message) {
//                        if (success) {
//                            Log.d(TAG+"BillingINAPP", "عملية الشراء نجحت: " + message);
//                            // هنا يمكنك إضافة كود لتحديث الواجهة أو منطق التطبيق عند نجاح الشراء
//
////                            if (message.equals(BASIC_PLAN_NAME)){
////                                upgradePlan(BASIC_PLAN_NAME);
////                            }else if (message.equals(PRO_PLAN_NAME)){
////                                upgradePlan(PRO_PLAN_NAME);
////                            }else if (message.equals(BASIC_NAME)){
////                                upgradePlan(BASIC_NAME);
////                            }else if (message.equals(PRO_NAME)){
////                                upgradePlan(PRO_NAME);
////                            }
//
//
//
//                        } else {
//                            Log.e(TAG+"BillingINAPP", "فشل عملية الشراء: " + message);
//                            // هنا يمكنك إضافة كود لمعالجة الخطأ أو إعلام المستخدم
//                        }
//                    }
//                });
//            }
//        });

        billingManager = new BillingManager(getActivity(),this);

//        subscriptionBillingManager =  new SubscriptionBillingManager(getActivity(), new SubscriptionBillingManager.BillingCallback() {
//            @Override
//            public void onBillingInitialized(boolean success) {
//                if (success) {
//                    // استعلام عن تفاصيل المنتجات بعد نجاح الاتصال
//                    List<String> subIds = Arrays.asList(SUB_ID_1, SUB_ID_2);
//                    subscriptionBillingManager.queryProductDetails(subIds, SubscriptionBillingManager.PRODUCT_TYPE_SUBS);
//                } else {
//                    Toast.makeText(getContext(), "فشل الاتصال بخدمة الفوترة", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onProductDetailsReceived(List<ProductDetails> productDetails) {
//                // عرض المنتجات للمستخدم (مثل إنشاء أزرار للشراء)
//            }
//
//            @Override
//            public void onPurchaseCompleted(Purchase purchase) {
//                // تم إكمال الشراء بنجاح
//                Toast.makeText(getContext(), "تم الشراء بنجاح", Toast.LENGTH_SHORT).show();
//                Log.d(TAG+"SubscriptionBillingManager", "تم الشراء بنجاح  onPurchaseCompleted");
//
//                // تفعيل ميزات الاشتراك
//            }
//
//            @Override
//            public void onPurchaseFailed(int errorCode, String errorMessage) {
//                // فشل عملية الشراء
//                Toast.makeText(getContext(), "فشل الشراء: " + errorMessage, Toast.LENGTH_SHORT).show();
//                Log.d(TAG+"SubscriptionBillingManager", "onPurchaseFailed: "+"فشل الشراء: " + errorMessage);
//
//            }
//
//            @Override
//            public void onSubscriptionStatus(boolean isActive, String subscriptionId, long expiryTimeMillis) {
//                // تحديث واجهة المستخدم بناءً على حالة الاشتراك
//                if (isActive) {
//                    // تفعيل ميزات الاشتراك
//                    Date expiryDate = new Date(expiryTimeMillis);
//                    String currentTime = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a").format(expiryTimeMillis);
//                    Log.d(TAG+"SubscriptionBillingManager", "onSubscriptionStatus: "+currentTime);
//                    // عرض تاريخ انتهاء الاشتراك
//                } else {
//                    // عرض خيارات الشراء
//                    Log.d(TAG+"SubscriptionBillingManager", "onSubscriptionStatus: عرض خيارات الشراء");
//                }
//            }
//        });

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

            //billingManager.launchPurchaseFlow("basic_word_processing");


            if (linearLayoutMonthlyBasic.isSelected()){
                //subscriptionBillingManager.purchase(SUB_ID_1);
                billingManager.launchPurchaseFlow(SUB_ID_1);
                //upgradePlan(BASIC_PLAN_NAME);
            }else if (linearLayoutMonthlyPro.isSelected()){
                //subscriptionBillingManager.purchase(SUB_ID_2);
                billingManager.launchPurchaseFlow(SUB_ID_2);
                //upgradePlan(PRO_PLAN_NAME);
            }else if (linearLayoutBasic.isSelected()){
                billingManager.launchPurchaseFlow(PRODUCT_ID_1);
                //upgradePlan(BASIC_NAME);
            }else if (linearLayoutPro.isSelected()){
                billingManager.launchPurchaseFlow(PRODUCT_ID_2);
                //upgradePlan(PRO_NAME);
            }


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

    private void upgradePlan(String plan){

        User user = prefs.getUser();

        switch (plan){
            case BASIC_PLAN_NAME:
                user.setEndSubscription(endSubscription());
                user.setMembership(BASIC_PLAN_NAME);
                user.setWordPremium(BASIC_PLAN_PROCESS_LIMIT);
                break;
            case PRO_PLAN_NAME:
                user.setEndSubscription(endSubscription());
                user.setMembership(PRO_PLAN_NAME);
                user.setWordPremium(PRO_PLAN_PROCESS_LIMIT);
                break;
            case BASIC_NAME:
                long totalProcess1 = prefs.getUser().getWordProcessing()+BASIC_PROCESS_LIMIT;
                user.setWordProcessing(totalProcess1);
                break;
            case PRO_NAME:
                long totalProcess2 = prefs.getUser().getWordProcessing()+PRO_PROCESS_LIMIT;
                user.setWordProcessing(totalProcess2);
                break;
            default:
                Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                break;
        }


        prefs.saveUser(user);
        databaseReference.child("Users").child(user.getUid()).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Data saved successfully endSubscription ");
//                    Intent intent = new Intent(getContext(), MainActivity.class);
//                    getContext().startActivity(intent);

                    if (isAdded()) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        requireContext().startActivity(intent);
                    } else {
                        Log.w(TAG, "Fragment not attached; skipping navigation");
                    }

                    })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving data endSubscription ", e);
                });

    }


    private long endSubscription(){
        long endSubscription = prefs.getLong("currentTime",0)+(86400*30);
        Log.d(TAG, "endSubscription: "+endSubscription);
        return endSubscription;
    }


    @Override
    public void onPurchaseResult(boolean success, String message) {
        Log.d(TAG, "success : " + success + " message : " + message);

        if (message.equals(BASIC_PLAN_ID)){
            upgradePlan(BASIC_PLAN_NAME);
        }else if (message.equals(PRO_PLAN_ID)){
            upgradePlan(PRO_PLAN_NAME);
        }else if (message.equals(BASIC_ID)){
            upgradePlan(BASIC_NAME);
        }else if (message.equals(PRO_ID)){
            upgradePlan(PRO_NAME);
        }

    }
}