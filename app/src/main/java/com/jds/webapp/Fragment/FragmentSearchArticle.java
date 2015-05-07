package com.jds.webapp.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jds.webapp.Adapter.AdapterSearchArticle;
import com.jds.webapp.JSONControl;
import com.jds.webapp.DataArticle;
import com.jds.webapp.PageManager;
import com.jds.webapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragmentSearchArticle extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AdapterSearchArticle mAdapter;
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
    private static final String KEY_WORD = "keyword";
    private String keyword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getExtras();
        View view = inflater.inflate(R.layout.activity_fragment_list_article, container, false);
        mListView = (ListView) view.findViewById(R.id.lvArticle);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        new GetArticle(keyword).execute();
        return view;
    }

    @Override
    public void onRefresh() {
        new GetArticle(keyword).execute();
    }

    public class GetArticle extends AsyncTask<String, Void, JSONArray> {
        ProgressDialog pDialog;
        String keyword="";
        public GetArticle(String keyword){
            this.keyword = keyword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... arg) {
            JSONArray json = null;
            LIST_ARTICLE_MATOME = new ArrayList<DataArticle>();
            JSONControl JSONControl = new JSONControl();
            try {
                json = JSONControl.searchArticle(keyword);
            } catch (ConnectException e) {
                e.printStackTrace();
            }
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
                    }
                }
                mAdapter = new AdapterSearchArticle(getActivity(), LIST_ARTICLE_MATOME);
            }
            return json;
        }

        @Override
        protected void onPostExecute(final JSONArray json) {
            // TODO Auto-generated method stub
            super.onPostExecute(json);
            pDialog.dismiss();
            mListView.setAdapter(mAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void getExtras() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            keyword = bundle.getString(KEY_WORD);
        }
    }

}
