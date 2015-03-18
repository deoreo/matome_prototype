package com.jds.webapp.Fragment;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.UiLifecycleHelper;
import com.jds.webapp.DataListSavedArticle;
import com.jds.webapp.R;
import com.jds.webapp.SavedArticleHandler;
import com.jds.webapp.SavedArticleThread;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;


public class FragmentHeaderArticle extends Fragment {
    private View btnFacebook, btnTwitter, btnShareOther, btnSave;
    private View mVw;
    private Realm realm;
    private SavedArticleThread savedArticleThread;
    private String key, title, date, author, pv, thumbnail;
    private Drawable drawableSave;
    private Resources res;
    private int identifierSave;
    private String urlMatome = "http://matome.id/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.getExtras();
        res = getResources();
        identifierSave = res.getIdentifier("save", null, null);
        drawableSave = res.getDrawable(R.drawable.save);
        realm = Realm.getInstance(getActivity());
        RealmQuery<DataListSavedArticle> query = realm.where(DataListSavedArticle.class);
        View view = inflater.inflate(R.layout.activity_fragment_header_article, container, false);

        btnFacebook = view.findViewById(R.id.btnShareFacebook);
        btnTwitter = view.findViewById(R.id.btnShareTwitter);
        btnShareOther = view.findViewById(R.id.btnShareOther);
        btnSave = view.findViewById(R.id.btnSave);
        if (query.equalTo("key", key).count() > 0) {
            btnSave.setEnabled(false);
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                btnSave.setBackgroundResource(identifierSave);
            } else {
                btnSave.setBackground(drawableSave);
            }
        } else btnSave.setEnabled(true);

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url_article = urlMatome + key;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, url_article);

                // See if official Facebook app is found
                boolean facebookAppFound = false;
                List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                        intent.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }

                if (!facebookAppFound) {
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url_article + "&t=" + title;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }

                startActivity(intent);

            }
        });
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url_article = urlMatome + key;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, url_article);
                String sharerUrl = "https://twitter.com/intent/tweet?text=" + title + "%20-%20" + url_article + "&via=matome_id";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));

                startActivity(intent);

            }
        });
        btnShareOther.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url_article = "http://m.matome.id/" + key;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, title + " - " + url_article);
                startActivity(intent);

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("HeaderSave", "Save Article");
                Message message = buildMessage(SavedArticleHandler.ADD_ARTICLE, key, title, date, author, pv, thumbnail);
                savedArticleThread.handler.sendMessage(message);
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnSave.setBackgroundResource(identifierSave);
                } else {
                    btnSave.setBackground(drawableSave);
                }
                btnSave.setEnabled(false);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
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
        thumbnail = bundle.getString("thumbnail");
    }

    private static Message buildMessage(int action, String key, String title, String date, String author, String pv, String thumbnail) {
        Bundle bundle = new Bundle(2);
        bundle.putInt(SavedArticleHandler.ACTION, action);
        bundle.putString("key", key);
        bundle.putString("title", title);
        bundle.putString("date", date);
        bundle.putString("author", author);
        bundle.putString("pv", pv);
        bundle.putString("thumbnail", thumbnail);
        Message message = new Message();
        message.setData(bundle);
        return message;
    }


}
