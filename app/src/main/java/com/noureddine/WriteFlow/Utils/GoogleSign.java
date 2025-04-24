package com.noureddine.WriteFlow.Utils;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.activities.AuthActivity;
import com.noureddine.WriteFlow.fragments.LoginFragment;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.repositorys.FirebaseRepository;

public class GoogleSign {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private Activity activity;
    private Fragment fragment;
    private FirebaseAuth auth;
    private EncryptedPrefsManager prefs;
    private FirebaseRepository firebaseRepository;

    // Constructor for Activity
    public GoogleSign(Activity activity, FirebaseAuth auth, EncryptedPrefsManager prefs) {
        this.activity = activity;
        this.auth = auth;
        this.prefs = prefs;
        firebaseRepository = new FirebaseRepository(activity);
        initGoogleSignIn();
    }

    // Constructor for Fragment
    public GoogleSign(Fragment fragment, FirebaseAuth auth, EncryptedPrefsManager prefs) {
        this.fragment = fragment;
        this.activity = fragment.requireActivity();
        this.auth = auth;
        this.prefs = prefs;
        firebaseRepository = new FirebaseRepository(activity);
        initGoogleSignIn();
    }

    private void initGoogleSignIn() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        if (fragment != null) {
            fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            activity.startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                return true;
            } catch (ApiException e) {
                // Google Sign In failed
                Log.w("GoogleSignIn", "Google sign in failed with code: " + e.getStatusCode(), e);
                Toast.makeText(activity, "Google Sign In Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = auth.getCurrentUser();

                            // Check if this is a new user or existing user login
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNewUser) {
                                // This is a new account creation
                                prefs.saveUser(new User(user.getDisplayName(), user.getEmail(), user.getUid(),FREE_PLAN_NAME, 0,0,0));
                                firebaseRepository.saveUser(new User(user.getDisplayName(),user.getEmail(),user.getUid(),FREE_PLAN_NAME,0,0,0));
                                Toast.makeText(activity, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            } else {
                                // This is an existing user login
                                Toast.makeText(activity, "Welcome back " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            }

                            ((AuthActivity) activity).toHome();
                            activity.finish();
                        } else {
                            // Sign in failed
                            Log.w("GoogleSignIn", "signInWithCredential:failure", task.getException());
                            Toast.makeText(activity, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}