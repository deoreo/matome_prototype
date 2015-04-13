package com.jds.webapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.afollestad.materialdialogs.MaterialDialog;


public class DialogBox {

    private static DialogBox sInstance;
    public static DialogBox getInstance() {
        if (sInstance == null) {
            sInstance = new DialogBox();
        }
        return sInstance;
    }
    public void showDialog(Context context, Drawable icon, String positive, String negative, String title, String content) {
        new MaterialDialog.Builder(context)
                .autoDismiss(true)
                .icon(icon)
                //.icon(context.getResources().getDrawable(R.drawable.news))
                .title(title)
                .content(content)
                .positiveText(positive)
                .negativeText(negative)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }})
                .show();
    }

}
