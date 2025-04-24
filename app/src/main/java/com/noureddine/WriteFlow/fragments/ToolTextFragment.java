package com.noureddine.WriteFlow.fragments;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;
import static com.unity3d.services.core.properties.ClientProperties.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.CopySaveResult;
import com.noureddine.WriteFlow.Utils.DialogLoading;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.Utils.TextProcessing;
import com.noureddine.WriteFlow.activities.HomeActivity;
import com.noureddine.WriteFlow.activities.TextToolActivity;
import com.noureddine.WriteFlow.model.HistoryArticle;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.viewModels.HistoryArticleViewModel;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class ToolTextFragment extends Fragment implements IUnityAdsInitializationListener{

    private String toolTitle;
    private TextView textView;
    private EditText input,output,opirator1,opirator2;
    private Button button;
    private ImageView back,copy,txt,word,pdf,html;
    private LinearLayout linearLayout;
    private Spinner spinner;
    private HistoryArticleViewModel historyArticleViewModel;
    private EncryptedPrefsManager prefs;
    private DialogLoading dialogLoading;

    private String interstitialAdUnitId = "Interstitial_Android";
    private String GAME_ID = "5817517"; // Replace with your actual Game ID
    private boolean TEST_MODE = false;

    private CopySaveResult copySaveResult;
    private User user;

    private Bundle bundle;
    private HistoryArticle historyArticle = new HistoryArticle();

    private String []models = {"  -- By --  ","  Length  ","  Alphabet  "};

    // Activity result launcher for regular storage permission
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(requireContext(), "Regular storage permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Regular storage permission denied", Toast.LENGTH_SHORT).show();
                }
            });


    public ToolTextFragment() {}

    public static ToolTextFragment newInstance() {
        ToolTextFragment fragment = new ToolTextFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            toolTitle = getArguments().getString("textTool");
//        }

        bundle = getArguments();
        if (bundle != null){
            if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                historyArticle = (HistoryArticle) bundle.get("HistoryArticle");
                toolTitle = historyArticle.getType();
            }else {
                toolTitle = bundle.getString("textTool");
            }
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tool_text,container,false);

        textView = v.findViewById(R.id.textView4);
        input = v.findViewById(R.id.editTextText7);
        output = v.findViewById(R.id.editTextText5);
        opirator1 = v.findViewById(R.id.editTextText6);
        opirator2 = v.findViewById(R.id.editTextText8);
        button = v.findViewById(R.id.button4);
        back = v.findViewById(R.id.imageView11);
        copy = v.findViewById(R.id.imageView1);
        txt = v.findViewById(R.id.imageView2);
        word = v.findViewById(R.id.imageView3);
        pdf = v.findViewById(R.id.imageView4);
        html = v.findViewById(R.id.imageView5);
        linearLayout = v.findViewById(R.id.linearLayoutSaveRuselt);
        spinner = v.findViewById(R.id.spinner3);

        historyArticleViewModel = new HistoryArticleViewModel(getActivity().getApplication());
        prefs = EncryptedPrefsManager.getInstance(getContext());
        user = prefs.getUser();
        linearLayout.setVisibility(View.GONE);
        output.setVisibility(View.GONE);
        textView.setText(toolTitle);
        SetupTool();

        if (user.getMembership().equals(FREE_PLAN_NAME)){
            UnityAds.initialize(getApplicationContext(), GAME_ID, TEST_MODE, this);
            loadInterstitialAd();
        }

        dialogLoading = new DialogLoading(getContext());
        dialogLoading.loadingProgressDialog("Processing...");

        copySaveResult = new CopySaveResult(getActivity(),requestPermissionLauncher);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextToolActivity) getActivity()).navigateToFragment(new ListToolTextFragment());
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogLoading.showLoadingProgressDialog();

                String operatorStr1 = opirator1.getText().toString().trim();
                String operatorStr2 = opirator2.getText().toString().trim();
                String inputStr = input.getText().toString().trim();
                ProcessText(inputStr,operatorStr1,operatorStr2);

            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySaveResult.copyClipboard(output.getText().toString());
            }
        });

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySaveResult.saveAsFile(output.getText().toString(),"txt");
            }
        });

        word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySaveResult.saveAsFile(output.getText().toString(),"word");
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySaveResult.saveAsFile(output.getText().toString(),"pdf");
            }
        });

        html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySaveResult.saveAsFile(output.getText().toString(),"html");
            }
        });


        return v;
    }

    public void SetupTool(){
        switch (toolTitle) {
            case "Reverse Text":
            case "Remove Duplicate Spaces":
            case "Remove Duplicate Lines":
            case "Unwrap Text":
            case "Remove Punctuation":
            case "Remove Diacritics":
            case "Fix Text Spacing":
                //only text
                opirator1.setVisibility(View.GONE);
                opirator2.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                if (bundle != null){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    input.setText(historyArticle.getArticle());
                    output.setText(historyArticle.getResponse());
                }
                break;
            case "Insert New Line After Symbol/word":
            case "Replace New Line With Symbol/word":
                // text and 1 filter char Symbol
                opirator1.setVisibility(View.VISIBLE);
                opirator1.setHint("Symbol or Word");
                opirator2.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                if (bundle != null){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    opirator1.setText(historyArticle.getOperatorStr1());
                    input.setText(historyArticle.getArticle());
                    output.setText(historyArticle.getResponse());
                }
                break;
            case "Repeat Text":
            case "Truncate Text":
            case "Wrap Text By Words":
            case "Wrap Text By Characters":
                // text and 1 filter int num
                opirator1.setVisibility(View.VISIBLE);
                opirator1.setHint("1");
                opirator2.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                if (bundle != null){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    opirator1.setText(historyArticle.getOperatorStr1());
                    input.setText(historyArticle.getArticle());
                    output.setText(historyArticle.getResponse());
                }
                break;
            case "Filter Text By Word":
                // text and 1 filter string word
                opirator1.setVisibility(View.VISIBLE);
                opirator1.setHint("word");
                opirator2.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                if (bundle != null){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    opirator1.setText(historyArticle.getOperatorStr1());
                    input.setText(historyArticle.getArticle());
                    output.setText(historyArticle.getResponse());
                }
                break;
            case "Sort Text":
                // text and spinner ("length","alphabet")
                opirator1.setVisibility(View.GONE);
                opirator2.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                ArrayAdapter<String> spinnerModeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<>());
                spinnerModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerModeAdapter);
                spinnerModeAdapter.addAll(models);
                spinnerModeAdapter.notifyDataSetChanged();
                if (bundle != null){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    input.setText(historyArticle.getArticle());
                    output.setText(historyArticle.getResponse());
                    spinner.setSelection(historyArticle.getOptionSpinner());
                }
                break;
            case "Find and Replace Text":
                // text and and 2 filter
                opirator1.setVisibility(View.VISIBLE);
                opirator1.setHint("Find word");
                opirator2.setVisibility(View.VISIBLE);
                opirator2.setHint("Replace with");
                spinner.setVisibility(View.GONE);
                if (bundle != null){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    input.setText(historyArticle.getArticle());
                    output.setText(historyArticle.getResponse());
                    opirator1.setText(historyArticle.getOperatorStr1());
                    opirator2.setText(historyArticle.getOperatorStr2());
                    Log.d("TAG", "SetupTool: "+historyArticle.getOperatorStr1()+" "+historyArticle.getOperatorStr2());
                }
                break;
            default:
                // No matching case found
                Toast.makeText(getContext(),"Have same error", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
                break;
        }
    }


    public void ProcessText(String text, String filter1,String filter2){
        switch (toolTitle) {
            case "Reverse Text":
                if (!text.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.reverserText(text));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Remove Duplicate Spaces":
                if (!text.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.removerSpaceDuplicateText(text));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Remove Duplicate Lines":
                if (!text.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.removerLineDuplicateText(text));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Unwrap Text":
                if (!text.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.unWrapText(text));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Remove Punctuation":
                if (!text.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.removePunctuation(text));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Remove Diacritics":
                if (!text.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.removeDiacritics(text));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Fix Text Spacing":
                if (!text.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.distanceFixerText(text));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;

            case "Insert New Line After Symbol/word":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.insertNewLineAfterSymbol(text,filter1));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Replace New Line With Symbol/word":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.replaceSymbolNewLine(text,filter1));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Repeat Text":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    try {
                        if (user.getMembership().equals(FREE_PLAN_NAME)){
                            showInterstitialAd();
                        }else {
                            linearLayout.setVisibility(View.VISIBLE);
                            output.setVisibility(View.VISIBLE);
                        }
                        output.setText(TextProcessing.repeatText(text,Integer.parseInt(filter1)));

                        if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                            historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                    System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                        }else {
                            historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                    System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                        }

                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Truncate Text":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    try {
                        if (user.getMembership().equals(FREE_PLAN_NAME)){
                            showInterstitialAd();
                        }else {
                            linearLayout.setVisibility(View.VISIBLE);
                            output.setVisibility(View.VISIBLE);
                        }
                        output.setText(TextProcessing.truncatorText(text,Integer.parseInt(filter1)));

                        if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                            historyArticleViewModel.updateArticle(new HistoryArticle(historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                    System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                        }else {
                            historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                    System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                        }

                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Wrap Text By Words":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    try {
                        if (user.getMembership().equals(FREE_PLAN_NAME)){
                            showInterstitialAd();
                        }else {
                            linearLayout.setVisibility(View.VISIBLE);
                            output.setVisibility(View.VISIBLE);
                        }
                        output.setText(TextProcessing.wrapTextByWords(text,Integer.parseInt(filter1)));

                        if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                            historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                    System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                        }else {
                            historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                    System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                        }

                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Wrap Text By Characters":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    try {
                        if (user.getMembership().equals(FREE_PLAN_NAME)){
                            showInterstitialAd();
                        }else {
                            linearLayout.setVisibility(View.VISIBLE);
                            output.setVisibility(View.VISIBLE);
                        }
                        output.setText(TextProcessing.wrapTextByCharacters(text,Integer.parseInt(filter1)));

                        if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                            historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                    System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                        }else {
                            historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                    System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                        }

                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Filter Text By Word":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.filterTextByWord(text,filter1));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Sort Text":
                // text and spinner ("length","alphabet")
                if (spinner.getSelectedItemPosition() == 0){
                    Toast.makeText(getContext(), "Please select by.", Toast.LENGTH_SHORT).show();
                }else {
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }

                    if (spinner.getSelectedItemPosition() == 1){
                        output.setText(TextProcessing.sorterTextByLength(text));
                    }else {
                        output.setText(TextProcessing.sorterTextByAlphabet(text));
                    }

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }
                break;
            case "Find and Replace Text":
                // text and and 2 filter
                if (!text.isEmpty() && !filter1.isEmpty() && !filter2.isEmpty()){
                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        showInterstitialAd();
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                    }
                    output.setText(TextProcessing.findandReplaceText(text,filter1,filter2));

                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        historyArticleViewModel.updateArticle(new HistoryArticle( historyArticle.getId(), user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }else {
                        historyArticleViewModel.insertArticle(new HistoryArticle( user.getUid(), output.getText().toString(), text, toolTitle,
                                System.currentTimeMillis(),filter1,filter2,spinner.getSelectedItemPosition()));
                    }

                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                // No matching case found
                Toast.makeText(getContext(),"Have same error", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
                break;
        }

        dialogLoading.dismissLoadingProgressDialog();

    }

    @Override
    public void onInitializationComplete() {
        // Unity Ads initialization complete
        Log.d("UnityAds", "Initialization Complete");
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        // Unity Ads initialization failed
        Log.d("UnityAds", "Initialization Failed: " + error + " - " + message);
    }


    // INTERSTITIAL ADS METHODS

    public void loadInterstitialAd() {
        Log.d("UnityAds", "Loading interstitial ad: " + interstitialAdUnitId);
        UnityAds.load(interstitialAdUnitId, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                Log.d("UnityAds", "Interstitial ad loaded successfully: " + placementId);
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                Log.e("UnityAds", "Interstitial ad failed to load: " + error + " - " + message);
                // Retry loading after a delay
                new Handler().postDelayed(() -> loadInterstitialAd(), 5000);
            }
        });
    }

    public void showInterstitialAd() {

        Log.d("UnityAds", "Showing interstitial ad");
        UnityAds.show(getActivity(), interstitialAdUnitId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                Log.e("UnityAds", "Interstitial ad show failed: " + error + " - " + message);
                dialogLoading.dismissLoadingProgressDialog();
                loadInterstitialAd();
                Toast.makeText(getContext(), "Ad show failed click agin ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {
                Log.d("UnityAds", "Interstitial ad show started");
                linearLayout.setVisibility(View.VISIBLE);
                output.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnityAdsShowClick(String placementId) {
                Log.d("UnityAds", "Interstitial ad clicked");
            }

            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                Log.d("UnityAds", "Interstitial ad show completed: " + state.toString());
                dialogLoading.dismissLoadingProgressDialog();
            }
        });

    }

}