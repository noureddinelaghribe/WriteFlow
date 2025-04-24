package com.noureddine.WriteFlow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.model.TextTool;

import java.util.ArrayList;
import java.util.List;

public class AdapterTextTool extends RecyclerView.Adapter<AdapterTextTool.ViweHolder>{

    private List<TextTool> toolList = new ArrayList<>();
    private setOnClickListenerTextTool setOnClickListenerTextTool;

    public AdapterTextTool(List<TextTool> toolList, AdapterTextTool.setOnClickListenerTextTool setOnClickListenerTextTool) {
        this.toolList = toolList;
        this.setOnClickListenerTextTool = setOnClickListenerTextTool;
    }

    @NonNull
    @Override
    public AdapterTextTool.ViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viwe = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_tool, parent, false);
        return new ViweHolder(viwe);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTextTool.ViweHolder holder, int position) {

        TextTool textTool = toolList.get(position);
        holder.name.setText(textTool.getName());
        holder.description.setText(textTool.getDescription());
        holder.icon.setText(String.valueOf(textTool.getName().charAt(1)).toUpperCase());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickListenerTextTool.OnClick(textTool);
            }
        });

    }

    @Override
    public int getItemCount() {
        return toolList.size();
    }

    public class ViweHolder extends RecyclerView.ViewHolder {
        TextView name,description,icon;
        LinearLayout linearLayout;
        public ViweHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.textViewIcon);
            name = itemView.findViewById(R.id.textView);
            description = itemView.findViewById(R.id.textView2);
            linearLayout = itemView.findViewById(R.id.linearLayout);

        }
    }


    public interface setOnClickListenerTextTool{
        void OnClick(TextTool textTool);
    }

}
