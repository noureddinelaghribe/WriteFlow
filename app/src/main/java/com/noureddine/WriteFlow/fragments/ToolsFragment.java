package com.noureddine.WriteFlow.fragments;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.activities.HomeActivity;
import com.noureddine.WriteFlow.activities.ProcessingWordActivity;
import com.noureddine.WriteFlow.activities.TextToolActivity;
import com.noureddine.WriteFlow.adapter.AdapterTools;
import com.noureddine.WriteFlow.model.Tool;
import com.noureddine.WriteFlow.model.User;

import java.util.ArrayList;
import java.util.List;


public class ToolsFragment extends Fragment {

    List<Tool> toolList ;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ViewPager2 viewPager;
    LinearLayout linearLayoutUpgrade,linearLayoutPremium ;
    Button upgradeButton ;
    EncryptedPrefsManager prefs;
    User user;



    public ToolsFragment() {}

    public static ToolsFragment newInstance() {
        ToolsFragment fragment = new ToolsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tools,container,false);

        linearLayoutUpgrade = v.findViewById(R.id.linearLayoutUpgrade);
        linearLayoutPremium = v.findViewById(R.id.linearLayoutPremium);
        upgradeButton = v.findViewById(R.id.button3);
        recyclerView = v.findViewById(R.id.recyclerView);

        prefs = EncryptedPrefsManager.getInstance(getContext());
        initUI();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        toolList = new ArrayList<>();
        toolList.add(new Tool(R.drawable.paraphraser,"Paraphraser / Rewriting"));
        toolList.add(new Tool(R.drawable.grammar,"Grammar Checker"));
        toolList.add(new Tool(R.drawable.robot,"AI Detector"));
        //toolList.add(new Tool(R.drawable.translation,"Translation"));
        toolList.add(new Tool(R.drawable.writing,"Paragraph Generator"));
        toolList.add(new Tool(R.drawable.summarizer,"Summarizer"));
        toolList.add(new Tool(R.drawable.text,"Text Tools"));

        // Get the ViewPager2 from the activity
        HomeActivity activity = (HomeActivity) getActivity();
        viewPager = activity.getViewPager2(); // You'll need to create this method
        viewPager.setCurrentItem(0, true); // true for smooth scroll animation

        adapter = new AdapterTools(toolList, new AdapterTools.setOnClickListenerTool() {
            @Override
            public void OnClick(Tool tool) {
                Intent intent;
                if (tool.getText().equals("Text Tools")){
                    intent = new Intent(getContext(), TextToolActivity.class);
                }else {
                    intent = new Intent(getContext(), ProcessingWordActivity.class);
                    intent.putExtra("type",tool.getText());
                }
                getContext().startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Inside your fragment
                viewPager = requireActivity().findViewById(R.id.viwepager);
                viewPager.setCurrentItem(2, true);

            }
        });

        return v;
    }



    @Override
    public void onResume() {
        super.onResume();

        initUI();

    }


    private void initUI() {
        user = prefs.getUser();
        if (!user.getMembership().equals(FREE_PLAN_NAME)){
            linearLayoutUpgrade.setVisibility(View.GONE);
            linearLayoutPremium.setVisibility(View.VISIBLE);
        }else {
            linearLayoutUpgrade.setVisibility(View.VISIBLE);
            linearLayoutPremium.setVisibility(View.GONE);
        }
    }


    }
