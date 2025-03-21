package com.noureddine.WriteFlow.fragments;



import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_PROCESS_LIMIT;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneAIDetector;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneGrammarChecker;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneParagraphGenerator;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneParaphraserRewriting;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneSummarizer;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneAIDetector;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneGrammarChecker;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneParagraphGenerator;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneParaphraserRewriting;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneSummarizer;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneAIDetector;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneGrammarChecker;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneParagraphGenerator;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneParaphraserRewriting;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneSummarizer;
import static com.noureddine.WriteFlow.Utils.TextProcessing.countWords;
import static com.unity3d.services.core.properties.ClientProperties.getApplicationContext;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.noureddine.WriteFlow.Utils.DialogLoading;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.Utils.RewardedUnityAd;
import com.noureddine.WriteFlow.activities.ProcessingWordActivity;
import com.noureddine.WriteFlow.model.GrammarChecker;
import com.noureddine.WriteFlow.model.HistoryArticle;
import com.noureddine.WriteFlow.model.TypeProcessing;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.repositorys.FirebaseRepository;
import com.noureddine.WriteFlow.viewModels.ChatViewModel;
import com.noureddine.WriteFlow.viewModels.HistoryArticleViewModel;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class ProcessingWordsFragment extends Fragment implements IUnityAdsInitializationListener{

//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private static final Logger log = LogManager.getLogger(ProcessingWordsFragment.class);
//
//    private String mParam1;
//    private String mParam2;

    private final String GAME_ID = "5817517";
    private final boolean TEST_MODE = false; // Disable for production

    RewardedUnityAd rewardedUnityAd;

    String []languages = {"  --  Languages  --  ","  English (US)  ","  French  ","  Spanish  ","  German  ","  Arabic  ","  Africans  ","  Chinese  ","  Hindi  ",
            "  Romanian  ","  Russian  ", "  Danish  ","  Indonesian  ","  Dutch  ","  Italian  ","  Swedish  ","  English (AU)  ","  Japanese  ","  Malay  ",
            "  Tagalog  ","  English (CA)  ","  Turkish  ", "  English (UK)  ","  Norwegian  ","  Ukrainian  ","  Polish  ","  Vietnamese  ","  Portuguese  "};

    String []models = {"  -- Modes --  ","  Standard  ","  Fluency  ","  Humanize  ","  Formal  ","  Academic  ","  Simple  ","  Creative  ","  Expand  "};

    int wordLimit = 0;
    TextView typeTextView ,moreLess ,countWord ,limit,aiPercentage,humanPercentage,grammarPercentage;
    ImageView img ,back,copy,txt,word,pdf,html;
    Spinner spinnerLqnguqge ,spinnerMode;
    LinearLayout linearLayoutMore ;
    EditText keyword ,text;
    Button button ;
    CardView cardViewfilters,cardViewAiGenirator,cardViewCopyResult,cardViewGrammerError;
    DialogLoading dialogLoading;
    ChatViewModel viewModel;
    String type;
    boolean isExpanded = false;
    ArrayAdapter<String> spinnerModeAdapter;
    EncryptedPrefsManager prefs;
    User user ;
    FirebaseRepository firebaseRepository;
    HistoryArticleViewModel historyArticleViewModel;
    HistoryArticle historyArticle = new HistoryArticle();
    Bundle bundle;


    // Activity result launcher for regular storage permission
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(requireContext(), "Regular storage permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Regular storage permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    public ProcessingWordsFragment() {}

    public static ProcessingWordsFragment newInstance(/*String param1, String param2*/) {
        ProcessingWordsFragment fragment = new ProcessingWordsFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        // Initialize Unity Ads
        UnityAds.initialize(getApplicationContext(), GAME_ID, TEST_MODE, this);

        bundle = getArguments();
        if (bundle != null){
            if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                historyArticle = (HistoryArticle) bundle.get("HistoryArticle");
                type = historyArticle.getType();
            }else {
                type = bundle.getString("type");
            }
        }

    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_processing_words,container,false);

        typeTextView = v.findViewById(R.id.textView4);
        aiPercentage = v.findViewById(R.id.textView23);
        humanPercentage = v.findViewById(R.id.textView25);
        grammarPercentage = v.findViewById(R.id.textView26);
        img = v.findViewById(R.id.imageView4);
        back = v.findViewById(R.id.imageView6);
        copy = v.findViewById(R.id.imageViewCopy);
        txt = v.findViewById(R.id.imageViewTxt);
        word = v.findViewById(R.id.imageViewWord);
        pdf = v.findViewById(R.id.imageViewPdf);
        html = v.findViewById(R.id.imageViewHtml);
        spinnerLqnguqge = v.findViewById(R.id.spinner);
        linearLayoutMore = v.findViewById(R.id.linearLayoutMore);
        spinnerMode = v.findViewById(R.id.spinner2);
        keyword = v.findViewById(R.id.editTextText4);
        moreLess = v.findViewById(R.id.textView19);
        text = v.findViewById(R.id.editTextText6);
        countWord = v.findViewById(R.id.textView20);
        limit = v.findViewById(R.id.textView21);
        button = v.findViewById(R.id.button3);

        cardViewfilters = v.findViewById(R.id.cardViewfilters);
        cardViewAiGenirator = v.findViewById(R.id.cardViewAiGenirator);
        cardViewCopyResult = v.findViewById(R.id.cardViewCopyResult);
        cardViewGrammerError = v.findViewById(R.id.cardViewGrammerError);

        historyArticleViewModel = new HistoryArticleViewModel(getActivity().getApplication());
        prefs = EncryptedPrefsManager.getInstance(getContext());
        user = prefs.getUser();
        firebaseRepository = new FirebaseRepository(getContext());
        dialogLoading = new DialogLoading(getContext());
        setType();
        showHiddenFilter();
        rewardedUnityAd = new RewardedUnityAd(getContext());
        rewardedUnityAd.loadRewardedAd();

        dialogLoading.loadingProgressDialog("Processing...");
        limit.setText(" / "+wordLimit+" Word");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLqnguqge.setAdapter(spinnerAdapter);
        spinnerAdapter.addAll(languages);
        spinnerAdapter.notifyDataSetChanged();

        spinnerModeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMode.setAdapter(spinnerModeAdapter);
        spinnerModeAdapter.addAll(models);
        spinnerModeAdapter.notifyDataSetChanged();

        if (bundle != null){
            if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                onProcessingComplete(((HistoryArticle) bundle.get("HistoryArticle")).getType());
            }
        }

        viewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(ChatViewModel.class);

        viewModel.getLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) dialogLoading.showLoadingProgressDialog();
            button.setEnabled(!isLoading);
            text.setEnabled(!isLoading);
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            dialogLoading.dismissLoadingProgressDialog();
        });

        viewModel.getResponseLiveData().observe(getViewLifecycleOwner(), response -> {

            dialogLoading.dismissLoadingProgressDialog();

            onProcessingComplete(response);

        });

        moreLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showHiddenFilter();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProcessingWordActivity) getActivity()).toHome();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String prompt = text.getText().toString().trim();
                if (!prompt.isEmpty()) {

                    if (user.getMembership().equals(FREE_PLAN_NAME)){
                        //show ads
                        //Toast.makeText(getContext(), "Show Ads FREE_PLAN_NAME", Toast.LENGTH_SHORT).show();
                        rewardedUnityAd.showRewardedAd();
                        pushToProcess();
                    }else {
                        // minis from wordPremium or wordProcessing orshow ads
                        // minis from wordPremium or wordProcessing
                        // go process
                        // push articl to history

                        String[] words = prompt.trim().isEmpty() ? new String[0] : prompt.trim().split("\\s+");

                        if (user.getWordPremium()>=words.length){
                            long curentWordPremium = user.getWordPremium()-words.length;
                            user.setWordPremium(curentWordPremium);
                            firebaseRepository.saveUser(user);
                            pushToProcess();
                        }else {
                            if (user.getWordProcessing()>=words.length){
                                long curentWordProcessing = user.getWordProcessing()-words.length;
                                user.setWordProcessing(curentWordProcessing);
                                firebaseRepository.saveUser(user);
                                pushToProcess();

                            }else {
                                //show ads
                                Toast.makeText(getContext(), "Show Ads", Toast.LENGTH_SHORT).show();
                                pushToProcess();
                            }
                        }
                    }

                } else {
                    Toast.makeText(getContext(), "Please enter text.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyClipboard(text.getText().toString());
            }
        });

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAsFile(text.getText().toString(),"txt");
            }
        });

        word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAsFile(text.getText().toString(),"word");
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAsFile(text.getText().toString(),"pdf");
            }
        });

        html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAsFile(text.getText().toString(),"html");
            }
        });


        text.addTextChangedListener(new TextWatcher() {
            boolean isUpdating = false;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (isUpdating) return;
                isUpdating = true;

                String input = charSequence.toString();

                // Split the text into words using whitespace as delimiter
                String[] words = input.trim().isEmpty() ? new String[0] : input.trim().split("\\s+");
                int wordCount = words.length;
                boolean textChanged = false;

                // Check if there are words and limit the last word to 30 characters
                if (wordCount > 0) {
                    String lastWord = words[wordCount - 1];

//                    // If the last word exceeds 30 characters, truncate it
//                    if (lastWord.length() > 30) {
//                        lastWord = lastWord.substring(0, 30);
//                        words[words.length - 1] = lastWord;
//                        textChanged = true;
//                    }

                    // If the last word exceeds 30 characters, delete it completely
                    if (lastWord.length() > 30) {
                        // Remove the last word by creating a new array with one less word
                        String[] newWords = new String[wordCount - 1];
                        System.arraycopy(words, 0, newWords, 0, wordCount - 1);
                        words = newWords;
                        textChanged = true;
                    }

                }

                // If the total words exceed 100, limit to the first 100 words
                if (wordCount > wordLimit) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < wordLimit; j++) {
                        sb.append(words[j]);
                        if (j < wordLimit-1) {
                            sb.append(" ");
                        }
                    }
                    input = sb.toString();
                    textChanged = true;
                } else if (textChanged) {
                    // If only the last word was modified, rebuild the input
                    input = String.join(" ", words);
                }

                // Apply changes if needed
                if (textChanged) {
                    text.setText(input);
                    text.setSelection(input.length());
                }

                // Update the word count display
                //int wordCount = words.length;*
                //int remainingWords = 100 - wordCount;

                //countWord.setText(wordCount+ "/100 words" + (words.length > 0 ? " | Last word: " + words[words.length - 1].length() + "/30 chars" : ""));
                //countWord.setText(String.valueOf(wordCount));*
                countWord.setText(String.valueOf(wordCount));

                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });



        return v;

    }

    private void pushToProcess(){

        if (countWords(text.getText().toString().trim()) > wordLimit){
            Toast.makeText(getContext(), "The text exceeds the allowed limit.", Toast.LENGTH_SHORT).show();
        }else {

            firebaseRepository.ProcessingAnalytics(type);

            switch (type){

                case"Paraphraser / Rewriting":

                    if (spinnerLqnguqge.getSelectedItemPosition() == 0){
                        Toast.makeText(getContext(), "Please select a language.", Toast.LENGTH_SHORT).show();
                    }else {
                        if (spinnerMode.getSelectedItemPosition() == 0){
                            Toast.makeText(getContext(), "Please select a text style mode.", Toast.LENGTH_SHORT).show();
                        }else {
                            viewModel.sendMessage(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    languages[spinnerLqnguqge.getSelectedItemPosition()],
                                    models[spinnerMode.getSelectedItemPosition()],
                                    ""
                            ));
                        }
                    }

                    break;
                case"Grammar Checker":
                case"AI Detector":
                case "Summarizer":

                    if (spinnerLqnguqge.getSelectedItemPosition() == 0){
                        Toast.makeText(getContext(), "Please select a language.", Toast.LENGTH_SHORT).show();
                    }else {
                        viewModel.sendMessage(new TypeProcessing(
                                text.getText().toString(),
                                type,
                                "",
                                "",
                                ""
                        ));
                    }

                    break;
                case"Paragraph Generator":

                    if (spinnerLqnguqge.getSelectedItemPosition() == 0){
                        Toast.makeText(getContext(), "Please select a language.", Toast.LENGTH_SHORT).show();
                    }else {
                        if (spinnerMode.getSelectedItemPosition() == 0){
                            Toast.makeText(getContext(), "Please select a text style mode.", Toast.LENGTH_SHORT).show();
                        }else {
                            if (type.equals("Paragraph Generator")){
                                viewModel.sendMessage(new TypeProcessing(
                                        text.getText().toString(),
                                        type,
                                        languages[spinnerLqnguqge.getSelectedItemPosition()],
                                        models[spinnerMode.getSelectedItemPosition()],
                                        keyword.getText().toString())
                                );
                            }
                        }
                    }

                    break;
                    
            }

        }

    }


    private void setType(){
        switch (type){

            case"Paraphraser / Rewriting":
                typeTextView.setText("Paraphraser / Rewriting");
                button.setText("Rewrite");

                if (user.getMembership().equals(PRO_PLAN_NAME)){
                    wordLimit = wordLimitProPlaneParaphraserRewriting;
                } else if (user.getMembership().equals(BASIC_PLAN_NAME)) {
                    wordLimit = wordLimitBasicPlaneParaphraserRewriting;
                }else {
                    wordLimit = wordLimitFreePlaneParaphraserRewriting;
                }

                img.setImageResource(R.drawable.paraphraser);
                cardViewfilters.setVisibility(View.VISIBLE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.GONE);
                keyword.setVisibility(View.GONE);
                moreLess.setVisibility(View.GONE);
                break;
            case"Grammar Checker":
                typeTextView.setText("Grammar Checker");
                button.setText("Checked");

                if (user.getMembership().equals(PRO_PLAN_NAME)){
                    wordLimit = wordLimitProPlaneGrammarChecker;
                } else if (user.getMembership().equals(BASIC_PLAN_NAME)) {
                    wordLimit = wordLimitBasicPlaneGrammarChecker;
                }else {
                    wordLimit = wordLimitFreePlaneGrammarChecker;
                }

                img.setImageResource(R.drawable.grammar);
                cardViewfilters.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewfilters.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.GONE);
                break;
            case"AI Detector":
                typeTextView.setText("AI Detector");
                button.setText("Detected");

                if (user.getMembership().equals(PRO_PLAN_NAME)){
                    wordLimit = wordLimitProPlaneAIDetector;
                } else if (user.getMembership().equals(BASIC_PLAN_NAME)) {
                    wordLimit = wordLimitBasicPlaneAIDetector;
                }else {
                    wordLimit = wordLimitFreePlaneAIDetector;
                }

                img.setImageResource(R.drawable.robot);
                cardViewfilters.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.GONE);
                break;
            case"Paragraph Generator":
                typeTextView.setText("Paragraph Generator");
                button.setText("Generated");

                if (user.getMembership().equals(PRO_PLAN_NAME)){
                    wordLimit = wordLimitProPlaneParagraphGenerator;
                } else if (user.getMembership().equals(BASIC_PLAN_NAME)) {
                    wordLimit = wordLimitBasicPlaneParagraphGenerator;
                }else {
                    wordLimit = wordLimitFreePlaneParagraphGenerator;
                }

                img.setImageResource(R.drawable.writing);
                cardViewfilters.setVisibility(View.VISIBLE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.GONE);

                break;
            case"Summarizer":
                typeTextView.setText("Summarizer");
                button.setText("Summarized");

                if (user.getMembership().equals(PRO_PLAN_NAME)){
                    wordLimit = wordLimitProPlaneSummarizer;
                } else if (user.getMembership().equals(BASIC_PLAN_NAME)) {
                    wordLimit = wordLimitBasicPlaneSummarizer;
                }else {
                    wordLimit = wordLimitFreePlaneSummarizer;
                }

                img.setImageResource(R.drawable.summarizer);
                cardViewfilters.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.GONE);
                break;

        }
    }

    private void showHiddenFilter(){

        if (isExpanded){
            moreLess.setText("show less");
            linearLayoutMore.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linearLayoutMore,"alpha",0f,1f);
            objectAnimator.setDuration(300);
            objectAnimator.start();
            isExpanded = !isExpanded;
        }else {
            moreLess.setText("show more");
            linearLayoutMore.setVisibility(View.GONE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linearLayoutMore,"alpha",1f,0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();
            isExpanded = !isExpanded;
        }

    }


    public GrammarChecker parseGrammarCheckerResponse(String response) {
        try {
            JSONObject jsonContent = new JSONObject(response);
            String text = jsonContent.getString("text");         // Extract the "text" field
            String issue = String.valueOf(jsonContent.getInt("issue")); // Extract and convert the "issue" field to String
            return new GrammarChecker(text, issue);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    @SuppressLint("SetTextI18n")
    private void onProcessingComplete(String response) {

        switch (type){

            case"Paraphraser / Rewriting":
                if ( !user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), response, type, System.currentTimeMillis()));
                    text.setText(response);
                }else {
                    text.setText( ((HistoryArticle) bundle.get("HistoryArticle")).getResponse() );
                }

                button.setVisibility(View.GONE);
                cardViewfilters.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.VISIBLE);
                break;
            case"Grammar Checker":
                GrammarChecker grammarChecker = new GrammarChecker();

                if (bundle != null){
                    if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                        grammarChecker = ((HistoryArticle) bundle.get("HistoryArticle")).getGrammarChecker();
                    }else {
                        grammarChecker = new GrammarChecker(parseGrammarCheckerResponse(response));
                        if (!user.getMembership().equals(FREE_PLAN_NAME)){
                            // String uid, GrammarChecker grammarChecker, String type, String article, long date
                            if (grammarChecker != null){
                                historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), grammarChecker, type, text.getText().toString().trim(), System.currentTimeMillis()));
                            }
                        }
                    }
                }

                text.setText(grammarChecker.getText());
                grammarPercentage.setText(grammarChecker.getIssue());
                button.setVisibility(View.GONE);
                cardViewfilters.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.VISIBLE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.VISIBLE);
                break;
            case"AI Detector":
                if ( !user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    // String uid,String response, String type, String article, long date
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), response, type, text.getText().toString().trim(), System.currentTimeMillis()));
                    try {
                        aiPercentage.setText(Integer.parseInt(response)+"%");
                        humanPercentage.setText((100-Integer.parseInt(response))+"%");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    try {
                        aiPercentage.setText(Integer.parseInt(((HistoryArticle) bundle.get("HistoryArticle")).getResponse())+"%");
                        humanPercentage.setText((100-Integer.parseInt(((HistoryArticle) bundle.get("HistoryArticle")).getResponse()))+"%");
                        text.setText(((HistoryArticle) bundle.get("HistoryArticle")).getArticle());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                cardViewfilters.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.VISIBLE);
                cardViewCopyResult.setVisibility(View.GONE);
                break;
            case"Paragraph Generator":
                if ( !user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    // String uid,String response, String type, long date
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), response, type, System.currentTimeMillis()));
                    text.setText(response);
                }else {
                    text.setText( ((HistoryArticle) bundle.get("HistoryArticle")).getResponse() );
                }

                cardViewfilters.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.VISIBLE);
                break;
            case"Summarizer":
                if ( !user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    // String uid,String response, String type, long date
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), response, type, System.currentTimeMillis()));
                    text.setText(response);
                }else {
                    text.setText( ((HistoryArticle) bundle.get("HistoryArticle")).getResponse() );
                }

                cardViewfilters.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.VISIBLE);
                break;

        }

        // Update the word count display
        countWord.setText(String.valueOf(countWords(text.getText().toString().trim())));

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



//    private void loadingProgressDialog() {
//        dialog = new Dialog(getContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.loading, null);
//        dialog.setContentView(view);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//    }
//
//
//    public void showLoadingProgressDialog() {
//        dialog.show();
//    }
//
//    public void dismissLoadingProgressDialog() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }

//    public boolean isShowingLoadingProgressDialog() {
//        return dialog != null && dialog.isShowing();
//    }

    public void copyClipboard(String text){
        // Inside your Fragment class, e.g., in onViewCreated or another method
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(requireActivity(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("MissingInflatedId")
    public void saveAsFile(String text,String type){

        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_name_save_file, null);

        // Get reference to the EditText in the dialog layout
        final EditText editText = dialogView.findViewById(R.id.dialog_edit_text);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Name File")
                .setView(dialogView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (!Environment.isExternalStorageManager()) {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                    Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                    Toast.makeText(requireContext(), "Please grant all files access permission", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    // If the specific intent isn't available, try the general storage settings
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivity(intent);
                                    Toast.makeText(requireContext(), "Please grant all files access permission", Toast.LENGTH_LONG).show();
                                }
                            } else {

                                // txt word pdf html
                                switch (type){
                                    case "txt":
                                        saveAsTxt(text,editText.getText().toString().trim());
                                        break;
                                    case "word":
                                        saveTextAsWord(text,editText.getText().toString().trim());
                                        break;
                                    case "pdf":
                                        saveTextAsPDF(text,editText.getText().toString().trim());
                                        break;
                                    case "html":
                                        saveAsHtml(text,editText.getText().toString().trim());
                                        break;
                                }

                            }
                        } else {
                            // For Android 10 (API 29) and below, request READ_EXTERNAL_STORAGE
                            requestPermissionLauncher.launch(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE);
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void saveAsTxt(String text,String fileName){

        String SAVE_FOLDER_NAME = "PhraseFlow";
        String SAVE_FILE_NAME = fileName+".txt";

        File file = getContext().getDatabasePath(SAVE_FILE_NAME);
        File sdir = new File(Environment.getExternalStorageDirectory(), SAVE_FOLDER_NAME);

        String path = sdir.getPath() + File.separator + SAVE_FILE_NAME;

        if (!sdir.exists()) {
            sdir.mkdirs();
        }

        if (path != null) {
            // Create a new file in that directory
            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(text.getBytes());
                Toast.makeText(requireContext(), "File saved: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Error saving file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "External storage not available", Toast.LENGTH_SHORT).show();
        }

    }


    private void saveTextAsWord(String text, String fileName) {

        String SAVE_FOLDER_NAME = "PhraseFlow";
        String SAVE_FILE_NAME = fileName+".docx";

        File file = getContext().getDatabasePath(SAVE_FILE_NAME);
        File sdir = new File(Environment.getExternalStorageDirectory(), SAVE_FOLDER_NAME);

        String path = sdir.getPath() + File.separator + SAVE_FILE_NAME;

        if (!sdir.exists()) {
            sdir.mkdirs();
        }

        try {
            // Create a new document
            XWPFDocument document = new XWPFDocument();

            // Create a new paragraph
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(text);

            // Define the file path
            //File file = new File(getExternalFilesDir(null), fileName + ".docx");

            // Write the document to file
            FileOutputStream out = new FileOutputStream(path);
            document.write(out);
            out.close();
            document.close();

            Toast.makeText(getContext(), "Document saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error saving document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTextAsPDF(String text, String fileName) {
        String SAVE_FOLDER_NAME = "PhraseFlow";
        String SAVE_FILE_NAME = fileName+".pdf";

        File file = getContext().getDatabasePath(SAVE_FILE_NAME);
        File sdir = new File(Environment.getExternalStorageDirectory(), SAVE_FOLDER_NAME);

        String path = sdir.getPath() + File.separator + SAVE_FILE_NAME;

        if (!sdir.exists()) {
            sdir.mkdirs();
        }

        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            document.add(new Paragraph(text));
            document.close();

            Toast.makeText(getContext(), "PDF saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAsHtml(String content, String fileName) {
        try {
            // Create HTML content
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    //"    <title>" + fileName + "</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    content +
                    "\n</body>\n" +
                    "</html>";

            String SAVE_FOLDER_NAME = "PhraseFlow";
            String SAVE_FILE_NAME = fileName+".html";

            File file = getContext().getDatabasePath(SAVE_FILE_NAME);
            File sdir = new File(Environment.getExternalStorageDirectory(), SAVE_FOLDER_NAME);

            String path = sdir.getPath() + File.separator + SAVE_FILE_NAME;

            if (!sdir.exists()) {
                sdir.mkdirs();
            }

            // Write to the file
            FileWriter writer = new FileWriter(path);
            writer.write(htmlContent);
            writer.close();

            Toast.makeText(requireContext(), "HTML saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error saving HTML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }






}