package com.noureddine.WriteFlow.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.noureddine.WriteFlow.R;
import com.noureddine.WriteFlow.model.Tool;

import java.util.ArrayList;
import java.util.List;

public class AdapterTools extends RecyclerView.Adapter<AdapterTools.ViweHolder>{

    private List<Tool> toolList = new ArrayList<>();
    private setOnClickListenerTool setOnClickListenerTool;

    public AdapterTools(List<Tool> toolList, AdapterTools.setOnClickListenerTool setOnClickListenerTool) {
        this.toolList = toolList;
        this.setOnClickListenerTool = setOnClickListenerTool;
    }

    public AdapterTools(List<Tool> toolList) {
        this.toolList = toolList;
    }

    @NonNull
    @Override
    public AdapterTools.ViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viwe = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool, parent, false);
        return new ViweHolder(viwe);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTools.ViweHolder holder, int position) {

        Tool tool = toolList.get(position);
        holder.text.setText(tool.getText());
        holder.img.setImageResource(tool.getImg());
        holder.img.setImageTintList(ColorStateList.valueOf(Color.WHITE));
//        if ( tool.getText().equals("Paragraph Generator")
//                || tool.getText().equals("Text Tools")
//                || tool.getText().equals("Plagiarism Checking") ){
//            holder.crown.setVisibility(View.VISIBLE);
//        }else {
//            holder.crown.setVisibility(View.GONE);
//        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickListenerTool.OnClick(toolList.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return toolList.size();
    }

    public class ViweHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView img,crown;
        LinearLayout linearLayout;
        public ViweHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.textView);
            img = itemView.findViewById(R.id.imageView);
            //crown = itemView.findViewById(R.id.imageViewCrown);
            linearLayout = itemView.findViewById(R.id.linearLayout);

        }
    }


    public interface setOnClickListenerTool{
        void OnClick(Tool tool);
    }


}
