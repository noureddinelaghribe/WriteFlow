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
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * مدير الاشتراكات والمشتريات في تطبيقات أندرويد
// * يتعامل مع Google Play Billing Library
// */
//public class SubscriptionBillingManager {
//    private static final String TAG = "SubscriptionBillingManager";
//
//    // ثوابت لأنواع المنتجات
//    public static final String PRODUCT_TYPE_SUBS = BillingClient.ProductType.SUBS;    // اشتراكات
//
//    // العميل المسؤول عن الاتصال بخدمة الفوترة
//    private BillingClient billingClient;
//    private Activity activity;
//    private BillingCallback billingCallback;
//
//    // تخزين تفاصيل المنتجات للوصول السريع
//    private Map<String, ProductDetails> productDetailsMap = new HashMap<>();
//
//    // حالة الاتصال بخدمة الفوترة
//    private boolean isServiceConnected = false;
//
//    // واجهة الاستجابة لعمليات الشراء والاشتراك
//
//    public interface BillingCallback {
//        void onBillingInitialized(boolean success);
//        void onPurchaseCompleted(Purchase purchase);
//        void onPurchaseFailed(int errorCode, String errorMessage);
//        void onProductDetailsReceived(List<ProductDetails> productDetails);
//        void onSubscriptionStatus(boolean isActive, String subscriptionId, long expiryTimeMillis);
//    }
//
//    /**
//     * المُنشئ: يتطلب Activity وكائن BillingCallback
//     */
//    public SubscriptionBillingManager(Activity activity, BillingCallback callback) {
//        this.activity = activity;
//        this.billingCallback = callback;
//        setupBillingClient();
//    }
//
//    /**
//     * إعداد عميل الفوترة والاتصال بخدمة Google Play
//     */
//    private void setupBillingClient() {
//        Log.d(TAG, "تهيئة BillingClient...");
//
//        billingClient = BillingClient.newBuilder(activity)
//                .setListener(purchasesUpdatedListener)
//                .enablePendingPurchases()
//                .build();
//
//        connectToPlayBillingService();
//    }
//
//    /**
//     * مستمع تحديث المشتريات - يستدعى عند تحديث حالة أي شراء أو عند إكمال عملية شراء
//     */
//    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
//        @Override
//        public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
//            int responseCode = billingResult.getResponseCode();
//
//            if (responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
//                Log.d(TAG, "المشتريات محدثة، العدد: " + purchases.size());
//
//                // معالجة كل عملية شراء
//                for (Purchase purchase : purchases) {
//                    handlePurchase(purchase);
//                }
//            } else if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
//                Log.d(TAG, "المستخدم ألغى عملية الشراء");
//                billingCallback.onPurchaseFailed(responseCode, "تم إلغاء الشراء من قبل المستخدم");
//            } else {
//                Log.e(TAG, "خطأ في عملية الشراء: " + billingResult.getDebugMessage());
//                billingCallback.onPurchaseFailed(responseCode, billingResult.getDebugMessage());
//            }
//        }
//    };
//
//    /**
//     * الاتصال بخدمة Google Play Billing
//     */
//    private void connectToPlayBillingService() {
//        Log.d(TAG, "جاري الاتصال بخدمة Google Play Billing...");
//
//        billingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
//                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                    Log.d(TAG, "تم الاتصال بنجاح بخدمة Billing");
//                    isServiceConnected = true;
//                    billingCallback.onBillingInitialized(true);
//
//                    // استعلام عن المشتريات الحالية بعد الاتصال
//                    queryPurchases();
//                } else {
//                    Log.e(TAG, "فشل الاتصال بخدمة Billing: " + billingResult.getDebugMessage());
//                    isServiceConnected = false;
//                    billingCallback.onBillingInitialized(false);
//                }
//            }
//
//            @Override
//            public void onBillingServiceDisconnected() {
//                Log.d(TAG, "انقطع الاتصال بخدمة Billing");
//                isServiceConnected = false;
//                // إعادة محاولة الاتصال عند انقطاعه
//                connectToPlayBillingService();
//            }
//        });
//    }
//
//    /**
//     * استعلام عن المشتريات الحالية (اشتراكات ومشتريات لمرة واحدة)
//     */
//    public void queryPurchases() {
//        if (!isServiceConnected) {
//            Log.e(TAG, "لا يمكن الاستعلام عن المشتريات: الخدمة غير متصلة");
//            return;
//        }
//
//        Log.d(TAG, "الاستعلام عن جميع المشتريات...");
//
//        // استعلام عن الاشتراكات
//        queryPurchasesByType(BillingClient.ProductType.SUBS);
//
//        // استعلام عن المشتريات لمرة واحدة
//        queryPurchasesByType(BillingClient.ProductType.INAPP);
//    }
//
//    /**
//     * استعلام عن المشتريات حسب النوع (اشتراك أو لمرة واحدة)
//     */
//    private void queryPurchasesByType(String productType) {
//        QueryPurchasesParams params = QueryPurchasesParams.newBuilder()
//                .setProductType(productType)
//                .build();
//
//        billingClient.queryPurchasesAsync(params, new PurchasesResponseListener() {
//            @Override
//            public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> purchases) {
//                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                    Log.d(TAG, "تم استلام المشتريات لنوع " + productType + " العدد: " + purchases.size());
//
//                    for (Purchase purchase : purchases) {
//                        // اختيار المشتريات النشطة فقط
//                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
//                            handlePurchase(purchase);
//
//                            // إرسال حالة الاشتراك إلى واجهة الاستجابة
//                            if (productType.equals(BillingClient.ProductType.SUBS)) {
//                                for (String productId : purchase.getProducts()) {
//                                    billingCallback.onSubscriptionStatus(
//                                            true,
//                                            productId,
//                                            purchase.getPurchaseTime() + 30L * 24 * 60 * 60 * 1000 // تقدير 30 يوم
//                                    );
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    Log.e(TAG, "فشل استعلام المشتريات: " + billingResult.getDebugMessage());
//                }
//            }
//        });
//    }
//
//    /**
//     * استعلام عن تفاصيل منتج معين (اشتراك أو منتج لمرة واحدة)
//     */
//    public void queryProductDetails(List<String> productIds, String productType) {
//        if (!isServiceConnected) {
//            Log.e(TAG, "لا يمكن الاستعلام: الخدمة غير متصلة");
//            return;
//        }
//
//        Log.d(TAG, "الاستعلام عن تفاصيل المنتجات...");
//
//        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
//
//        // تجهيز قائمة المنتجات للاستعلام
//        for (String productId : productIds) {
//            productList.add(
//                    QueryProductDetailsParams.Product.newBuilder()
//                            .setProductId(productId)
//                            .setProductType(productType)
//                            .build()
//            );
//        }
//
//        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
//                .setProductList(productList)
//                .build();
//
//        billingClient.queryProductDetailsAsync(params, new ProductDetailsResponseListener() {
//            @Override
//            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> productDetailsList) {
//                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                    Log.d(TAG, "تم استلام تفاصيل المنتجات، العدد: " + productDetailsList.size());
//
//                    // تخزين تفاصيل المنتجات للوصول السريع
//                    for (ProductDetails details : productDetailsList) {
//                        productDetailsMap.put(details.getProductId(), details);
//                    }
//
//                    billingCallback.onProductDetailsReceived(productDetailsList);
//                } else {
//                    Log.e(TAG, "فشل جلب تفاصيل المنتجات: " + billingResult.getDebugMessage());
//                    billingCallback.onProductDetailsReceived(Collections.emptyList());
//                }
//            }
//        });
//    }
//
//    /**
//     * بدء عملية الشراء لمنتج محدد (دفع لمرة واحدة أو اشتراك)
//     */
//    public void purchase(String productId) {
//        if (!isServiceConnected) {
//            Log.e(TAG, "لا يمكن بدء عملية الشراء: الخدمة غير متصلة");
//            return;
//        }
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
//
//    /**
//     * معالجة عملية الشراء المكتملة: التأكيد وتحديث الحالة
//     */
//    private void handlePurchase(Purchase purchase) {
//        Log.d(TAG, "معالجة عملية الشراء: " + purchase.getOrderId());
//
//        // التحقق من حالة الشراء
//        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
//            // إخطار واجهة الاستجابة بنجاح الشراء
//            billingCallback.onPurchaseCompleted(purchase);
//
//            // تأكيد عملية الشراء إذا لم يتم تأكيدها بعد
//            if (!purchase.isAcknowledged()) {
//                acknowledgePurchase(purchase.getPurchaseToken());
//            }
//        }
////        else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
////            Log.d(TAG, "عملية الشراء معلقة: " + purchase.getOrderId());
////            billingCallback.onPurchaseFailed(BillingClient.BillingResponseCode.ITEM_PENDING, "الشراء معلق بانتظار إكمال الدفع");
////        }
//        else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
//        Log.d(TAG, "عملية الشراء معلقة: " + purchase.getOrderId());
//        // استخدام رمز استجابة مناسب بدلاً من ITEM_PENDING (غير موجود)
//        billingCallback.onPurchaseFailed(BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
//                "الشراء معلق بانتظار إكمال الدفع");
//        }
//    }
//
//    /**
//     * تأكيد عملية الشراء لتجنب استرداد المال التلقائي
//     */
//    private void acknowledgePurchase(String purchaseToken) {
//        Log.d(TAG, "تأكيد عملية الشراء: " + purchaseToken);
//
//        AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder()
//                .setPurchaseToken(purchaseToken)
//                .build();
//
//        billingClient.acknowledgePurchase(params, new AcknowledgePurchaseResponseListener() {
//            @Override
//            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
//                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                    Log.d(TAG, "تم تأكيد عملية الشراء بنجاح");
//                } else {
//                    Log.e(TAG, "فشل تأكيد عملية الشراء: " + billingResult.getDebugMessage());
//                }
//            }
//        });
//    }
//
//    /**
//     * التحقق مما إذا كان الاشتراك نشطاً
//     */
//    public void checkSubscriptionStatus(String subscriptionId) {
//        if (!isServiceConnected) {
//            Log.e(TAG, "لا يمكن التحقق من حالة الاشتراك: الخدمة غير متصلة");
//            return;
//        }
//
//        Log.d(TAG, "التحقق من حالة الاشتراك: " + subscriptionId);
//
//        QueryPurchasesParams params = QueryPurchasesParams.newBuilder()
//                .setProductType(BillingClient.ProductType.SUBS)
//                .build();
//
//        billingClient.queryPurchasesAsync(params, new PurchasesResponseListener() {
//            @Override
//            public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> purchases) {
//                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                    boolean isActive = false;
//                    long expiryTimeMillis = 0;
//
//                    for (Purchase purchase : purchases) {
//                        if (purchase.getProducts().contains(subscriptionId) &&
//                                purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
//                            isActive = true;
//                            // في واقع الأمر، يجب استخراج تاريخ انتهاء الصلاحية من تفاصيل المنتج
//                            // هنا نستخدم تقديراً فقط
//                            expiryTimeMillis = purchase.getPurchaseTime() + 30L * 24 * 60 * 60 * 1000; // 30 يوم
//                            break;
//                        }
//                    }
//
//                    billingCallback.onSubscriptionStatus(isActive, subscriptionId, expiryTimeMillis);
//                } else {
//                    Log.e(TAG, "فشل الاستعلام عن حالة الاشتراك: " + billingResult.getDebugMessage());
//                    billingCallback.onSubscriptionStatus(false, subscriptionId, 0);
//                }
//            }
//        });
//    }
//
//    /**
//     * إغلاق الاتصال بخدمة الفوترة عند الانتهاء
//     */
//    public void endConnection() {
//        if (billingClient != null && billingClient.isReady()) {
//            Log.d(TAG, "إغلاق الاتصال بخدمة الفوترة");
//            billingClient.endConnection();
//            isServiceConnected = false;
//        }
//    }
//}