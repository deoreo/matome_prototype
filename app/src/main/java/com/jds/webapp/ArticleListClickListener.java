package com.jds.webapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jds.webapp.Fragment.FragmentArticle;
import com.jds.webapp.Fragment.FragmentHeaderArticle;
import com.jds.webapp.Fragment.FragmentTop;


public class ArticleListClickListener implements View.OnClickListener{
    private FragmentActivity mActivity;

    private DataArticle dataArticle;
    private String key, title, date, author, pv, thumbnail;
    private String fromAdapter;
    private View topLayout;

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

        if(fromAdapter.equals("AdapterListArticle")) {
            PageManager.getInstance().fromFragment = "FragmentHome";
        }
        else if(fromAdapter.equals("AdapterSavedArticle")) {
            PageManager.getInstance().fromFragment = "FragmentSaved";
        }
        else if(fromAdapter.equals("AdapterSearchArticle")) {
            PageManager.getInstance().fromFragment = "FragmentSearch";
        }
        else if(fromAdapter.equals("AdapterCategoryArticle")) {
            PageManager.getInstance().fromFragment = "FragmentNav";
        }

        try {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            FragmentArticle fragmentArticle = new FragmentArticle();
            FragmentHeaderArticle fragmentHeaderArticle = new FragmentHeaderArticle();

            fragmentArticle.setArguments(args);
            fragmentHeaderArticle.setArguments(args);

            ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
            ft.replace(R.id.container, fragmentArticle);
            ft.replace(R.id.headerLayout,fragmentHeaderArticle);
            ft.addToBackStack(null);
            ft.commit();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

}
