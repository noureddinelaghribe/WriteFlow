package com.noureddine.WriteFlow.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.noureddine.WriteFlow.model.MyTab;

import java.util.ArrayList;

public class AdapterPager extends FragmentStateAdapter {

    private ArrayList<MyTab> list = new ArrayList<MyTab>();

    public AdapterPager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addTab(MyTab tab) {
        list.add(tab);
        notifyDataSetChanged();
    }

    public String getTabName(int position){
        return list.get(position).getTabName();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position).getFragment();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
























}
