package com.noureddine.WriteFlow.model;

import androidx.fragment.app.Fragment;

public class MyTab {

    private String TabName;
    private Fragment fragment;

    public MyTab(String tabName, Fragment fragment) {
        TabName = tabName;
        this.fragment = fragment;
    }

    public String getTabName() {
        return TabName;
    }

    public void setTabName(String tabName) {
        TabName = tabName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
