package com.noureddine.WriteFlow.adapter;

import static com.noureddine.WriteFlow.Utils.SubscriptionConstants.FREE_PLAN_NAME;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.Utils.DataCoverter;
import com.noureddine.WriteFlow.activities.HomeActivity;
import com.noureddine.WriteFlow.model.History;
import com.noureddine.WriteFlow.model.HistoryArticle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViweHolder>{

    List<HistoryArticle> historyList ;
    DataCoverter dataCoverter = new DataCoverter();
    OnClickListener onClickListener;

    public AdapterHistory(List<HistoryArticle> historyList,OnClickListener onClickListener) {
        this.historyList = historyList != null ? historyList : new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateHistory(List<HistoryArticle> historyList) {
        // Update the list and notify the adapter
        this.historyList = historyList != null ? historyList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterHistory.ViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viwe = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history2, parent, false);
        return new ViweHolder(viwe);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterHistory.ViweHolder holder, int position) {

        HistoryArticle historyArticle = historyList.get(position);
        holder.type.setText(historyArticle.getType());


        switch (historyArticle.getType()){
            case"Grammar Checker":
                holder.text.setText("Grammar Error Fixed : "+limitText(historyArticle.getResponse(),100));
                break;
            case"AI Detector":
                holder.text.setText("AI-generated : "+limitText(historyArticle.getResponse(),100)+" %");
                break;
            default:
                holder.text.setText(limitText(historyArticle.getResponse(),100));
                break;
        }

        holder.date.setText(dataCoverter.longToData(historyArticle.getDate()));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onItemCklick(historyArticle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViweHolder extends RecyclerView.ViewHolder {
        TextView date,text,type;
        LinearLayout linearLayout;
        public ViweHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.textView);
            text = itemView.findViewById(R.id.textView2);
            date = itemView.findViewById(R.id.textView27);
            linearLayout = itemView.findViewById(R.id.linearLayoutHistory);

        }
    }

    public String limitText(String text, int maxChars) {
        if (text == null || text.length() <= maxChars) {
            Log.d("TAG", "limitText: "+text);
            return text;
        } else {
            return text.substring(0, maxChars) + "...";
        }
    }


    public interface OnClickListener {
        void onItemCklick(HistoryArticle historyArticle);
    }


}
