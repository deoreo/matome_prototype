package com.jds.webapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jds.webapp.Fragment.FragmentArticle;
import com.jds.webapp.Fragment.FragmentHeaderArticle;


public class ArticleListClickListener implements View.OnClickListener{
    private FragmentActivity mActivity;

    private DataArticle dataArticle;
    private String key, title, date, author, pv, thumbnail;
    private String fromAdapter;

    public ArticleListClickListener(FragmentActivity activity, String fromAdapter, String key,String title, String date, String author, String pv, String thumbnail){
        mActivity = activity;
        this.fromAdapter = fromAdapter;
        this.key = key;
        this.title = title;
        this.date = date;
        this.author = author;
        this.pv = pv;
        this.thumbnail = thumbnail;
    }

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putString("key", key);
        args.putString("title", title);
        args.putString("date", date);
        args.putString("author", author);
        args.putString("pv", pv);
        args.putString("thumbnail", thumbnail);
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        if(fromAdapter.equals("AdapterListArticle")) {
            PageManager.getInstance().fromFragment = "FragmentHome";
        }
        else if(fromAdapter.equals("AdapterSavedArticle")) {
            PageManager.getInstance().fromFragment = "FragmentSaved";
        }

        try {
            FragmentArticle fragmentArticle = new FragmentArticle();
            FragmentHeaderArticle fragmentHeaderArticle = new FragmentHeaderArticle();
            fragmentArticle.setArguments(args);
            fragmentHeaderArticle.setArguments(args);

            ft.replace(R.id.container, fragmentArticle);
            ft.replace(R.id.headerLayout,fragmentHeaderArticle);

            ft.addToBackStack(null);
            ft.commit();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

}
