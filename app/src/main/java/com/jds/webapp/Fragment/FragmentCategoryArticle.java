package com.jds.webapp.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baoyz.widget.PullRefreshLayout;
import com.jds.webapp.Adapter.AdapterCategoryArticle;
import com.jds.webapp.DialogBox;
import com.jds.webapp.JSONControl;
import com.jds.webapp.ArticlePersistence;
import com.jds.webapp.DataArticle;
import com.jds.webapp.NetworkManager;
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


public class FragmentCategoryArticle extends Fragment {
    ListView mListView;
    PullRefreshLayout mSwipeRefreshLayout;
    AdapterCategoryArticle mAdapter;
    List<DataArticle> LIST_ARTICLE_MATOME = null;
    List<DataArticle> LIST_ARTICLE_MATOME_PREF = null;
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
    public String strKategori = "1";
    private int articleCount;
    private View btnFashion, btnCosmetics, btnTravel, btnBeauty, btnGourmet, btnGoods, btnLife, btnApps;
    private LayoutInflater mInflater;
    private ViewGroup mContainer;
    private void getExtras() {
        try {
            Bundle bundle = getArguments();
            strKategori = bundle.getString("category");
        } catch (NullPointerException e) {
            e.printStackTrace();
            strKategori = "1";
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getExtras();
        mInflater = inflater;
        mContainer = container;
        articlePersistence = new ArticlePersistence(getActivity());
        View view = inflater.inflate(R.layout.fragment_list_article_coba, container, false);


        mListView = (ListView) view.findViewById(R.id.lvArticle);
        mListView.setSelection(PageManager.getInstance().rowCategory);
        mSwipeRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        // listen refresh event
        mSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkManager.getInstance(getActivity()).isConnectingToInternet()) {
                    new GetArticle(strKategori).execute();
                } else {
                    DialogBox.getInstance().showDialog(getActivity(), null, "OK", "", "Warning", "Internet Connection Trouble!");
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mSwipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        mSwipeRefreshLayout.setRefreshing(true);
        articleCount = articlePersistence.getListCategoryArticle(strKategori).size();
        if (NetworkManager.getInstance(getActivity()).isConnectingToInternet()) {
            if (articleCount <= 0) {
                new GetArticle(strKategori).execute();
            } else {
                new GetArticleFromSharedPref(strKategori).execute();
            }
        }
        else{
            DialogBox.getInstance().showDialog(getActivity(), null, "OK", "", "Warning", "Internet Connection Trouble!");
            mSwipeRefreshLayout.setRefreshing(false);
        }
        return view;
    }







    public class GetArticle extends AsyncTask<String, Void, JSONArray> {
        String category;

        public GetArticle(String category) {
            this.category = category;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(String... arg) {
            JSONArray json = null;
            LIST_ARTICLE_MATOME = new ArrayList<DataArticle>();
            ArticlePersistence persistence = new ArticlePersistence(getActivity());
            JSONControl JSONControl = new JSONControl();
            try {
                json = JSONControl.listCategoryArticle(category);
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
                persistence.setListCategoryArticle(category, LIST_ARTICLE_MATOME);
                try {
                    mAdapter = new AdapterCategoryArticle(getActivity(), LIST_ARTICLE_MATOME);
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
            mListView.setSelection(PageManager.getInstance().rowCategory);
            mListView.setAdapter(mAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
            //PageManager.getInstance().setIsButtonCategoryEnable(true);
        }
    }

    public class GetArticleFromSharedPref extends AsyncTask<String, Void, String> {
        AdapterCategoryArticle mAdapter;
        String category;
        List<DataArticle> LIST_ARTICLE_MATOME_PREF = null;

        public GetArticleFromSharedPref(String category) {
            this.category = category;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg) {
            try {
                LIST_ARTICLE_MATOME_PREF = articlePersistence.getListCategoryArticle(category);
                mAdapter = new AdapterCategoryArticle(getActivity(), LIST_ARTICLE_MATOME_PREF);
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            return category;
        }

        @Override
        protected void onPostExecute(final String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mListView.setAdapter(mAdapter);
            mListView.setSelection(PageManager.getInstance().rowCategory);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


}
