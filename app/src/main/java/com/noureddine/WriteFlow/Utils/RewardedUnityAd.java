package com.noureddine.WriteFlow.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class RewardedUnityAd {
    private String rewardedAdUnitId = "Rewarded_Android";
    private Context context;

    public RewardedUnityAd(Context context) {
        this.context = context;
    }

    public void loadRewardedAd() {
        UnityAds.load(rewardedAdUnitId, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                // Rewarded ad loaded
                Log.d("UnityAds", "Rewarded ad loaded: " + placementId);
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                // Rewarded ad failed to load
                Log.e("UnityAds", "Rewarded ad load failed: " + error + " - " + message);
            }
        });
    }

    public void showRewardedAd() {
        UnityAds.show((Activity) context, rewardedAdUnitId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                // Rewarded ad show failed
                Log.e("UnityAds", "Rewarded ad show failed: " + error + " - " + message);
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {
                // Rewarded ad show started
                Log.d("UnityAds", "Rewarded ad show started");
            }

            @Override
            public void onUnityAdsShowClick(String placementId) {
                // Rewarded ad was clicked
                Log.d("UnityAds", "Rewarded ad clicked");
            }

            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                // Reward user here if state is COMPLETED
                if (state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
                    // Reward the user
                    Log.d("UnityAds", "Rewarded ad completed - grant reward");
                } else {
                    Log.d("UnityAds", "Rewarded ad not completed");
                }
            }
        });
    }
}
