package com.noureddine.WriteFlow.Utils;

import android.annotation.SuppressLint;

public class NumberFormat {

    @SuppressLint("DefaultLocale")
    public String String(long number){
        if (number<999){
            return String.valueOf(number);
        }
        return String.format("%,d", number);
    }

}
