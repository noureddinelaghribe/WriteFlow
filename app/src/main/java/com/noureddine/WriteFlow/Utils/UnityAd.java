//package com.noureddine.WriteFlow.Utils;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Handler;
//import android.util.Log;
//
//import com.noureddine.WriteFlow.model.HistoryArticle;
//import com.unity3d.ads.IUnityAdsLoadListener;
//import com.unity3d.ads.IUnityAdsShowListener;
//import com.unity3d.ads.UnityAds;
//import com.unity3d.ads.UnityAdsShowOptions;
//
//public class UnityAd {
//    private String rewardedAdUnitId = "Rewarded_Android";
//    private String interstitialAdUnitId = "Interstitial_Android";
//    private Context context;
//    onUnityAdsShowComplete onUnityAdsShowComplete;
//
//    public UnityAd( Context context, onUnityAdsShowComplete onUnityAdsShowComplete) {
//        this.context = context;
//        this.onUnityAdsShowComplete = onUnityAdsShowComplete;
//    }
//
////    public void loadRewardedAd() {
////        UnityAds.load(rewardedAdUnitId, new IUnityAdsLoadListener() {
////            @Override
////            public void onUnityAdsAdLoaded(String placementId) {
////                // Rewarded ad loaded
////                Log.d("UnityAds", "Rewarded ad loaded: " + placementId);
////            }
////
////            @Override
////            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
////                // Rewarded ad failed to load
////                Log.e("UnityAds", "Rewarded ad load failed: " + error + " - " + message);
////                // Retry loading after a delay
////                new Handler().postDelayed(() -> loadRewardedAd(), 5000);
////            }
////        });
////    }
////
////    public void showRewardedAd() {
////        UnityAds.show((Activity) context, rewardedAdUnitId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
////            @Override
////            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
////                // Rewarded ad show failed
////                Log.e("UnityAds", "Rewarded ad show failed: " + error + " - " + message);
////                onUnityAdsShowComplete.onRewardedAdComplete(false);
////            }
////
////            @Override
////            public void onUnityAdsShowStart(String placementId) {
////                // Rewarded ad show started
////                Log.d("UnityAds", "Rewarded ad show started");
////            }
////
////            @Override
////            public void onUnityAdsShowClick(String placementId) {
////                // Rewarded ad was clicked
////                Log.d("UnityAds", "Rewarded ad clicked");
////            }
////
////            @Override
////            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
////                // Reward user here if state is COMPLETED
////                if (state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
////                    // Reward the user
////                    Log.d("UnityAds", "Rewarded ad completed - grant reward");
////                    onUnityAdsShowComplete.onRewardedAdComplete(true);
////                } else {
////                    Log.d("UnityAds", "Rewarded ad not completed");
////                    onUnityAdsShowComplete.onRewardedAdComplete(false);
////                }
////            }
////        });
////    }
////
//
//    // INTERSTITIAL ADS METHODS
//
//    public void loadInterstitialAd() {
//        Log.d("UnityAds", "Loading interstitial ad: " + interstitialAdUnitId);
//        UnityAds.load(interstitialAdUnitId, new IUnityAdsLoadListener() {
//            @Override
//            public void onUnityAdsAdLoaded(String placementId) {
//                Log.d("UnityAds", "Interstitial ad loaded successfully: " + placementId);
//            }
//
//            @Override
//            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
//                Log.e("UnityAds", "Interstitial ad failed to load: " + error + " - " + message);
//                // Retry loading after a delay
//                new Handler().postDelayed(() -> loadInterstitialAd(), 5000);
//            }
//        });
//    }
//
//    public void showInterstitialAd() {
//
//        Log.d("UnityAds", "Showing interstitial ad");
//        UnityAds.show((Activity) context, interstitialAdUnitId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
//            @Override
//            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
//                Log.e("UnityAds", "Interstitial ad show failed: " + error + " - " + message);
//                loadInterstitialAd(); // Try loading a new ad
//                onUnityAdsShowComplete.onInterstitialAdComplete(false);
//            }
//
//            @Override
//            public void onUnityAdsShowStart(String placementId) {
//                Log.d("UnityAds", "Interstitial ad show started");
//            }
//
//            @Override
//            public void onUnityAdsShowClick(String placementId) {
//                Log.d("UnityAds", "Interstitial ad clicked");
//            }
//
//            @Override
//            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
//                Log.d("UnityAds", "Interstitial ad show completed: " + state.toString());
//                loadInterstitialAd(); // Load the next ad
//                onUnityAdsShowComplete.onInterstitialAdComplete(true);
//            }
//        });
//
//    }
//
//
//    public interface onUnityAdsShowComplete {
//        void onInterstitialAdComplete(boolean complete);
//        //void onRewardedAdComplete(boolean complete);
//    }
//
//}
