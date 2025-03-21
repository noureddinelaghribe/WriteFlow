package com.noureddine.WriteFlow.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.DialogLoading;
import com.noureddine.WriteFlow.activities.AuthActivity;


public class ResetPasswordFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FirebaseAuth auth;
    EditText email ;
    TextView login ;
    Button sendLink ;
    DialogLoading dialogLoading;

    public ResetPasswordFragment() {}

    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_reset_password,container,false);

        email = v.findViewById(R.id.editTextText);
        login = v.findViewById(R.id.textView17);
        sendLink = v.findViewById(R.id.button);
        
        auth = FirebaseAuth.getInstance();
        dialogLoading = new DialogLoading(getContext());
        dialogLoading.loadingProgressDialog("Processing...");

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendPasswordReset();
                
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity) getActivity()).navigateToFragment(new LoginFragment());
            }
        });


        return v;

    }





    private void sendPasswordReset() {
        String sEmail = email.getText().toString().trim();

        if (sEmail.isEmpty()) {
            Toast.makeText(getContext(), "Please enter Email", Toast.LENGTH_LONG).show();
            return;
        }

        dialogLoading.showLoadingProgressDialog();

        FirebaseAuth.getInstance().sendPasswordResetEmail(sEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialogLoading.dismissLoadingProgressDialog();
                        if (task.isSuccessful()) {
                            ((AuthActivity) getActivity()).navigateToFragment(new LoginFragment());
                            Toast.makeText(getContext(), "A password reset link has been sent to your email. Please check your inbox.", Toast.LENGTH_SHORT).show();
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(getContext(), "Email not found. Please check your email address.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to send reset link. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }





}