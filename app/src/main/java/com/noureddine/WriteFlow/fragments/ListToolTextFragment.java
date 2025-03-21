package com.noureddine.WriteFlow.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.activities.TextToolActivity;
import com.noureddine.WriteFlow.adapter.AdapterTextTool;
import com.noureddine.WriteFlow.model.TextTool;

import java.util.ArrayList;
import java.util.List;


public class ListToolTextFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    List<TextTool> textTools = new ArrayList<>() ;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    public ListToolTextFragment() {}

    public static ListToolTextFragment newInstance(String param1, String param2) {
        ListToolTextFragment fragment = new ListToolTextFragment();
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
        addTool();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_tool_text,container,false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AdapterTextTool(textTools, new AdapterTextTool.setOnClickListenerTextTool() {
            @Override
            public void OnClick(TextTool textTool) {

                Bundle args = new Bundle();
                args.putString("textTool", textTool.getName());

                ((TextToolActivity) getActivity()).navigateToFragment(new ToolTextFragment(),args);
            }
        });
        recyclerView.setAdapter(adapter);

        return v;
    }

    public void addTool(){

        textTools.add(new TextTool("Insert New Line After Symbol/word", "Inserts new line after each occurrence of the given symbol for formatting."));
        textTools.add(new TextTool("Replace New Line With Symbol/word", "Replaces new line characters with the specified symbol for consistent layout."));
        textTools.add(new TextTool("Repeat Text", "Repeats the provided text multiple times to create repeated patterns."));
        textTools.add(new TextTool("Reverse Text", "Reverses the order of characters, producing a mirrored text output."));
        textTools.add(new TextTool("Truncate Text", "Extracts the first N characters from the text for concise previews."));
        textTools.add(new TextTool("Filter Text By Word", "Returns paragraphs containing the specified word, case-insensitively, for effective filtering."));
        textTools.add(new TextTool("Sort Text", "Sorts paragraphs either alphabetically or by length for organized presentation."));
        textTools.add(new TextTool("Remove Duplicate Lines", "Eliminates consecutive new line characters, condensing text into a single line break."));
        textTools.add(new TextTool("Remove Duplicate Spaces", "Replaces multiple spaces with one, ensuring consistent spacing throughout the text."));
        textTools.add(new TextTool("Wrap Text By Words", "Inserts line breaks after a fixed number of characters, wrapping text smartly."));
        textTools.add(new TextTool("Wrap Text By Characters", "Inserts new lines after a specific character count to maintain text width."));
        textTools.add(new TextTool("Unwrap Text", "Removes all new line characters, consolidating text into a continuous block."));
        textTools.add(new TextTool("Fix Text Spacing", "Normalizes spacing and line breaks for a cleaner, uniformly formatted text."));
        textTools.add(new TextTool("Find and Replace Text", "Searches and substitutes text segments using regex for dynamic modifications."));
        textTools.add(new TextTool("Remove Punctuation", "Strips out punctuation characters completely to simplify text for analysis."));
        textTools.add(new TextTool("Remove Diacritics", "Eliminates diacritical marks from text, enhancing text uniformity for processing."));

    }

}