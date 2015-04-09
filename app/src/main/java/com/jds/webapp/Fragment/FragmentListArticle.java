package com.jds.webapp.Fragment;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.jds.webapp.Adapter.AdapterListArticle;
import com.jds.webapp.JSONControl;
import com.jds.webapp.ArticlePersistence;
import com.jds.webapp.DataArticle;
import com.jds.webapp.NotificationReceiver;
import com.jds.webapp.PageManager;
import com.jds.webapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class FragmentListArticle extends Fragment {
    ListView mListView;
    PullRefreshLayout mSwipeRefreshLayout;
    MaterialDialog mMaterialDialog;
    AdapterListArticle mAdapter;
    List<DataArticle> LIST_ARTICLE_MATOME = null;
    DataArticle article;
    private static final String KEY_ID = "_id";
    private static final String KEY_KEY = "key";
    private static final String KEY_TITLE = "ttl";
    private static final String KEY_USR = "usr";
    private static final String KEY_AUTHOR = "nam";
    private static final String KEY_PV = "pv";
    private static final String KEY_POST_DATE = "pos";
    private static final String KEY_THUMBNAIL = "img";
    private ArticlePersistence articlePersistence;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        articlePersistence = new ArticlePersistence(getActivity());
        View view = inflater.inflate(R.layout.fragment_list_article_coba, container, false);
        mListView = (ListView) view.findViewById(R.id.lvArticle);
        mSwipeRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetArticle().execute();
            }
        });
        mSwipeRefreshLayout.setRefreshStyle(PullRefreshLayout.MODE_BOTTOM);
        int articleCount = articlePersistence.getListSavedArticle().size();
       // if (articleCount <= 0)
            if(NetworkManager.getInstance(getActivity()).isConnectingToInternet()) {
                new GetArticle().execute();
            }
            else{
                showDialog();
            }
       // else
       //     new GetArticleFromSharedPref().execute();
        mSwipeRefreshLayout.setRefreshing(true);
        return view;
    }

    private void showDialog() {

        mMaterialDialog = new MaterialDialog(getActivity())
                .setTitle("Warning!")
                .setMessage("Internet Connection Trouble")
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();

                    }
                });

        mMaterialDialog.show();
    }

    public class GetArticle extends AsyncTask<String, Void, JSONArray> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);

        }

        @Override
        protected JSONArray doInBackground(String... arg) {
                JSONArray json = null;
                LIST_ARTICLE_MATOME = new ArrayList<DataArticle>();
                ArticlePersistence persistence = new ArticlePersistence(getActivity());
                JSONControl JSONControl = new JSONControl();
                json = JSONControl.listArticle();
                if (json != null) {
                    for (int i = 0; i < json.length(); i++) {
                        String id = "";
                        String key = "";
                        String thumbnail = "";
                        String title = "";
                        String pv = "";
                        String post = "";
                        String postdate = "";
                        String author = "";
                        boolean status = true;
                        try {
                            JSONObject jsonObject = json.getJSONObject(i);
                            id = jsonObject.getString(KEY_ID);
                            key = jsonObject.getString(KEY_KEY);
                            thumbnail = jsonObject.getString(KEY_THUMBNAIL);
                            title = jsonObject.getString(KEY_TITLE);
                            pv = jsonObject.getString(KEY_PV);
                            post = jsonObject.getString(KEY_POST_DATE);
                            Date date = PageManager.getInstance().convertFormatDate(post);
                            postdate = new SimpleDateFormat("yyyy/MM/dd").format(date);
                            JSONObject jsonObject1 = jsonObject.getJSONObject(KEY_USR);
                            author = jsonObject1.getString(KEY_AUTHOR);
                            status = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new GetArticleFromSharedPref().execute();
                            status = false;
                        }
                        if (status) {
                            article = new DataArticle();
                            article.setId(id);
                            article.setKey(key);
                            article.setThumbnail(thumbnail);
                            article.setAuthor(author);
                            article.setTitle(title);
                            article.setDate(postdate);
                            article.setPv(pv);
                            article.setContent("");
                            LIST_ARTICLE_MATOME.add(article);
                            /*if(i==0){
                                boolean isNull = false;
                                DataArticle firstArticle = null;
                                try {
                                    firstArticle = persistence.getFirstArticle();
                                    if(!firstArticle.getKey().equals(article.getKey()) && !isNull) {
                                        persistence.setFirstArticle(article);
                                        //notif

                                        Intent alarmIntent = new Intent(getActivity(), NotificationReceiver.class);
                                        alarmIntent.putExtra("title",article.getTitle());
                                        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                                        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                                        int interval = 1 * 30 * 1000;;
                                        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

                                    }
                                }
                                catch (NullPointerException e){
                                    isNull = true;
                                    persistence.setFirstArticle(article);
                                }
                            }*/
                        }
                    }

                    try {
                        persistence.setListSavedArticle(LIST_ARTICLE_MATOME);
                        mAdapter = new AdapterListArticle(getActivity(), LIST_ARTICLE_MATOME);
                    }
                    catch (NullPointerException e){
                        e.printStackTrace();
                    }

            }

            return json;
        }

        @Override
        protected void onPostExecute(final JSONArray json) {
            // TODO Auto-generated method stub
            super.onPostExecute(json);
            mListView.setAdapter(mAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public class GetArticleFromSharedPref extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        ArticlePersistence articlePersistence = new ArticlePersistence(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Load Articles...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... arg) {
            LIST_ARTICLE_MATOME = articlePersistence.getListSavedArticle();
            mAdapter = new AdapterListArticle(getActivity(), LIST_ARTICLE_MATOME);
            return "Success";
        }

        @Override
        protected void onPostExecute(final String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            mListView.setAdapter(mAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


}
