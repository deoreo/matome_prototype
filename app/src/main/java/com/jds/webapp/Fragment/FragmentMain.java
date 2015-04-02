package com.jds.webapp.Fragment;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Window;
import com.jds.webapp.PageManager;
import com.jds.webapp.R;


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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.headerLayout, new FragmentHeaderMain())
                    .commit();
            PageManager.getInstance().fromFragment = "FragmentMain";

        }
    }



}
