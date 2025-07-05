package com.noureddine.WriteFlow.Utils;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PROCESS_LIMIT;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.noureddine.WriteFlow.activities.MainActivity;
import com.noureddine.WriteFlow.model.User;

public class BillingManagerPaypal {

    private static EncryptedPrefsManager prefs;
    private static Context context;
    private static DatabaseReference databaseReference;

    public BillingManagerPaypal(EncryptedPrefsManager prefs, Context context, DatabaseReference databaseReference) {
        this.prefs = prefs;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    public static boolean checkPayment(String plan, String url) {
        // Check if URL contains required parts
        if (url.contains("example.com") &&
                url.contains("token=") &&
                url.contains("st=COMPLETED")) {

            upgradePlan(plan);
            return true;

        }
        return false;
    }


    private static void upgradePlan(String plan){

        Log.d("BillingManagerPaypal", "upgradePlan: upgradePlan");
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
                Log.d("BillingManagerPaypal", "upgradePlan: BASIC_NAME");
                long totalProcess1 = prefs.getUser().getWordProcessing()+BASIC_PROCESS_LIMIT;
                user.setWordProcessing(totalProcess1);
                break;
            case PRO_NAME:
                long totalProcess2 = prefs.getUser().getWordProcessing()+PRO_PROCESS_LIMIT;
                user.setWordProcessing(totalProcess2);
                break;
            default:
                Toast.makeText(context, "Unexpected error", Toast.LENGTH_SHORT).show();
                break;
        }


        prefs.saveUser(user);
        databaseReference.child("Users").child(user.getUid()).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("TAG", "Data saved successfully endSubscription ");

                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);

                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error saving data endSubscription ", e);
                });

    }


    private static long endSubscription(){
        long endSubscription = prefs.getLong("currentTime",0)+(86400*30);
        Log.d("TAG", "endSubscription: "+endSubscription);
        return endSubscription;
    }


}
