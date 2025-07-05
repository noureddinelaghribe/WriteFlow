

package com.noureddine.WriteFlow.Utils;

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
