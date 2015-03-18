package com.jds.webapp.Fragment;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.jds.webapp.AlertDialogManager;
import com.jds.webapp.DataListSavedArticle;
import com.jds.webapp.R;
import com.jds.webapp.SavedArticleThread;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmResults;


public class FragmentMain extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fragment_main);
        if (savedInstanceState == null) {

            FragmentListArticle fragmentListArticle = new FragmentListArticle();
            Bundle args = new Bundle();
            args.putString("keyword", "");
            fragmentListArticle.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragmentListArticle)
                    .commit();
            /*
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.topLayout, new FragmentTop())
                    .commit();
                    */

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.headerLayout, new FragmentHeaderMain())
                    .commit();


        }
    }

}
