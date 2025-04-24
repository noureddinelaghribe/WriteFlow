package com.noureddine.WriteFlow.fragments;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.noureddine.WriteFlow.activities.ProcessingWordActivity;
import com.noureddine.WriteFlow.activities.TextToolActivity;
import com.noureddine.WriteFlow.adapter.AdapterHistory;
import com.noureddine.WriteFlow.model.History;
import com.noureddine.WriteFlow.model.HistoryArticle;
import com.noureddine.WriteFlow.model.User;
import com.noureddine.WriteFlow.viewModels.HistoryArticleViewModel;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    private ViewPager2 viewPager;
    private LinearLayout linearLayoutUpgrade ;
    private Button upgradeButton ;
    private RecyclerView recyclerView ;
    private EncryptedPrefsManager prefs;
    private User user;
    private HistoryArticleViewModel historyArticleViewModel;

    public HistoryFragment() {}

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
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
        View v = inflater.inflate(R.layout.fragment_history,container,false);

        linearLayoutUpgrade = v.findViewById(R.id.linearLayoutUpgrade);
        upgradeButton = v.findViewById(R.id.button3);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        prefs = EncryptedPrefsManager.getInstance(getContext());
        historyArticleViewModel = new HistoryArticleViewModel(getActivity().getApplication());
        initUI();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewPager = requireActivity().findViewById(R.id.viwepager);
                viewPager.setCurrentItem(0, true);
            }
        });

        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        if (user.getMembership().equals(FREE_PLAN_NAME)){
            linearLayoutUpgrade.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            linearLayoutUpgrade.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            historyArticleViewModel.getAllUsers(prefs.getUser().getUid()).observe(getViewLifecycleOwner(), new Observer<List<HistoryArticle>>() {
                @Override
                public void onChanged(List<HistoryArticle> historyArticles) {
                    AdapterHistory adapter = new AdapterHistory(historyArticles, new AdapterHistory.OnClickListener() {
                        @Override
                        public void onItemCklick(HistoryArticle historyArticle) {
                            Intent intent;
                            switch (historyArticle.getType()){
                                case"Paraphraser / Rewriting":
                                case"Grammar Checker":
                                case"AI Detector":
                                case"Paragraph Generator":
                                case"Summarizer":
                                    intent = new Intent(getContext(), ProcessingWordActivity.class);
                                    intent.putExtra("HistoryArticle",historyArticle);
                                    break;
                                default:
                                    intent = new Intent(getContext(), TextToolActivity.class);
                                    intent.putExtra("HistoryArticle",historyArticle);
                                    break;
                            }
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    adapter.updateHistory(historyArticles);
                }
            });

        }

    }


}

