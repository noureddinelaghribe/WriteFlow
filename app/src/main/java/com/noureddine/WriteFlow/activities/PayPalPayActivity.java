package com.noureddine.WriteFlow.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.BillingManagerPaypal;
import com.noureddine.WriteFlow.Utils.DialogLoading;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;

public class PayPalPayActivity extends AppCompatActivity {

    private WebView webView;
    private String paymentUrl;
    private String plan;
    private Button buttonCancelPayment;

    private DialogLoading dialogLoading;

    private EncryptedPrefsManager prefs;
    private DatabaseReference databaseReference;
    private BillingManagerPaypal billingManagerPaypal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay_pal_pay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonCancelPayment = findViewById(R.id.buttonCancelPayment);

        prefs = EncryptedPrefsManager.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        billingManagerPaypal = new BillingManagerPaypal(prefs,this,databaseReference);

        dialogLoading = new DialogLoading(PayPalPayActivity.this);
        dialogLoading.loadingProgressDialog("Loading...");
        dialogLoading.showLoadingProgressDialog();


        // Get payment URL from intent
        paymentUrl = getIntent().getStringExtra("payment_url");
        plan = getIntent().getStringExtra("plan");

        initializeWebView();
        loadPayPalPayment();


    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initializeWebView() {
        webView = findViewById(R.id.webPay);

        // Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);


        // Set WebViewClient to handle URL changes
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // This method is called when URL is about to change
                Log.d("PayPalPayActivity", "shouldOverrideUrlLoading: ");

                if (BillingManagerPaypal.checkPayment(plan, url)){
                    dialogLoading.dismissLoadingProgressDialog();
                    Toast.makeText(PayPalPayActivity.this, "payment successflly ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PayPalPayActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                return false; // Let WebView handle the URL
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // Called when page starts loading
                if (!PayPalPayActivity.this.isFinishing() && !PayPalPayActivity.this.isDestroyed()) {
                    dialogLoading.showLoadingProgressDialog();
                }
                Log.d("PayPalPayActivity", "onPageStarted: ");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Called when page finishes loading
                Log.d("PayPalPayActivity", "onPageFinished: "+url);
                if (url.equals(paymentUrl)) {
                    dialogLoading.dismissLoadingProgressDialog();
                }

            }
        });

    }

    private void loadPayPalPayment() {
        if (paymentUrl != null && !paymentUrl.isEmpty()) {
            webView.loadUrl(paymentUrl);
            buttonCancelPayment.setOnClickListener(v -> showCancelPaymentDialog(this));
        } else {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void handlePaymentCancel() {
        Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showCancelPaymentDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Cancel Payment")
                .setMessage("Are you sure you want to cancel this payment? Any progress will be lost.")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> {
                    // Cancel the payment process
                    dialog.dismiss();
                    handlePaymentCancel();
                })
                .setNegativeButton("Continue Payment", (dialog, which) -> {
                    // Dismiss the dialog, continue with payment
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Handle back press as cancellation
            showCancelPaymentDialog(this);
        }
    }


}