//package com.noureddine.WriteFlow.Utils;
//
//import android.app.Activity;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.android.billingclient.api.AcknowledgePurchaseParams;
//import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
//import com.android.billingclient.api.BillingClient;
//import com.android.billingclient.api.BillingClientStateListener;
//import com.android.billingclient.api.BillingFlowParams;
//import com.android.billingclient.api.BillingResult;
//import com.android.billingclient.api.ProductDetails;
//import com.android.billingclient.api.ProductDetailsResponseListener;
//import com.android.billingclient.api.Purchase;
//import com.android.billingclient.api.PurchasesResponseListener;
//import com.android.billingclient.api.PurchasesUpdatedListener;
//import com.android.billingclient.api.QueryProductDetailsParams;
//import com.android.billingclient.api.QueryPurchasesParams;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class BillingManager {
//    private BillingClient billingClient;
//    private Activity activity;
//    private List<ProductDetails> availableProducts = new ArrayList<>();
//
//    public BillingManager(Activity activity) {
//        this.activity = activity;
//
//        // Initialize the billing client
//        billingClient = BillingClient.newBuilder(activity)
//                .setListener(new PurchasesUpdatedListener() {
//                    @Override
//                    public void onPurchasesUpdated(@NonNull BillingResult billingResult,
//                                                   @Nullable List<Purchase> purchases) {
//                        // Handle purchase updates here
//                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
//                            for (Purchase purchase : purchases) {
//                                handlePurchase(purchase);
//                            }
//                        }
//                    }
//                })
//                .enablePendingPurchases()
//                .build();
//
//        // Connect to Google Play
//        startConnection();
//    }
//
//    private void startConnection() {
//        billingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
//                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                    // Billing client is ready
//                    queryAvailableProducts();
//                }
//            }
//
//            @Override
//            public void onBillingServiceDisconnected() {
//                // Retry connection
//                startConnection();
//            }
//        });
//    }
//
////    private void queryAvailableProducts() {
////        // Build a query for in-app products
////        QueryProductDetailsParams.Builder paramsBuilder = QueryProductDetailsParams.newBuilder();
////        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
////
////        // Add your product IDs here
////        productList.add(QueryProductDetailsParams.Product.newBuilder()
////                .setProductId("your_product_id1")
////                .setProductType(BillingClient.ProductType.INAPP)
////                .build());
////
////        // Add your product IDs here
////        productList.add(QueryProductDetailsParams.Product.newBuilder()
////                .setProductId("your_product_id2")
////                .setProductType(BillingClient.ProductType.INAPP)
////                .build());
////
////        // Add your product IDs here
////        productList.add(QueryProductDetailsParams.Product.newBuilder()
////                .setProductId("your_sub1")
////                .setProductType(BillingClient.ProductType.SUBS)
////                .build());
////
////        // Add your product IDs here
////        productList.add(QueryProductDetailsParams.Product.newBuilder()
////                .setProductId("your_sub2")
////                .setProductType(BillingClient.ProductType.SUBS)
////                .build());
////
////        paramsBuilder.setProductList(productList);
////
////        // Query product details
////        billingClient.queryProductDetailsAsync(paramsBuilder.build(),
////                new ProductDetailsResponseListener() {
////                    @Override
////                    public void onProductDetailsResponse(@NonNull BillingResult billingResult,
////                                                         @NonNull List<ProductDetails> productDetailsList) {
////                        // Process the product details
////                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
////                            // Save product details for later use when making purchases
////                        }
////                    }
////                });
////    }
//
//
//    private void queryAvailableProducts() {
//        String[] inAppIds = {"basic_word_processing", "pro_word_processing"};
//        String[] subsIds = {"basic_subscribe", "pro_subscribe"};
//        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
//
//        for (String id : inAppIds) {
//            productList.add(
//                    QueryProductDetailsParams.Product.newBuilder()
//                            .setProductId(id)
//                            .setProductType(BillingClient.ProductType.INAPP)
//                            .build()
//            );
//        }
//
//        for (String id : subsIds) {
//            productList.add(
//                    QueryProductDetailsParams.Product.newBuilder()
//                            .setProductId(id)
//                            .setProductType(BillingClient.ProductType.SUBS)
//                            .build()
//            );
//        }
//
//        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
//                .setProductList(productList)
//                .build();
//
//        billingClient.queryProductDetailsAsync(params, (billingResult, productDetailsList) -> {
//            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                availableProducts.clear();
//                availableProducts.addAll(productDetailsList);
//                Log.d("Billing", "تم تحميل " + productDetailsList.size() + " منتج.");
//            } else {
//                Log.e("Billing", "فشل الاستعلام: " + billingResult.getDebugMessage());
//            }
//        });
//    }
//
//
//    public void launchPurchaseFlow(ProductDetails productDetails) {
//        List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList = new ArrayList<>();
//        productDetailsParamsList.add(
//                BillingFlowParams.ProductDetailsParams.newBuilder()
//                        .setProductDetails(productDetails)
//                        .build()
//        );
//
//        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
//                .setProductDetailsParamsList(productDetailsParamsList)
//                .build();
//
//        billingClient.launchBillingFlow(activity, billingFlowParams);
//    }
//
//    private void handlePurchase(Purchase purchase) {
//        // Verify the purchase (should verify on your server with Google Play Developer API)
//        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
//            // Grant the item to the user
//
//            // Acknowledge the purchase
//            if (!purchase.isAcknowledged()) {
//                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
//                        .setPurchaseToken(purchase.getPurchaseToken())
//                        .build();
//
//                billingClient.acknowledgePurchase(acknowledgePurchaseParams,
//                        new AcknowledgePurchaseResponseListener() {
//                            @Override
//                            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
//                                // Handle acknowledgment response
//                            }
//                        });
//            }
//        }
//    }
//
//    // Call this method when your activity is destroyed
//    public void endConnection() {
//        if (billingClient != null) {
//            billingClient.endConnection();
//        }
//    }
//}






















//
//
//package com.noureddine.WriteFlow.Utils;
//
//import android.app.Activity;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.android.billingclient.api.AcknowledgePurchaseParams;
//import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
//import com.android.billingclient.api.BillingClient;
//import com.android.billingclient.api.BillingClientStateListener;
//import com.android.billingclient.api.BillingFlowParams;
//import com.android.billingclient.api.BillingResult;
//import com.android.billingclient.api.ProductDetails;
//import com.android.billingclient.api.ProductDetailsResponseListener;
//import com.android.billingclient.api.Purchase;
//import com.android.billingclient.api.PurchasesUpdatedListener;
//import com.android.billingclient.api.QueryProductDetailsParams;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class BillingManager {
//    private static final String TAG = "BillingManager";
//    private BillingClient billingClient;
//    private Activity activity;
//    private List<ProductDetails> availableProducts = new ArrayList<>();
//    private Map<String, ProductDetails> productDetailsMap = new HashMap<>();
//    private BillingManagerListener listener;
//
//    // تعريف معرفات المنتجات - يجب أن تكون مطابقة تماماً لمعرفات MainActivity
//    private static final String PRODUCT_ID_1 = "basic_word_processing";
//    private static final String PRODUCT_ID_2 = "pro_word_processing";
//    private static final String SUB_ID_1 = "basic_subscribe";
//    private static final String SUB_ID_2 = "pro_subscribe";
//
//    public interface BillingManagerListener {
//        void onBillingManagerReady();
//        void onProductsQueried(List<ProductDetails> products);
//        void onPurchaseSuccessful(String productId);
//        void onPurchaseFailed(int errorCode, String errorMessage);
//    }
//
//    public BillingManager(Activity activity) {
//        this.activity = activity;
//
//        // Initialize the billing client
//        billingClient = BillingClient.newBuilder(activity)
//                .setListener(new PurchasesUpdatedListener() {
//                    @Override
//                    public void onPurchasesUpdated(@NonNull BillingResult billingResult,
//                                                   @Nullable List<Purchase> purchases) {
//                        // Handle purchase updates here
//                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
//                            for (Purchase purchase : purchases) {
//                                handlePurchase(purchase);
//                            }
//                        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
//                            Log.d(TAG, "تم إلغاء عملية الشراء من قبل المستخدم");
//                            if (listener != null) {
//                                listener.onPurchaseFailed(billingResult.getResponseCode(), "تم إلغاء العملية");
//                            }
//                        } else {
//                            Log.e(TAG, "فشلت عملية الشراء: " + billingResult.getDebugMessage());
//                            if (listener != null) {
//                                listener.onPurchaseFailed(billingResult.getResponseCode(), billingResult.getDebugMessage());
//                            }
//                        }
//                    }
//                })
//                .enablePendingPurchases()
//                .build();
//
//        // Connect to Google Play
//        startConnection();
//    }
//
//    public void setBillingManagerListener(BillingManagerListener listener) {
//        this.listener = listener;
//    }
//
//    private void startConnection() {
//        billingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
//                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                    // Billing client is ready
//                    Log.d(TAG, "تم توصيل خدمة الفواتير بنجاح");
//
//                    // استدعاء queryAvailableProducts قبل إشعار المستمع
//                    // لضمان تحميل تفاصيل المنتجات أولاً
//                    queryAvailableProducts();
//                } else {
//                    Log.e(TAG, "فشل توصيل خدمة الفواتير: " + billingResult.getDebugMessage());
//                    if (listener != null) {
//                        listener.onPurchaseFailed(billingResult.getResponseCode(), billingResult.getDebugMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onBillingServiceDisconnected() {
//                // Retry connection
//                Log.d(TAG, "انقطع الاتصال بخدمة الفواتير، جاري إعادة المحاولة...");
//                startConnection();
//            }
//        });
//    }
//
////    private void queryAvailableProducts() {
////        // استخدام المعرفات الثابتة المطابقة لما في MainActivity
////        String[] inAppIds = {PRODUCT_ID_1, PRODUCT_ID_2};
////        String[] subsIds = {SUB_ID_1, SUB_ID_2};
////        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
////
////        for (String id : inAppIds) {
////            productList.add(
////                    QueryProductDetailsParams.Product.newBuilder()
////                            .setProductId(id)
////                            .setProductType(BillingClient.ProductType.INAPP)
////                            .build()
////            );
////        }
////
////        for (String id : subsIds) {
////            productList.add(
////                    QueryProductDetailsParams.Product.newBuilder()
////                            .setProductId(id)
////                            .setProductType(BillingClient.ProductType.SUBS)
////                            .build()
////            );
////        }
////
////        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
////                .setProductList(productList)
////                .build();
////
////        billingClient.queryProductDetailsAsync(params, (billingResult, productDetailsList) -> {
////            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
////                availableProducts.clear();
////                productDetailsMap.clear();
////                availableProducts.addAll(productDetailsList);
////
////                // طباعة أسماء المنتجات التي تم تحميلها للتشخيص
////                Log.d(TAG, "المنتجات المتاحة:");
////                for (ProductDetails details : productDetailsList) {
////                    Log.d(TAG, "تم العثور على منتج: " + details.getProductId());
////                    productDetailsMap.put(details.getProductId(), details);
////                }
////
////                Log.d(TAG, "تم تحميل " + productDetailsList.size() + " منتج.");
////
////                // إشعار المستمع بأن المنتجات جاهزة والاتصال مكتمل
////                if (listener != null) {
////                    listener.onProductsQueried(productDetailsList);
////                    // إشعار بجاهزية BillingManager بعد تحميل المنتجات
////                    listener.onBillingManagerReady();
////                }
////
////                // تحقق من المشتريات السابقة
////                checkPurchases();
////            } else {
////                Log.e(TAG, "فشل الاستعلام عن المنتجات: " + billingResult.getDebugMessage());
////                if (listener != null) {
////                    listener.onPurchaseFailed(billingResult.getResponseCode(), "فشل الاستعلام عن المنتجات: " + billingResult.getDebugMessage());
////                }
////            }
////        });
////    }
//
//
//
//    private void queryAvailableProducts() {
//        // Build a query for in-app products
//        QueryProductDetailsParams.Builder paramsBuilder = QueryProductDetailsParams.newBuilder();
//        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
//
//        // Add your product IDs here
//        productList.add(QueryProductDetailsParams.Product.newBuilder()
//                .setProductId("PRODUCT_ID_1")
//                .setProductType(BillingClient.ProductType.INAPP)
//                .build());
//
////        // Add your product IDs here
////        productList.add(QueryProductDetailsParams.Product.newBuilder()
////                .setProductId("PRODUCT_ID_2")
////                .setProductType(BillingClient.ProductType.INAPP)
////                .build());
////
////        // Add your product IDs here
////        productList.add(QueryProductDetailsParams.Product.newBuilder()
////                .setProductId("SUB_ID_1")
////                .setProductType(BillingClient.ProductType.SUBS)
////                .build());
////
////        // Add your product IDs here
////        productList.add(QueryProductDetailsParams.Product.newBuilder()
////                .setProductId("SUB_ID_2")
////                .setProductType(BillingClient.ProductType.SUBS)
////                .build());
//
//        paramsBuilder.setProductList(productList);
//
//        // Query product details
//        billingClient.queryProductDetailsAsync(paramsBuilder.build(),
//                new ProductDetailsResponseListener() {
//                    @Override
//                    public void onProductDetailsResponse(@NonNull BillingResult billingResult,
//                                                         @NonNull List<ProductDetails> productDetailsList) {
//                        // Process the product details
//                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                            // Save product details for later use when making purchases
//                        }
//                    }
//                });
//    }
//
//
//
//    private void checkPurchases() {
//        // التحقق من المشتريات السابقة لمنتجات الشراء لمرة واحدة
//        billingClient.queryPurchasesAsync(
//                BillingClient.ProductType.INAPP,
//                (billingResult, purchases) -> {
//                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                        processPurchases(purchases);
//                    }
//                }
//        );
//
//        // التحقق من الاشتراكات النشطة
//        billingClient.queryPurchasesAsync(
//                BillingClient.ProductType.SUBS,
//                (billingResult, purchases) -> {
//                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                        processPurchases(purchases);
//                    }
//                }
//        );
//    }
//
//    private void processPurchases(List<Purchase> purchases) {
//        for (Purchase purchase : purchases) {
//            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
//                // التحقق مما إذا كان المنتج تم شراؤه بالفعل
//                handlePurchase(purchase);
//            }
//        }
//    }
//
//    public void launchPurchaseFlow(String productId) {
//        ProductDetails productDetails = productDetailsMap.get(productId);
//
//        if (productDetails == null) {
//            Log.e(TAG, "لم يتم العثور على تفاصيل المنتج: " + productId);
//
//            // التحقق ما إذا كانت المنتجات قد تم تحميلها
//            if (productDetailsMap.isEmpty()) {
//                Log.e(TAG, "لم يتم تحميل أي منتجات. إعادة الاستعلام...");
//                // إعادة الاستعلام عن المنتجات
//                queryAvailableProducts();
//            }
//
//            if (listener != null) {
//                listener.onPurchaseFailed(-1, "لم يتم العثور على تفاصيل المنتج: " + productId);
//            }
//            return;
//        }
//
//        launchPurchaseFlow(productDetails);
//    }
//
//    public void launchPurchaseFlow(ProductDetails productDetails) {
//        if (productDetails == null) {
//            Log.e(TAG, "تفاصيل المنتج فارغة!");
//            return;
//        }
//
//        List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList = new ArrayList<>();
//
//        // التحقق من نوع المنتج (اشتراك أو شراء لمرة واحدة)
//        if (productDetails.getProductType().equals(BillingClient.ProductType.SUBS)) {
//            // للاشتراكات، يجب تحديد بيانات العرض (offer)
//            if (!productDetails.getSubscriptionOfferDetails().isEmpty()) {
//                productDetailsParamsList.add(
//                        BillingFlowParams.ProductDetailsParams.newBuilder()
//                                .setProductDetails(productDetails)
//                                .setOfferToken(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken())
//                                .build()
//                );
//            }
//        } else {
//            // للمنتجات العادية
//            productDetailsParamsList.add(
//                    BillingFlowParams.ProductDetailsParams.newBuilder()
//                            .setProductDetails(productDetails)
//                            .build()
//            );
//        }
//
//        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
//                .setProductDetailsParamsList(productDetailsParamsList)
//                .build();
//
//        BillingResult billingResult = billingClient.launchBillingFlow(activity, billingFlowParams);
//
//        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
//            Log.e(TAG, "فشل بدء عملية الشراء: " + billingResult.getDebugMessage());
//            if (listener != null) {
//                listener.onPurchaseFailed(billingResult.getResponseCode(), billingResult.getDebugMessage());
//            }
//        }
//    }
//
//    private void handlePurchase(Purchase purchase) {
//        // التحقق من حالة الشراء
//        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
//            // التحقق من صحة الشراء (يجب التحقق على الخادم الخاص بك باستخدام Google Play Developer API)
//
//            // إعلام المستمع بنجاح الشراء
//            if (listener != null) {
//                for (String productId : purchase.getProducts()) {
//                    listener.onPurchaseSuccessful(productId);
//                }
//            }
//
//            // تأكيد الشراء إذا لم يتم تأكيده بعد
//            if (!purchase.isAcknowledged()) {
//                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
//                        .setPurchaseToken(purchase.getPurchaseToken())
//                        .build();
//
//                billingClient.acknowledgePurchase(acknowledgePurchaseParams,
//                        new AcknowledgePurchaseResponseListener() {
//                            @Override
//                            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
//                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                                    Log.d(TAG, "تم تأكيد عملية الشراء بنجاح");
//                                } else {
//                                    Log.e(TAG, "فشل تأكيد عملية الشراء: " + billingResult.getDebugMessage());
//                                }
//                            }
//                        });
//            }
//        }
//    }
//
//    public ProductDetails getProductDetails(String productId) {
//        ProductDetails details = productDetailsMap.get(productId);
//        if (details == null) {
//            Log.d(TAG, "لم يتم العثور على تفاصيل المنتج: " + productId);
//        }
//        return details;
//    }
//
//    public List<ProductDetails> getAvailableProducts() {
//        return availableProducts;
//    }
//
//    // استدعاء هذه الطريقة عند تدمير النشاط
//    public void endConnection() {
//        if (billingClient != null) {
//            billingClient.endConnection();
//        }
//    }
//}





















package com.noureddine.WriteFlow.Utils;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRODUCT_ID_1;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRODUCT_ID_2;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.SUB_ID_1;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.SUB_ID_2;

import android.app.Activity;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.QueryPurchasesParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BillingManager {
    // تعريف الثابت TAG لتسجيل الرسائل
    private static final String TAG = "BillingManager";

    private BillingClient billingClient;
    private Activity activity;
    private PurchaseCallback purchaseCallback;
    public String PRODUCT_ID = "";


    /**
     * المُنشئ: يتم هنا تمرير الـ Activity وواجهة رد الاتصال الخاصة بالشراء
     */
    public BillingManager(Activity activity, PurchaseCallback callback) {
        this.activity = activity;
        this.purchaseCallback = callback;
        // بدء إعداد BillingClient عند إنشاء الكائن
        setupBillingClient();
    }

    /**
     * إعداد BillingClient وإنشاء الاتصال بخدمة Google Play
     */
    private void setupBillingClient() {
        Log.d(TAG, "تهيئة BillingClient...");
        // إنشاء كائن BillingClient وتعيين مستمع للمشتريات
        billingClient = BillingClient.newBuilder(activity)
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult,
                                                   @Nullable List<Purchase> purchases) {
                        // التحقق من نجاح عملية الشراء
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                && purchases != null) {
                            Log.d(TAG, "تم تحديث المشتريات بنجاح، عدد المشتريات: " + purchases.size());
                            for (Purchase purchase : purchases) {
                                handlePurchase(purchase);
                            }
                        } else {
                            // في حالة حدوث خطأ أثناء الشراء، تسجيل الخطأ
                            Log.e(TAG, "فشل عملية الشراء: " + billingResult.getDebugMessage());
                            // يمكن استدعاء رد الاتصال لتبليغ المستخدم بالفشل:
                            // purchaseCallback.onPurchaseResult(false, "فشل: " + billingResult.getDebugMessage());
                        }
                    }
                })
                .enablePendingPurchases()
                .build();

        // بدء الاتصال بخدمة Google Play
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "تم الاتصال بخدمة Google Play بنجاح");
                    // بعد الاتصال، نقوم بالاستعلام عن المشتريات الحالية لاستعادة الحالة
                    queryExistingPurchases();
                    queryExistingPurchasesSub();
                } else {
                    // تسجيل رسالة الخطأ إذا فشل الاتصال
                    Log.e(TAG, "فشل إعداد خدمة Google Play: " + billingResult.getDebugMessage());
                    // purchaseCallback.onPurchaseResult(false, "فشل إعداد الفوترة: " + billingResult.getDebugMessage());
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // تسجيل رسالة الخطأ عند انقطاع الاتصال
                Log.e(TAG, "تم قطع الاتصال بخدمة Google Play");
                // هنا يمكن محاولة إعادة الاتصال أو إعلام المستخدم
                // purchaseCallback.onPurchaseResult(false, "تم قطع الاتصال بخدمة الفوترة");
            }
        });
    }

    /**
     * الاستعلام عن المشتريات الحالية لاستعادة حالة التطبيق (مثلاً عند استئناف الاستخدام)
     */
    private void queryExistingPurchases() {
        Log.d(TAG, "استعلام عن المشتريات الحالية...");
        QueryPurchasesParams queryParams = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build();

        billingClient.queryPurchasesAsync(queryParams, new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> purchases) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "تم استعلام المشتريات بنجاح، عدد المشتريات: " + purchases.size());
                    for (Purchase purchase : purchases) {
                        // معالجة المشتريات التي لم يتم تأكيدها بعد
                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                            handlePurchase(purchase);
                        }
                    }
                } else {
                    Log.e(TAG, "فشل استعلام المشتريات: " + billingResult.getDebugMessage());
                }
            }
        });

    }


    private void queryExistingPurchasesSub() {
        QueryPurchasesParams params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build();

        billingClient.queryPurchasesAsync(params, new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> purchases) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "تم استلام المشتريات لنوع " + BillingClient.ProductType.SUBS + " العدد: " + purchases.size());

                    for (Purchase purchase : purchases) {
                        // اختيار المشتريات النشطة فقط
                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                            handlePurchase(purchase);

                            // إرسال حالة الاشتراك إلى واجهة الاستجابة

                            for (String productId : purchase.getProducts()) {
                                Log.d(TAG, "onQueryPurchasesResponse: status "+true+" productId "+productId+" end sub in  "+purchase.getPurchaseTime() + 30L * 24 * 60 * 60 * 1000);
//                                billingCallback.onSubscriptionStatus(
//                                        true,
//                                        productId,
//                                        purchase.getPurchaseTime() + 30L * 24 * 60 * 60 * 1000 // تقدير 30 يوم
//                                );
                            }

                        }
                    }
                } else {
                    Log.e(TAG, "فشل استعلام المشتريات: " + billingResult.getDebugMessage());
                }
            }
        });
    }



    /**
     * بدء عملية الشراء لمنتج معين
     *
     * @param productId معرف المنتج المراد شراؤه
     */
//    public void launchPurchaseFlow(String productId) {
//        Log.d(TAG, "بدء عملية الشراء للمنتج: " + productId);
//        // إنشاء قائمة بالمنتجات التي سيتم استعلام تفاصيلها
//        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
//        if (productId.equals(PRODUCT_ID_1)||productId.equals(PRODUCT_ID_2)){
//            productList.add(
//                    QueryProductDetailsParams.Product.newBuilder()
//                            .setProductId(productId)
//                            .setProductType(BillingClient.ProductType.INAPP)
//                            .build()
//            );
//        }
//        else if (productId.equals(SUB_ID_1)||productId.equals(SUB_ID_2)){
//            productList.add(
//                    QueryProductDetailsParams.Product.newBuilder()
//                            .setProductId(productId)
//                            .setProductType(BillingClient.ProductType.SUBS)
//                            .build()
//            );
//        }
//
//        // إنشاء معلمات استعلام تفاصيل المنتج
//        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
//                .setProductList(productList)
//                .build();
//
//        // استعلام تفاصيل المنتج بشكل غير متزامن
//        billingClient.queryProductDetailsAsync(params, new ProductDetailsResponseListener() {
//            @Override
//            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> productDetailsList) {
//                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
//                        && !productDetailsList.isEmpty()) {
//                    Log.d(TAG, "تم الحصول على تفاصيل المنتج بنجاح");
//                    ProductDetails productDetails = productDetailsList.get(0);
//
//                    // تحضير معلمات العملية الشرائية
//                    List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList = Collections.singletonList(
//                            BillingFlowParams.ProductDetailsParams.newBuilder()
//                                    .setProductDetails(productDetails)
//                                    .build()
//                    );
//
//                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
//                            .setProductDetailsParamsList(productDetailsParamsList)
//                            .build();
//
//                    // إطلاق عملية الشراء
//                    BillingResult result = billingClient.launchBillingFlow(activity, flowParams);
//
//                    if (result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
//                        Log.e(TAG, "فشل إطلاق عملية الشراء: " + result.getDebugMessage());
//                        // purchaseCallback.onPurchaseResult(false, "فشل إطلاق عملية الشراء: " + result.getDebugMessage());
//                    }
//                } else {
//                    Log.e(TAG, "فشل الحصول على تفاصيل المنتج: " + billingResult.getDebugMessage());
//                    // purchaseCallback.onPurchaseResult(false, "فشل الحصول على تفاصيل المنتج: " + billingResult.getDebugMessage());
//                }
//            }
//        });
//    }

    /**
     * بدء عملية الشراء لمنتج معين
     *
     * @param productId معرف المنتج المراد شراؤه
     */
    public void launchPurchaseFlow(String productId) {
        Log.d(TAG, "بدء عملية الشراء للمنتج: " + productId);

        PRODUCT_ID = productId;

        // تحديد نوع المنتج (اشتراك أم منتج عادي)
        String productType;
        if (productId.equals(SUB_ID_1) || productId.equals(SUB_ID_2)) {
            productType = BillingClient.ProductType.SUBS;
        } else {
            productType = BillingClient.ProductType.INAPP;
        }

        // إنشاء قائمة بالمنتجات التي سيتم استعلام تفاصيلها
        List<QueryProductDetailsParams.Product> productList = Collections.singletonList(
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(productType)
                        .build()
        );

        // إنشاء معلمات استعلام تفاصيل المنتج
        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        // استعلام تفاصيل المنتج بشكل غير متزامن
        billingClient.queryProductDetailsAsync(params, new ProductDetailsResponseListener() {
            @Override
            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> productDetailsList) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && !productDetailsList.isEmpty()) {
                    Log.d(TAG, "تم الحصول على تفاصيل المنتج بنجاح");
                    ProductDetails productDetails = productDetailsList.get(0);

                    // تحضير معلمات العملية الشرائية
                    List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList;

                    // التعامل مع المنتج حسب نوعه
                    if (productType.equals(BillingClient.ProductType.SUBS)) {
                        // للاشتراكات: يجب تحديد OfferToken
                        List<ProductDetails.SubscriptionOfferDetails> offerDetailsList =
                                productDetails.getSubscriptionOfferDetails();

                        if (offerDetailsList == null || offerDetailsList.isEmpty()) {
                            Log.e(TAG, "لا توجد عروض اشتراك متاحة للمنتج: " + productId);
                            purchaseCallback.onPurchaseResult(false, "لا توجد عروض اشتراك متاحة");
                            return;
                        }

                        // اختيار أول عرض متاح (يمكن تعديل المنطق لاختيار عرض محدد)
                        String offerToken = offerDetailsList.get(0).getOfferToken();

                        productDetailsParamsList = Collections.singletonList(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(productDetails)
                                        .setOfferToken(offerToken)
                                        .build()
                        );

                        Log.d(TAG, "تم اختيار عرض اشتراك بـ OfferToken: " + offerToken);
                    } else {
                        // منتجات الدفع لمرة واحدة
                        productDetailsParamsList = Collections.singletonList(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(productDetails)
                                        .build()
                        );
                    }

                    // إنشاء معلمات تدفق الفوترة
                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                            .setProductDetailsParamsList(productDetailsParamsList)
                            .build();

                    // إطلاق عملية الشراء
                    BillingResult result = billingClient.launchBillingFlow(activity, flowParams);

                    if (result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                        Log.e(TAG, "فشل إطلاق عملية الشراء: " + result.getDebugMessage());
                        purchaseCallback.onPurchaseResult(false, "فشل إطلاق عملية الشراء: " + result.getDebugMessage());
                    }
                } else {
                    Log.e(TAG, "فشل الحصول على تفاصيل المنتج: " + billingResult.getDebugMessage());
                    purchaseCallback.onPurchaseResult(false, "فشل الحصول على تفاصيل المنتج: " + billingResult.getDebugMessage());
                }
            }
        });
    }



//    public void purchase(String productId) {
//
//        // التحقق من وجود تفاصيل المنتج في الذاكرة
//        ProductDetails productDetails = productDetailsMap.get(productId);
//
//        if (productDetails == null) {
//            Log.e(TAG, "لا يمكن بدء عملية الشراء: تفاصيل المنتج غير متوفرة");
//            billingCallback.onPurchaseFailed(BillingClient.BillingResponseCode.ITEM_UNAVAILABLE,
//                    "تفاصيل المنتج غير متوفرة، الرجاء الاستعلام عن تفاصيل المنتج أولاً");
//            return;
//        }
//
//        Log.d(TAG, "بدء عملية شراء المنتج: " + productId);
//
//        List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList;
//
//        // تجهيز معلمات الشراء بناءً على نوع المنتج
//        if (productDetails.getProductType().equals(BillingClient.ProductType.SUBS)) {
//            // للاشتراكات، نحتاج لتحديد فترة الاشتراك المطلوبة
//            ProductDetails.SubscriptionOfferDetails offerDetails = productDetails.getSubscriptionOfferDetails().get(0);
//
//            productDetailsParamsList = Collections.singletonList(
//                    BillingFlowParams.ProductDetailsParams.newBuilder()
//                            .setProductDetails(productDetails)
//                            .setOfferToken(offerDetails.getOfferToken())
//                            .build()
//            );
//        } else {
//            // منتجات الدفع لمرة واحدة
//            productDetailsParamsList = Collections.singletonList(
//                    BillingFlowParams.ProductDetailsParams.newBuilder()
//                            .setProductDetails(productDetails)
//                            .build()
//            );
//        }
//
//        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
//                .setProductDetailsParamsList(productDetailsParamsList)
//                .build();
//
//        BillingResult result = billingClient.launchBillingFlow(activity, flowParams);
//
//        if (result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
//            Log.e(TAG, "فشل إطلاق عملية الشراء: " + result.getDebugMessage());
//            billingCallback.onPurchaseFailed(result.getResponseCode(), result.getDebugMessage());
//        }
//    }

    /**
     * معالجة عملية الشراء المكتملة
     *
     * @param purchase كائن الشراء الذي يتم معالجته
     */
    private void handlePurchase(Purchase purchase) {
        Log.d(TAG, "معالجة عملية الشراء...");
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // التحقق مما إذا كانت عملية الشراء بحاجة إلى تأكيد
            if (!purchase.isAcknowledged()) {
                Log.d(TAG, "الشراء بحاجة للتأكيد، جاري تأكيد الشراء...");
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

                billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        boolean success = billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK;

                        if (success) {
                            Log.d(TAG, "تم تأكيد عملية الشراء بنجاح");
                            purchaseCallback.onPurchaseResult(true, PRODUCT_ID);
                            Log.d(TAG, purchase.getPackageName()+" "+purchase.getProducts()+" "+purchase.getOrderId());
                        } else {
                            Log.e(TAG, "فشل تأكيد عملية الشراء: " + billingResult.getDebugMessage());
                            // purchaseCallback.onPurchaseResult(false, "فشل تأكيد عملية الشراء: " + billingResult.getDebugMessage());
                        }
                    }
                });
            } else {
                Log.d(TAG, "المنتج قد تم شراؤه وتأكيده مسبقاً");
                purchaseCallback.onPurchaseResult(true, "تم شراء المنتج مسبقاً وتأكيده مسبقاً");
            }
        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
            Log.d(TAG, "عملية الشراء معلقة، يرجى استكمال عملية التحقق من وسيلة الدفع");
            purchaseCallback.onPurchaseResult(false, "عملية الشراء معلقة. يرجى استكمال التحقق من وسيلة الدفع.");
        } else {
            Log.e(TAG, "عملية الشراء لم تكتمل. الحالة: " + purchase.getPurchaseState());
            purchaseCallback.onPurchaseResult(false, "عملية الشراء لم تكتمل. الحالة: " + purchase.getPurchaseState());
        }
    }

    /**
     * واجهة رد الاتصال لإعلام حالة نتيجة عملية الشراء
     */
    public interface PurchaseCallback {
        void onPurchaseResult(boolean success, String message);
    }
}
