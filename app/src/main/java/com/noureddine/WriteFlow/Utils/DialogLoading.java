package com.noureddine.WriteFlow.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.noureddine.WriteFlow.R;

public class DialogLoading {

    Dialog dialog;
    Context context;

    public DialogLoading(Context context) {
        this.context = context;
        dialog = new Dialog(context);
    }

    public void loadingProgressDialog(String text) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.loading, null);
        TextView textView = view.findViewById(R.id.tvMessage);
        textView.setText(text);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void showLoadingProgressDialog() {
        dialog.show();
    }

    public void dismissLoadingProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean isShowingLoadingProgressDialog() {
        return dialog != null && dialog.isShowing();
    }

}
