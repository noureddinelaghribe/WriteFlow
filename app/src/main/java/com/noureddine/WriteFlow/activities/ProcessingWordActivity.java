package com.noureddine.WriteFlow.activities;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.fragments.ProcessingWordsFragment;
import com.noureddine.WriteFlow.model.GrammarChecker;
import com.noureddine.WriteFlow.model.HistoryArticle;

public class ProcessingWordActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_processing_word);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Bundle extras = getIntent().getExtras();
        if (savedInstanceState == null) {
            if (extras != null) {
                ProcessingWordsFragment processingWordsFragment = new ProcessingWordsFragment();
                processingWordsFragment.setArguments(extras);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.processing_word_container, processingWordsFragment)
                        .commit();
//                if (extras.get("HistoryArticle") instanceof HistoryArticle){
//
//                    switch (((HistoryArticle) extras.get("HistoryArticle")).getType()){
//
//                        case"Paraphraser / Rewriting":
//                        case"Grammar Checker":
//                        case"AI Detector":
//                        case"Paragraph Generator":
//                        case"Summarizer":
//                            ProcessingWordsFragment processingWordsFragment = new ProcessingWordsFragment();
//                            processingWordsFragment.setArguments(extras);
//                            getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.processing_word_container, processingWordsFragment)
//                                    .commit();
//                            break;
//                        default:
//                            //tool text activity
//                            break;
//                    }
//
//                }else {
//                    ProcessingWordsFragment processingWordsFragment = new ProcessingWordsFragment();
//                    processingWordsFragment.setArguments(extras);
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.processing_word_container, processingWordsFragment)
//                            .commit();
//                }
            }
        }



    }


    // دالة لتبديل الـ Fragment الحالي بآخر جديد
    public void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.processing_word_container, fragment)
                .addToBackStack(null) // إضافة العملية إلى BackStack للسماح بالرجوع
                .commit();
    }

    public void toHome(){
        Intent intent = new Intent(ProcessingWordActivity.this, HomeActivity.class);
        startActivity(intent);
    }



}