package com.noureddine.WriteFlow.fragments;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.DialogLoading;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.Utils.GoogleSign;
import com.noureddine.WriteFlow.activities.AuthActivity;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.repositorys.FirebaseRepository;

public class RegisterFragment extends Fragment {

    private EditText name, email, password;
    private TextView login, privacyPolicy ;
    private Button register ;
    private LinearLayout google ;
    private FirebaseAuth auth;
    private EncryptedPrefsManager prefs;
    private FirebaseRepository firebaseRepository;
    private DialogLoading dialogLoading;
    private GoogleSign googleSignHelper;


    public RegisterFragment() {}

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register,container,false);

        name = v.findViewById(R.id.editTextText1);
        email = v.findViewById(R.id.editTextText2);
        password = v.findViewById(R.id.editTextText3);
        login = v.findViewById(R.id.textView17);
        register = v.findViewById(R.id.button);
        google = v.findViewById(R.id.linearLayout);
        privacyPolicy = v.findViewById(R.id.textView19);

        auth = FirebaseAuth.getInstance();
        prefs = EncryptedPrefsManager.getInstance(getContext());
        firebaseRepository = new FirebaseRepository(getContext());
        dialogLoading = new DialogLoading(getContext());
        dialogLoading.loadingProgressDialog("Loading...");
        googleSignHelper = new GoogleSign(this, auth, prefs);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerNewUser();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((AuthActivity) getActivity()).navigateToFragment(new LoginFragment());

            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                googleSignHelper.signIn();

            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://bit.ly/4hW3sa3";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        return v;

    }


    void registerNewUser() {
        // Get values from input fields
        String sName = name.getText().toString().trim();
        String sEmail = email.getText().toString().trim();
        String sPassword = password.getText().toString().trim();

        // Validate input
        if (sName.isEmpty() || sEmail.isEmpty() || sPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please enter credentials", Toast.LENGTH_LONG).show();
            return;
        }

        dialogLoading.showLoadingProgressDialog();

        // Register new user with Firebase
        auth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialogLoading.dismissLoadingProgressDialog();
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                            prefs.saveUser(new User(sName,sEmail,auth.getUid(),FREE_PLAN_NAME,0,0,0));
                            firebaseRepository.saveUser(new User(sName,sEmail,auth.getUid(),FREE_PLAN_NAME,0,0,0));
                            //((AuthActivity) getActivity()).toHome();
                            //getActivity().finish();

                        } else {
                            // Registration failed
                            Toast.makeText(getContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Let the helper handle the result
        googleSignHelper.handleActivityResult(requestCode, resultCode, data);
    }



}