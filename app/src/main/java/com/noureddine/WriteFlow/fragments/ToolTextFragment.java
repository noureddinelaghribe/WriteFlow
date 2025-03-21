package com.noureddine.WriteFlow.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.Utils.TextProcessing;
import com.noureddine.WriteFlow.activities.HomeActivity;
import com.noureddine.WriteFlow.activities.TextToolActivity;
import com.noureddine.WriteFlow.model.HistoryArticle;
import com.noureddine.WriteFlow.viewModels.HistoryArticleViewModel;

import java.util.ArrayList;


public class ToolTextFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String toolTitle;
    TextView textView;
    EditText input,output,opirator1,opirator2;
    Button button;
    ImageView back,copy,txt,word,pdf,html;
    LinearLayout linearLayout;
    Spinner spinner;
    HistoryArticleViewModel historyArticleViewModel;
    EncryptedPrefsManager prefs;


    public ToolTextFragment() {}

    public static ToolTextFragment newInstance(String param1, String param2) {
        ToolTextFragment fragment = new ToolTextFragment();
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

        if (getArguments() != null) {
            toolTitle = getArguments().getString("textTool");
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
        linearLayout.setVisibility(View.GONE);
        output.setVisibility(View.GONE);
        textView.setText(toolTitle);
        SetupTool();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextToolActivity) getActivity()).navigateToFragment(new ListToolTextFragment());
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String operatorStr1 = opirator1.getText().toString().trim();
                String operatorStr2 = opirator2.getText().toString().trim();
                String inputStr = input.getText().toString().trim();
                ProcessText(inputStr,operatorStr1,operatorStr2);
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
                break;
            case "Insert New Line After Symbol/word":
            case "Replace New Line With Symbol/word":
                // text and 1 filter char Symbol
                opirator1.setVisibility(View.VISIBLE);
                opirator1.setHint("Symbol or Word");
                opirator2.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
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
                break;
            case "Filter Text By Word":
                // text and 1 filter string word
                opirator1.setVisibility(View.VISIBLE);
                opirator1.setHint("word");
                opirator2.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                break;
            case "Sort Text":
                // text and spinner ("length","alphabet")
                opirator1.setVisibility(View.GONE);
                opirator2.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                String []models = {"  -- By --  ","  Length  ","  Alphabet  "};
                ArrayAdapter<String> spinnerModeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<>());
                spinnerModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerModeAdapter);
                spinnerModeAdapter.addAll(models);
                spinnerModeAdapter.notifyDataSetChanged();
                break;
            case "Find and Replace Text":
                // text and and 2 filter
                opirator1.setVisibility(View.VISIBLE);
                opirator1.setHint("Find word");
                opirator2.setVisibility(View.VISIBLE);
                opirator2.setHint("Replace with");
                spinner.setVisibility(View.GONE);
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
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.reverserText(text));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Remove Duplicate Spaces":
                if (!text.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.removerSpaceDuplicateText(text));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Remove Duplicate Lines":
                if (!text.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.removerLineDuplicateText(text));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Unwrap Text":
                if (!text.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.unWrapText(text));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Remove Punctuation":
                if (!text.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.removePunctuation(text));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Remove Diacritics":
                if (!text.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.removeDiacritics(text));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Fix Text Spacing":
                if (!text.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.distanceFixerText(text));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;

            case "Insert New Line After Symbol/word":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.insertNewLineAfterSymbol(text,filter1));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Replace New Line With Symbol/word":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.replaceSymbolNewLine(text,filter1));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Repeat Text":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    try {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                        output.setText(TextProcessing.repeatText(text,Integer.parseInt(filter1)));
                        historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
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
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                        output.setText(TextProcessing.truncatorText(text,Integer.parseInt(filter1)));
                        historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
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
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                        output.setText(TextProcessing.wrapTextByWords(text,Integer.parseInt(filter1)));
                        historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
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
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                        output.setText(TextProcessing.wrapTextByCharacters(text,Integer.parseInt(filter1)));
                        historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Filter Text By Word":
                if (!text.isEmpty() && !filter1.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.filterTextByWord(text,filter1));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                }else {
                    Toast.makeText(getContext(),"Text cannot be null", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Sort Text":
                // text and spinner ("length","alphabet")
                if (spinner.getSelectedItemPosition() == 0){
                    Toast.makeText(getContext(), "Please select by.", Toast.LENGTH_SHORT).show();
                }else {
                    if (spinner.getSelectedItemPosition() == 1){
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                        output.setText(TextProcessing.sorterTextByLength(text));
                        historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        output.setVisibility(View.VISIBLE);
                        output.setText(TextProcessing.sorterTextByAlphabet(text));
                    }
                }
                break;
            case "Find and Replace Text":
                // text and and 2 filter
                if (!text.isEmpty() && !filter1.isEmpty() && !filter2.isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                    output.setVisibility(View.VISIBLE);
                    output.setText(TextProcessing.findandReplaceText(text,filter1,filter2));
                    historyArticleViewModel.insertArticle(new HistoryArticle( prefs.getUser().getUid(), output.getText().toString(), toolTitle, text, System.currentTimeMillis()));
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


    }




}