package com.noureddine.WriteFlow.fragments;



import static android.app.Activity.RESULT_OK;
import static com.noureddine.WriteFlow.Utils.CopySaveResult.SAVE_FOLDER_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.BASIC_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.PRO_PLAN_NAME;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneAIDetector;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneGrammarChecker;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneParagraphGenerator;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneParaphraserRewriting;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlanePlagiarismChecking;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneSmartTranslation;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitBasicPlaneSummarizer;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneAIDetector;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneGrammarChecker;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneParagraphGenerator;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneParaphraserRewriting;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlanePlagiarismChecking;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneSmartTranslation;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitFreePlaneSummarizer;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneAIDetector;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneGrammarChecker;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneParagraphGenerator;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneParaphraserRewriting;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlanePlagiarismChecking;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneSmartTranslation;
import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.wordLimitProPlaneSummarizer;
import static com.noureddine.WriteFlow.Utils.TextProcessing.countWords;
import static com.unity3d.scar.adapter.common.Utils.runOnUiThread;
import static com.unity3d.services.core.properties.ClientProperties.getApplicationContext;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.CopySaveResult;
import com.noureddine.WriteFlow.Utils.DialogLoading;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.Utils.ExtractTextFromFile;
import com.noureddine.WriteFlow.Utils.FilesManager;
import com.noureddine.WriteFlow.Utils.GsonToGrammarChecker;
import com.noureddine.WriteFlow.Utils.RemoveOutsideBraces;
import com.noureddine.WriteFlow.activities.ProcessingWordActivity;
import com.noureddine.WriteFlow.model.GrammarChecker;
import com.noureddine.WriteFlow.model.HistoryArticle;
import com.noureddine.WriteFlow.model.ProcessingWord;
import com.noureddine.WriteFlow.model.ResultApi;
import com.noureddine.WriteFlow.model.TypeProcessing;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.repositorys.FirebaseRepository;
import com.noureddine.WriteFlow.viewModels.ChatViewModel;
import com.noureddine.WriteFlow.viewModels.GeminiViewModel;
import com.noureddine.WriteFlow.viewModels.HistoryArticleViewModel;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ProcessingWordsFragment extends Fragment implements IUnityAdsInitializationListener , FilesManager.FileSelectionListener {


    private final String GAME_ID = "5817517";
    private final boolean TEST_MODE = false; // Disable for production

    private String rewardedAdUnitId = "Rewarded_Android";

    private String []languages = {"  --  Languages  --  ","  English (US)  ","  French  ","  Spanish  ","  German  ","  Arabic  ","  Africans  ","  Chinese  ","  Hindi  ",
            "  Romanian  ","  Russian  ", "  Danish  ","  Indonesian  ","  Dutch  ","  Italian  ","  Swedish  ","  English (AU)  ","  Japanese  ","  Malay  ",
            "  Tagalog  ","  English (CA)  ","  Turkish  ", "  English (UK)  ","  Norwegian  ","  Ukrainian  ","  Polish  ","  Vietnamese  ","  Portuguese  "};

    private String []models = {"  -- Modes --  ","  Standard  ","  Fluency  ","  Humanize  ","  Formal  ","  Academic  ","  Simple  ","  Creative  ","  Expand  "};

    private final String []modelAi = {"openai","gemini"};

    int wordLimit = 0;
    private TextView typeTextView ,moreLess ,countWord ,limit,aiPercentage,humanPercentage,grammarPercentage,textViewIssue,textViewCase;
    private ImageView img ,back,copy,txt,word,pdf,html,upload,mic,website;
    private Spinner spinnerLqnguqge ,spinnerMode;
    private LinearLayout linearLayoutMore ;
    private EditText keyword ,text;
    private Button button ;
    private CardView cardViewfilters,cardViewAiGenirator,cardViewCopyResult,cardViewGrammerError;
    private DialogLoading dialogLoading;
    private ChatViewModel viewModel;
    private GeminiViewModel geminiViewModel;
    private String type;
    boolean isExpanded = false;
    private ArrayAdapter<String> spinnerModeAdapter;
    private EncryptedPrefsManager prefs;
    private User user ;
    private FirebaseRepository firebaseRepository;
    private HistoryArticleViewModel historyArticleViewModel;
    private HistoryArticle historyArticle = new HistoryArticle();
    private Bundle bundle;
    private CopySaveResult copySaveResult;

    private FilesManager filesManager;
    ActivityResultLauncher<Intent> filePickerLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    private static String TAG = "ProcessingWordsFragment";

    // Activity result launcher for regular storage permission
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(requireContext(), "Regular storage permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Regular storage permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> micPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    recordAudioDialog();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });




    //dialog uploa file
    private AlertDialog uploadDialog;
    private Handler handler = new Handler();
    private int uploadProgress = 0;
    private ImageView imageViewSelecteFile ;
    private LinearLayout uploadArea ;
    private LinearLayout linearLayoutFilesUploading ;
    private TextView TextFileName ;
    private TextView textViewProgress ;
    private ProgressBar progressBarUpload ;
    private Button buttonUpload ;
    private String selectedFile;

    //dialog add link
    private TextView textViewTitalLink ;
    private EditText editTextLinkUrl ;
    private Button buttonAddLink ;
    private boolean isUrlValide = false ;
    private ProgressBar progressBarCheckUrl;


    //dialog record voice
    private AlertDialog recordVoiceDialog;
    private TextView textViewTime ;
    //private LinearLayout linearLayoutwaveform ;
    //private LinearLayout linearLayoutPause ;
    //private LinearLayout linearLayoutStop ;
    private Button buttonStopAndSave ;
    private View[] bars;
    private MediaRecorder recorder;
    private Handler handlerRecording = new Handler(Looper.getMainLooper());
    private boolean isRecording = false;
    private File outFile;  // نجعله حقلًا لتتمكّن من استخدامه في stopRecording()

//    private CountDownTimer countDownTimer;
//    private static final long TOTAL_TIME_MS = 5 * 60 * 1000; // 5 دقائق
//    private long timeLeftMs = TOTAL_TIME_MS; // يخزن الوقت المتبقي

    private Handler handlerTimeCounter = new Handler();
    private long startTimeMillis;
    //private boolean isRunning = false;
    private final long MAX_TIME_MS = 5 * 60 * 1000; // 5 دقائق بالمللي ثانية




    public ProcessingWordsFragment() {}

    public static ProcessingWordsFragment newInstance() {
        ProcessingWordsFragment fragment = new ProcessingWordsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getArguments();
        if (bundle != null){
            if(bundle.get("HistoryArticle") instanceof HistoryArticle){
                historyArticle = (HistoryArticle) bundle.get("HistoryArticle");
                type = historyArticle.getType();
            }else {
                type = bundle.getString("type");
            }
        }


        filesManager = new FilesManager((AppCompatActivity) getActivity());
        //initLaunchers();
        setupActivityResultLaunchers();



    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_processing_words,container,false);

        typeTextView = v.findViewById(R.id.textView4);
        aiPercentage = v.findViewById(R.id.textView23);
        humanPercentage = v.findViewById(R.id.textView25);
        grammarPercentage = v.findViewById(R.id.textView26);
        textViewIssue = v.findViewById(R.id.textView22);
        textViewCase = v.findViewById(R.id.textView24);
        img = v.findViewById(R.id.imageView4);
        back = v.findViewById(R.id.imageView6);
        copy = v.findViewById(R.id.imageViewCopy);
        txt = v.findViewById(R.id.imageViewTxt);
        word = v.findViewById(R.id.imageViewWord);
        pdf = v.findViewById(R.id.imageViewPdf);
        html = v.findViewById(R.id.imageViewHtml);
        upload = v.findViewById(R.id.imageViewUploadFile);
        mic = v.findViewById(R.id.imageViewMic);
        website = v.findViewById(R.id.imageViewWeb);
        spinnerLqnguqge = v.findViewById(R.id.spinnerLanguage);
        linearLayoutMore = v.findViewById(R.id.linearLayoutMore);
        spinnerMode = v.findViewById(R.id.spinnerStyle);
        keyword = v.findViewById(R.id.editTextTextKeyWords);
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

        if (user.getMembership().equals(FREE_PLAN_NAME)){
            UnityAds.initialize(getApplicationContext(), GAME_ID, TEST_MODE, this);
            loadRewardedAd();
        }

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
                onProcessingComplete(new ResultApi(((HistoryArticle) bundle.get("HistoryArticle")).getType().trim(),0,0));
            }
        }

        fetchOpenai();
        fetchGemini();

        copySaveResult = new CopySaveResult(getActivity(),requestPermissionLauncher);

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
                        showRewardedAd();
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
                                Log.d(TAG, "onClick: Show Ads");
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
                copySaveResult.copyClipboard(text.getText().toString());
            }
        });

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySaveResult.saveAsFile(text.getText().toString(),"txt", user.getUid());
            }
        });

        word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySaveResult.saveAsFile(text.getText().toString(),"word", user.getUid());
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySaveResult.saveAsFile(text.getText().toString(),"pdf", user.getUid());
            }
        });

        html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copySaveResult.saveAsFile(text.getText().toString(),"html", user.getUid());
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();

                //showUploadFileDialog();

            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();

                //checkPermissionAndRecord();


            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();

                //loadLinkDialog();

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

//    private void processData(){
//        CountDownLatch latch1 = new CountDownLatch(1);
//        CountDownLatch latch2 = new CountDownLatch(1);
//
//        Thread backgroundThread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("TAG", "Load Data 1 - Start");
//                try { Thread.sleep(2000); } catch (InterruptedException e) {}
//                Log.d("TAG", "Load Data 1 - Finished");
//                latch1.countDown(); // إشارة انتهاء data1
//            }
//        });
//
//        Thread backgroundThread2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    latch1.await(); // انتظار data1
//                    Log.d("TAG", "Load Data 2 - Start");
//                    Thread.sleep(1500);
//                    Log.d("TAG", "Load Data 2 - Finished");
//                    latch2.countDown(); // إشارة انتهاء data2
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Thread backgroundThread3 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    latch1.await(); // انتظار data1
//                    latch2.await(); // انتظار data2
//                    Log.d("TAG", "Load Data 3 - Start");
//                    Thread.sleep(1000);
//                    Log.d("TAG", "Load Data 3 - Finished");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        backgroundThread1.start();
//        backgroundThread2.start();
//        backgroundThread3.start();
//    }


    private void fetchOpenai() {

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

            ProcessingWord processingWord = new ProcessingWord(
                    user.getUid(),
                    type,
                    response.getPromptTokens(),
                    response.getCandidatesTokens(),
                    System.currentTimeMillis()
            );

            firebaseRepository.ProcessingAnalytics(processingWord);

        });

    }

    private void fetchGemini() {

        geminiViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(GeminiViewModel.class);


        geminiViewModel.getResultApi().observe(getViewLifecycleOwner(), result -> {
            dialogLoading.dismissLoadingProgressDialog();
            onProcessingComplete(result);

            ProcessingWord processingWord = new ProcessingWord(
                    user.getUid(),
                    type,
                    result.getPromptTokens(),
                    result.getCandidatesTokens(),
                    System.currentTimeMillis()
            );

            firebaseRepository.ProcessingAnalytics(processingWord);

        });

        geminiViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d("TAG", "onCreate: isLoading");
            if (isLoading) dialogLoading.showLoadingProgressDialog();
            button.setEnabled(!isLoading);
            text.setEnabled(!isLoading);
        });

        geminiViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.d("TAG", "onCreate: "+error);
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                dialogLoading.dismissLoadingProgressDialog();
            }
        });

    }


        private void pushToProcess(){

        int countW = countWords(text.getText().toString().trim());

        if ( countW > wordLimit){
            Toast.makeText(getContext(), "The text exceeds the allowed limit.", Toast.LENGTH_SHORT).show();
        }else {

            switch (type){

                case"Paraphraser / Rewriting":

                    if (spinnerLqnguqge.getSelectedItemPosition() == 0){
                        Toast.makeText(getContext(), "Please select a language.", Toast.LENGTH_SHORT).show();
                    }else {
                        if (spinnerMode.getSelectedItemPosition() == 0){
                            Toast.makeText(getContext(), "Please select a text style mode.", Toast.LENGTH_SHORT).show();
                        }else {

                            if (prefs.getToolPreferences().getParaphraserModel().equals(modelAi[0])){
                                viewModel.sendMessage(new TypeProcessing(
                                        text.getText().toString(),
                                        type,
                                        languages[spinnerLqnguqge.getSelectedItemPosition()],
                                        models[spinnerMode.getSelectedItemPosition()],
                                        ""
                                ));
                                //Toast.makeText(getContext(), "load openai", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "pushToProcess: load openai");
                            }else if (prefs.getToolPreferences().getParaphraserModel().equals(modelAi[1])){
                                geminiViewModel.generateContent(new TypeProcessing(
                                        text.getText().toString(),
                                        type,
                                        languages[spinnerLqnguqge.getSelectedItemPosition()],
                                        models[spinnerMode.getSelectedItemPosition()],
                                        ""
                                ));
                                //Toast.makeText(getContext(), "load gemini", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "pushToProcess: load gemini");
                            }else {
                                Toast.makeText(getContext(), "Failed get Tool", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    break;
                case"Grammar Checker":

                    if (spinnerLqnguqge.getSelectedItemPosition() == 0){
                        Toast.makeText(getContext(), "Please select a language.", Toast.LENGTH_SHORT).show();
                    }else {

                        if (prefs.getToolPreferences().getGrammarCheckerModel().equals(modelAi[0])){
                            viewModel.sendMessage(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    "",
                                    "",
                                    ""
                            ));
                            //Toast.makeText(getContext(), "load openai", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pushToProcess: load openai");
                        }else if (prefs.getToolPreferences().getParagraphGeneratorModel().equals(modelAi[1])){
                            geminiViewModel.generateContent(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    "",
                                    "",
                                    ""
                            ));
                            //Toast.makeText(getContext(), "load gemini", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pushToProcess: load gemini");
                        }else {
                            Toast.makeText(getContext(), "Failed get Tool", Toast.LENGTH_SHORT).show();
                        }

                    }

                    break;
                case"AI Detector":

                    if (spinnerLqnguqge.getSelectedItemPosition() == 0){
                        Toast.makeText(getContext(), "Please select a language.", Toast.LENGTH_SHORT).show();
                    }else {

                        if (prefs.getToolPreferences().getAiDetectorModel().equals(modelAi[0])){
                            viewModel.sendMessage(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    "",
                                    "",
                                    ""
                            ));
                            //Toast.makeText(getContext(), "load openai", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pushToProcess: load openai");
                        }else if (prefs.getToolPreferences().getAiDetectorModel().equals(modelAi[1])){
                            geminiViewModel.generateContent(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    "",
                                    "",
                                    ""
                            ));
                            //Toast.makeText(getContext(), "load gemini", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pushToProcess: load gemini");
                        }else {
                            Toast.makeText(getContext(), "Failed get Tool", Toast.LENGTH_SHORT).show();
                        }

                    }

                    break;
                case "Summarizer":

                    if (spinnerLqnguqge.getSelectedItemPosition() == 0){//hhh
                        Toast.makeText(getContext(), "Please select a language.", Toast.LENGTH_SHORT).show();
                    }else {
                        if (prefs.getToolPreferences().getSummarizerModel().equals(modelAi[0])){
                            viewModel.sendMessage(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    "",
                                    "",
                                    ""
                            ));
                            //Toast.makeText(getContext(), "load openai", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pushToProcess: load openai");
                        }else if (prefs.getToolPreferences().getSummarizerModel().equals(modelAi[1])){
                            geminiViewModel.generateContent(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    "",
                                    "",
                                    ""
                            ));
                            //Toast.makeText(getContext(), "load gemini", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pushToProcess: load gemini");
                        }else {
                            Toast.makeText(getContext(), "Failed get Tool", Toast.LENGTH_SHORT).show();
                        }

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

                                if (prefs.getToolPreferences().getParagraphGeneratorModel().equals(modelAi[0])){
                                    viewModel.sendMessage(new TypeProcessing(
                                            text.getText().toString(),
                                            type,
                                            languages[spinnerLqnguqge.getSelectedItemPosition()],
                                            models[spinnerMode.getSelectedItemPosition()],
                                            keyword.getText().toString())
                                    );
                                    //Toast.makeText(getContext(), "load openai", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "pushToProcess: load openai");
                                }else if (prefs.getToolPreferences().getParagraphGeneratorModel().equals(modelAi[1])){
                                    geminiViewModel.generateContent(new TypeProcessing(
                                            text.getText().toString(),
                                            type,
                                            languages[spinnerLqnguqge.getSelectedItemPosition()],
                                            models[spinnerMode.getSelectedItemPosition()],
                                            keyword.getText().toString())
                                    );
                                    //Toast.makeText(getContext(), "load gemini", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "pushToProcess: load gemini");
                                }else {
                                    Toast.makeText(getContext(), "Failed get Tool", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }

                    break;
                case"Smart Translation":

                    if (spinnerLqnguqge.getSelectedItemPosition() == 0){
                        Toast.makeText(getContext(), "Please select a language.", Toast.LENGTH_SHORT).show();
                    }else {

                        if (prefs.getToolPreferences().getSmartTranslationModel().equals(modelAi[0])){
                            viewModel.sendMessage(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    "",
                                    "",
                                    ""
                            ));
                            //Toast.makeText(getContext(), "load openai", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pushToProcess: load openai");
                        }else if (prefs.getToolPreferences().getSmartTranslationModel().equals(modelAi[1])){
                            geminiViewModel.generateContent(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    "",
                                    "",
                                    ""
                            ));
                            //Toast.makeText(getContext(), "load gemini", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pushToProcess: load gemini");
                        }else {
                            Toast.makeText(getContext(), "Failed get Tool", Toast.LENGTH_SHORT).show();
                        }

                    }

                    break;
                case"Plagiarism Checking":

                    if (spinnerLqnguqge.getSelectedItemPosition() == 0){
                        Toast.makeText(getContext(), "Please select a language.", Toast.LENGTH_SHORT).show();
                    }else {

                        if (prefs.getToolPreferences().getPlagiarismCheckingModel().equals(modelAi[0])){
                            viewModel.sendMessage(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    "",
                                    "",
                                    ""
                            ));
                            //Toast.makeText(getContext(), "load openai", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pushToProcess: load openai");
                        }else if (prefs.getToolPreferences().getPlagiarismCheckingModel().equals(modelAi[1])){
                            geminiViewModel.generateContent(new TypeProcessing(
                                    text.getText().toString(),
                                    type,
                                    "",
                                    "",
                                    ""
                            ));
                            //Toast.makeText(getContext(), "load gemini", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "pushToProcess: load gemini");
                        }else {
                            Toast.makeText(getContext(), "Failed get Tool", Toast.LENGTH_SHORT).show();
                        }

                    }

                    break;
            }

        }

    }


    private void setType(){

        upload.setVisibility(View.VISIBLE);
        mic.setVisibility(View.VISIBLE);
        website.setVisibility(View.VISIBLE);

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
                spinnerLqnguqge.setVisibility(View.VISIBLE);
                moreLess.setVisibility(View.VISIBLE);
                spinnerMode.setVisibility(View.VISIBLE);
                keyword.setVisibility(View.VISIBLE);

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

//                spinnerLqnguqge.setVisibility(View.GONE);
//                spinnerMode.setVisibility(View.GONE);
//                keyword.setVisibility(View.GONE);

                cardViewGrammerError.setVisibility(View.GONE);
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

                //spinnerLqnguqge.setVisibility(View.GONE);
                //spinnerMode.setVisibility(View.GONE);
                //keyword.setVisibility(View.GONE);

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

                spinnerLqnguqge.setVisibility(View.VISIBLE);
                spinnerMode.setVisibility(View.VISIBLE);
                moreLess.setVisibility(View.VISIBLE);
                keyword.setVisibility(View.VISIBLE);

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

//                spinnerLqnguqge.setVisibility(View.GONE);
//                spinnerMode.setVisibility(View.GONE);
//                keyword.setVisibility(View.GONE);

                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.GONE);
                break;
            case"Smart Translation":
                typeTextView.setText("Smart Translation");
                button.setText("Smart Translation");

                if (user.getMembership().equals(PRO_PLAN_NAME)){
                    wordLimit = wordLimitProPlaneSmartTranslation;
                } else if (user.getMembership().equals(BASIC_PLAN_NAME)) {
                    wordLimit = wordLimitBasicPlaneSmartTranslation;
                }else {
                    wordLimit = wordLimitFreePlaneSmartTranslation;
                }

                img.setImageResource(R.drawable.translation);

                cardViewfilters.setVisibility(View.VISIBLE);
                spinnerLqnguqge.setVisibility(View.VISIBLE);
                moreLess.setVisibility(View.GONE);
                spinnerMode.setVisibility(View.GONE);
                keyword.setVisibility(View.GONE);

                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.GONE);
                break;
            case"Plagiarism Checking":
                typeTextView.setText("Plagiarism Checking");
                button.setText("Plagiarism Checking");

                if (user.getMembership().equals(PRO_PLAN_NAME)){
                    wordLimit = wordLimitProPlanePlagiarismChecking;
                } else if (user.getMembership().equals(BASIC_PLAN_NAME)) {
                    wordLimit = wordLimitBasicPlanePlagiarismChecking;
                }else {
                    wordLimit = wordLimitFreePlanePlagiarismChecking;
                }

                img.setImageResource(R.drawable.plagiarism);
                img.setImageTintList(ColorStateList.valueOf(Color.WHITE));

                cardViewfilters.setVisibility(View.GONE);

                //spinnerLqnguqge.setVisibility(View.GONE);
                //spinnerMode.setVisibility(View.GONE);
                //keyword.setVisibility(View.GONE);

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



    @SuppressLint("SetTextI18n")
    private void onProcessingComplete(ResultApi responseGemini) {

        upload.setVisibility(View.GONE);
        mic.setVisibility(View.GONE);
        website.setVisibility(View.GONE);

        switch (type){

            case"Paraphraser / Rewriting":
                if ( !user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), responseGemini.getResult(), type, System.currentTimeMillis()));
                    text.setText(responseGemini.getResult());
                }else if( user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    text.setText(responseGemini.getResult());
                }else {
                    text.setText( ((HistoryArticle) bundle.get("HistoryArticle")).getResponse() );
                }

                button.setVisibility(View.GONE);
                //cardViewfilters.setVisibility(View.GONE);

                spinnerLqnguqge.setVisibility(View.GONE);
                spinnerMode.setVisibility(View.GONE);
                keyword.setVisibility(View.GONE);

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
                        String newResponse = RemoveOutsideBraces.removeOutsideBraces(responseGemini.getResult());
                        grammarChecker = new GrammarChecker(GsonToGrammarChecker.parseGrammarCheckerResponse(newResponse));
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
                //cardViewfilters.setVisibility(View.GONE);

                spinnerLqnguqge.setVisibility(View.GONE);
                spinnerMode.setVisibility(View.GONE);
                keyword.setVisibility(View.GONE);

                cardViewGrammerError.setVisibility(View.VISIBLE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.VISIBLE);
                break;
            case"AI Detector":
                if ( !user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    // String uid,String response, String type, String article, long date
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), responseGemini.getResult(), type, text.getText().toString().trim(), System.currentTimeMillis()));
                    try {
                        aiPercentage.setText(Integer.parseInt(responseGemini.getResult().trim())+"%");
                        humanPercentage.setText((100-Integer.parseInt(responseGemini.getResult().trim()))+"%");
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

                //cardViewfilters.setVisibility(View.GONE);

                textViewIssue.setText("AI-generated :");
                textViewCase.setText("Human-written :");

                spinnerLqnguqge.setVisibility(View.GONE);
                spinnerMode.setVisibility(View.GONE);
                keyword.setVisibility(View.GONE);

                button.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.VISIBLE);
                cardViewCopyResult.setVisibility(View.GONE);
                break;
            case"Paragraph Generator":
                if ( !user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    // String uid,String response, String type, long date
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), responseGemini.getResult(), type, System.currentTimeMillis()));
                    text.setText(responseGemini.getResult());
                }else {
                    text.setText( ((HistoryArticle) bundle.get("HistoryArticle")).getResponse() );
                }

                //cardViewfilters.setVisibility(View.GONE);

                spinnerLqnguqge.setVisibility(View.GONE);
                spinnerMode.setVisibility(View.GONE);
                keyword.setVisibility(View.GONE);

                button.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.VISIBLE);
                break;
            case"Summarizer":
                if ( !user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    // String uid,String response, String type, long date
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), responseGemini.getResult(), type, System.currentTimeMillis()));
                    text.setText(responseGemini.getResult());
                }else if(user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle)){
                    text.setText(responseGemini.getResult());
                }else{
                    text.setText( ((HistoryArticle) bundle.get("HistoryArticle")).getResponse() );
                }

                //cardViewfilters.setVisibility(View.GONE);

                spinnerLqnguqge.setVisibility(View.GONE);
                spinnerMode.setVisibility(View.GONE);
                keyword.setVisibility(View.GONE);

                button.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.VISIBLE);
                break;
            case"Smart Translation":
                if ( !user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), responseGemini.getResult(), type, System.currentTimeMillis()));
                    text.setText(responseGemini.getResult());
                }else if( user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    text.setText(responseGemini.getResult());
                }else {
                    text.setText( ((HistoryArticle) bundle.get("HistoryArticle")).getResponse() );
                }

                button.setVisibility(View.GONE);
                //cardViewfilters.setVisibility(View.GONE);

                spinnerLqnguqge.setVisibility(View.GONE);
                spinnerMode.setVisibility(View.GONE);
                keyword.setVisibility(View.GONE);

                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.GONE);
                cardViewCopyResult.setVisibility(View.VISIBLE);
                break;
            case"Plagiarism Checking":
                if ( !user.getMembership().equals(FREE_PLAN_NAME) && !(bundle.get("HistoryArticle") instanceof HistoryArticle) ){
                    // String uid,String response, String type, String article, long date
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), responseGemini.getResult(), type, text.getText().toString().trim(), System.currentTimeMillis()));
                    try {
                        aiPercentage.setText(Integer.parseInt(responseGemini.getResult().trim())+"%");
                        humanPercentage.setText((100-Integer.parseInt(responseGemini.getResult().trim()))+"%");
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

                //cardViewfilters.setVisibility(View.GONE);

                textViewIssue.setText("Similarity Percentage :");
                textViewCase.setText("Singularity Percentage :");

                spinnerLqnguqge.setVisibility(View.GONE);
                spinnerMode.setVisibility(View.GONE);
                keyword.setVisibility(View.GONE);

                button.setVisibility(View.GONE);
                cardViewGrammerError.setVisibility(View.GONE);
                cardViewAiGenirator.setVisibility(View.VISIBLE);
                cardViewCopyResult.setVisibility(View.GONE);
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


    // REWARDED ADS METHODS

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
                // Retry loading after a delay
                new Handler().postDelayed(() -> loadRewardedAd(), 5000);
            }
        });
    }

    public void showRewardedAd() {
        UnityAds.show( getActivity(), rewardedAdUnitId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                // Rewarded ad show failed
                Log.e("UnityAds", "Rewarded ad show failed: " + error + " - " + message);
                loadRewardedAd();
                dialogLoading.dismissLoadingProgressDialog();
                Toast.makeText(getContext(), "Ad show failed click agin ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {
                // Rewarded ad show started
                Log.d("UnityAds", "Rewarded ad show started");
                pushToProcess();
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





    private void showUploadFileDialog() {
        if (uploadDialog != null && uploadDialog.isShowing()) return; // Don't recreate

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_upload_file, null);

        imageViewSelecteFile = dialogView.findViewById(R.id.imageViewSelecteFile);
        uploadArea = dialogView.findViewById(R.id.uploadArea);

        linearLayoutFilesUploading = dialogView.findViewById(R.id.linearLayoutFilesUploading);
        TextFileName = dialogView.findViewById(R.id.textView28);
        textViewProgress = dialogView.findViewById(R.id.textViewProgress);
        progressBarUpload = dialogView.findViewById(R.id.progressBarUpload);

        final Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        buttonUpload = dialogView.findViewById(R.id.buttonUpload);

        // ... باقي الكود كما هو

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        uploadDialog = builder.create();

        uploadArea.setVisibility(View.VISIBLE);
        linearLayoutFilesUploading.setVisibility(View.GONE);
        textViewProgress.setVisibility(View.GONE);
        progressBarUpload.setVisibility(View.GONE);
        buttonUpload.setEnabled(false);
        buttonUpload.setAlpha(0.5f);

        // أزرار
        imageViewSelecteFile.setOnClickListener(V->{
            filesManager.selectFile();
        });

        buttonCancel.setOnClickListener(V->{
            uploadDialog.dismiss();
        });

        buttonUpload.setOnClickListener(V->{
            textViewProgress.setVisibility(View.VISIBLE);
            progressBarUpload.setVisibility(View.VISIBLE);
            buttonUpload.setText("Uploading...");
            buttonUpload.setEnabled(false);
            buttonUpload.setAlpha(0.5f);
            //uploadFile();
        });
        uploadDialog.setCancelable(false);
        uploadDialog.show();
    }


    // Register Activity Result Launchers
    private void setupActivityResultLaunchers() {
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();

                        File file = new File(getPath(getContext(), fileUri)); // Use your own method or a utility to get file path

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            List<Bitmap> images = ExtractTextFromFile.extractFromPdf(getContext(), file);
                            //send to convert img to text
                        }

                    } else {
                        filesManager.notifyError("No file selected");
                    }
                }
        );

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        filesManager.openFilePicker();
                    } else {
                        filesManager.notifyError("Permission required to access files");
                    }
                }
        );

        filesManager.setLaunchers(filePickerLauncher, permissionLauncher);

    }


    // واجهة الاستجابة من FilesManager
    @Override
    public void onFileSelected(FilesManager.FileInfo fileInfo) {

        selectedFile = fileInfo.getExtension();
        TextFileName.setText(fileInfo.getFileName());
        uploadArea.setVisibility(View.GONE);
        linearLayoutFilesUploading.setVisibility(View.VISIBLE);
        textViewProgress.setVisibility(View.GONE);
        progressBarUpload.setVisibility(View.GONE);
        buttonUpload.setEnabled(true);
        buttonUpload.setAlpha(1.0f);

    }

    @Override
    public void onFileSelectionError(String error) {
        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFileSelectionCancelled() {
        Toast.makeText(getContext(), "File selection cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionDenied() {
        Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
    }



    // Convert a Uri into a File object by copying its content to cache dir
    public static String getPath(Context context, Uri uri) {
        String fileName = getFileName(context, uri);
        if (fileName == null) fileName = "temp_file";

        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, fileName);

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buf = new byte[4096];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }

            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return file.getAbsolutePath(); // ✅ Return local path to use with File
    }

    @SuppressLint("Range")
    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }

        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }













    @SuppressLint("MissingInflatedId")
    private void loadLinkDialog() {

        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_link, null);

        // Get reference to the EditText in the dialog layout
        textViewTitalLink = dialogView.findViewById(R.id.textView29);
        editTextLinkUrl = dialogView.findViewById(R.id.editTextLinkUrl);
        buttonAddLink = dialogView.findViewById(R.id.buttonAddLink);
        progressBarCheckUrl = dialogView.findViewById(R.id.progressBarCheckUrl);
        final Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();;

        textViewTitalLink.setVisibility(View.GONE);
        buttonAddLink.setText("Ckeck Link");
        buttonAddLink.setEnabled(false);
        buttonAddLink.setAlpha(0.5f);
        progressBarCheckUrl.setVisibility(View.GONE);
        isUrlValide = false ;

        editTextLinkUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Patterns.WEB_URL.matcher(charSequence.toString()).matches()) {
                    buttonAddLink.setEnabled(false);
                    buttonAddLink.setAlpha(0.5f);
                } else {
                    buttonAddLink.setEnabled(true);
                    buttonAddLink.setAlpha(1.0f);                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        buttonAddLink.setOnClickListener(V->{
            if (!isUrlValide){
                buttonAddLink.setText("Ckeck Link");
                buttonAddLink.setEnabled(false);
                buttonAddLink.setAlpha(0.5f);
                progressBarCheckUrl.setVisibility(View.VISIBLE);
                checkUrl(editTextLinkUrl.getText().toString().trim());
                Toast.makeText(getContext(), "check Url", Toast.LENGTH_SHORT).show();
            }else {
                dialog.dismiss();
                Toast.makeText(getContext(), "load Url", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCancel.setOnClickListener(V->{
            dialog.dismiss();
        });

        // Show the dialog
        dialog.setCancelable(false);
        dialog.show();

    }


    private void checkUrl(String url){
        new Thread(() -> {
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
                String title = doc.title();

                runOnUiThread(() -> {
                    progressBarCheckUrl.setVisibility(View.GONE);
                    textViewTitalLink.setVisibility(View.VISIBLE);
                    textViewTitalLink.setText(title);
                    buttonAddLink.setText("Add Link");
                    buttonAddLink.setEnabled(true);
                    buttonAddLink.setAlpha(1.0f);
                    isUrlValide = true;
                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBarCheckUrl.setVisibility(View.GONE);
                    textViewTitalLink.setVisibility(View.VISIBLE);
                    textViewTitalLink.setText("Error: " + e.getMessage());
                    editTextLinkUrl.setText("");
                    buttonAddLink.setText("Ckeck Link");
                    buttonAddLink.setEnabled(false);
                    buttonAddLink.setAlpha(0.5f);
                    isUrlValide = false;
                });
            }
        }).start();

    }



    @SuppressLint("MissingInflatedId")
    private void recordAudioDialog() {

        if (recordVoiceDialog != null && recordVoiceDialog.isShowing()) return; // Don't recreate

        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_record_voice, null);

        // Get reference to the EditText in the dialog layout
        textViewTime = dialogView.findViewById(R.id.textViewTimer);
        buttonStopAndSave = dialogView.findViewById(R.id.buttonStopAndSave);
        final Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        recordVoiceDialog = builder.create();;


        bars = new View[] {
                dialogView.findViewById(R.id.bar1),
                dialogView.findViewById(R.id.bar2),
                dialogView.findViewById(R.id.bar3),
                dialogView.findViewById(R.id.bar4),
                dialogView.findViewById(R.id.bar5),
                dialogView.findViewById(R.id.bar6),
                dialogView.findViewById(R.id.bar7)
        };


        startRecording();
        startTimeMillis = System.currentTimeMillis();
        handlerTimeCounter.post(updateTimerRunnable);
        //isRunning = true;


        buttonStopAndSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
                handlerTimeCounter.removeCallbacks(updateTimerRunnable);
                recordVoiceDialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(V->{
            stopRecording();
            handlerTimeCounter.removeCallbacks(updateTimerRunnable);
            recordVoiceDialog.dismiss();
        });

        // Show the dialog
        recordVoiceDialog.setCancelable(false);
        recordVoiceDialog.show();

    }

    private void checkPermissionAndRecord() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        } else {
            recordAudioDialog();
        }
    }


    private void startRecording() {
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // 2. اختر مجلد التخزين الخارجي الخاص بالتطبيق
            //    → /storage/emulated/0/Android/data/your.package.name/files
            File baseDir = getContext().getExternalFilesDir(null);
            File recordingsDir = new File(baseDir, SAVE_FOLDER_NAME);
            if (!recordingsDir.exists()) {
                recordingsDir.mkdirs();
            }else if(!recordingsDir.isDirectory()){
                recordingsDir.delete();
                recordingsDir.mkdirs();
            }

            // 3. أنشئ اسم ملف فريد وملف الإخراج
            String fileName = "recording_" + System.currentTimeMillis() + ".3gp";
            outFile = new File(recordingsDir, fileName);
            recorder.setOutputFile(outFile.getAbsolutePath());


            Log.d(TAG, "startRecording: "+outFile.getAbsolutePath());

            recorder.setOutputFile(outFile.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            isRecording = true;
            handlerRecording.post(updateWaveform);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Recording failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            if (recorder != null) recorder.release();
            recorder = null;
        }
    }




    private void stopRecording() {
        isRecording = false;
        handlerRecording.removeCallbacks(updateWaveform);

        if (recorder != null) {
            try {
                recorder.stop();
            } catch (RuntimeException ignored) { }
            recorder.release();
            recorder = null;
        }

        // أعد الأعمدة لحجمها الطبيعي
        for (View bar : bars) {
            bar.setScaleY(1f);
        }
    }


    private final Runnable updateWaveform = new Runnable() {
        @Override
        public void run() {
            if (!isRecording) return;

            int amp = recorder.getMaxAmplitude();  // 0–32767
            float normalized = amp / 32767f;       // 0.0–1.0
            float minScale = 0.2f, maxScale = 6f;  // اضبط حسب ارتفاع المساحة
            float scale = minScale + normalized * (maxScale - minScale);

            // طبّق نفس الـ scale على كل bar
            for (View bar : bars) {
                bar.setScaleY(scale);
            }

            // كرر التحديث كل 50ms
            handlerRecording.postDelayed(this, 50);
        }
    };

    private final Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsed = System.currentTimeMillis() - startTimeMillis;
            if (elapsed >= MAX_TIME_MS) {
                //isRunning = false;

                stopRecording();
                handlerTimeCounter.removeCallbacks(updateTimerRunnable);
                recordVoiceDialog.dismiss();

                updateTimerText(MAX_TIME_MS);
                Toast.makeText(getContext(), "Reached 5 minutes", Toast.LENGTH_SHORT).show();
                return;
            }

            updateTimerText(elapsed);
            handler.postDelayed(this, 1000); // تحديث كل ثانية
        }
    };


    private void updateTimerText(long elapsedMillis) {
        int minutes = (int) (elapsedMillis / 1000) / 60;
        int seconds = (int) (elapsedMillis / 1000) % 60;
        String formatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewTime.setText(formatted);
    }





}