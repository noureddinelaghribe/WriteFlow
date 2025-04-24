package com.noureddine.WriteFlow.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.DialogLoading;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.Utils.GoogleSign;
import com.noureddine.WriteFlow.activities.AuthActivity;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.repositorys.FirebaseRepository;


public class LoginFragment extends Fragment {

    private EditText email, password;
    private TextView forgot, register, privacyPolicy;
    private Button login ;
    private LinearLayout google ;
    private FirebaseAuth auth;
    private EncryptedPrefsManager prefs;
    private DialogLoading dialogLoading;
    private GoogleSign googleSignHelper;


    public LoginFragment() {}

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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

        View v = inflater.inflate(R.layout.fragment_login,container,false);

        email = v.findViewById(R.id.editTextText);
        password = v.findViewById(R.id.editTextText2);
        forgot = v.findViewById(R.id.textView16);
        register = v.findViewById(R.id.textView17);
        login = v.findViewById(R.id.button);
        google = v.findViewById(R.id.linearLayout);
        privacyPolicy = v.findViewById(R.id.textView19);

        auth = FirebaseAuth.getInstance();
        prefs = EncryptedPrefsManager.getInstance(getContext());
        dialogLoading = new DialogLoading(getContext());
        dialogLoading.loadingProgressDialog("Loading...");
        googleSignHelper = new GoogleSign(this, auth, prefs);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity) getActivity()).navigateToFragment(new ResetPasswordFragment());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity) getActivity()).navigateToFragment(new RegisterFragment());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUserAccount();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Let the helper handle the result
        googleSignHelper.handleActivityResult(requestCode, resultCode, data);
    }

    private void loginUserAccount() {
        String sEmail = email.getText().toString().trim();
        String sPassword = password.getText().toString().trim();

        // Validate input
        if (sEmail.isEmpty() || sPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please enter credentials", Toast.LENGTH_LONG).show();
            return;
        }

        dialogLoading.showLoadingProgressDialog();

        // Login existing user
        auth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialogLoading.dismissLoadingProgressDialog();
                        if (task.isSuccessful()) {
                            ((AuthActivity) getActivity()).toHome();
                            getActivity().finish();
                        } else {
                            // Login failed
                            Toast.makeText(getContext(), "Login failed!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



}