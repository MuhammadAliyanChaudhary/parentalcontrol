package com.example.parentalcontrol.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialogUtils {

    private static ProgressDialog progressDialog;

    public static void showLoadingDialog(Context context) {
        dismissLoadingDialog();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
