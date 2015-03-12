package com.jds.webapp.Fragment;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jds.webapp.DataListSavedArticle;
import com.jds.webapp.R;
import com.jds.webapp.SavedArticleHandler;
import com.jds.webapp.SavedArticleThread;

import io.realm.Realm;
import io.realm.RealmQuery;


public class FragmentHeaderArticle extends Fragment {
    View btn1, btn2;
    View mVw;
    private Realm realm;
    public SavedArticleThread savedArticleThread;
    private String key, title, date, author, pv;
    Drawable drawableSave;
    Resources res;
    int identifierSave;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.getExtras();
        realm = Realm.getInstance(getActivity());
        res = getResources();
        identifierSave = res.getIdentifier("save",null,null);
        drawableSave = res.getDrawable(R.drawable.save);
        RealmQuery<DataListSavedArticle> query = realm.where(DataListSavedArticle.class);


        View view = inflater.inflate(R.layout.activity_fragment_header_article, container, false);
        if (container == null) {
        }
        btn1 = view.findViewById(R.id.btnShare);
        btn2 = view.findViewById(R.id.btnSave);
        if(query.equalTo("key", key).count() > 0){
            btn2.setEnabled(false);
            int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                btn2.setBackgroundResource(identifierSave);
            } else {
                btn2.setBackground(drawableSave);
            }
        }
        else btn2.setEnabled(true);

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("HeaderSave", "Save Article");
                Message message = buildMessage(SavedArticleHandler.ADD_ARTICLE,key,title,date,author,pv );
                savedArticleThread.handler.sendMessage(message);
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btn2.setBackgroundResource(identifierSave);
                } else {
                    btn2.setBackground(drawableSave);
                }
                btn2.setEnabled(false);
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVw = getView();
        savedArticleThread = new SavedArticleThread(getActivity());
        savedArticleThread.start();
    }

    private void getExtras() {
        Bundle bundle = getArguments();
        key = bundle.getString("key");
        title = bundle.getString("title");
        date = bundle.getString("date");
        author = bundle.getString("author");
        pv = bundle.getString("pv");
    }

    private static Message buildMessage(int action,String key,String title, String date, String author, String pv) {
        Bundle bundle = new Bundle(2);
        bundle.putInt(SavedArticleHandler.ACTION, action);
        bundle.putString("key", key);
        bundle.putString("title", title);
        bundle.putString("date", date);
        bundle.putString("author", author);
        bundle.putString("pv", pv);
        Message message = new Message();
        message.setData(bundle);
        return message;
    }
}
