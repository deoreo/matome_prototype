package com.jds.webapp.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.jds.webapp.Adapter.AdapterCategoryArticle;
import com.jds.webapp.JSONControl;
import com.jds.webapp.ArticlePersistence;
import com.jds.webapp.DataArticle;
import com.jds.webapp.PageManager;
import com.jds.webapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    int articleCount;


    private void getExtras() {
        Bundle bundle = getArguments();
            try {
                strKategori = bundle.getString("category");
            } catch (NullPointerException e){
                e.printStackTrace();
                strKategori = "1";
            }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getExtras();
        articlePersistence = new ArticlePersistence(getActivity());
        View view = inflater.inflate(R.layout.fragment_list_article_coba, container, false);
        mListView = (ListView) view.findViewById(R.id.lvArticle);
        mSwipeRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        // listen refresh event
        mSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetArticle(strKategori).execute();
            }
        });
        mSwipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        mSwipeRefreshLayout.setRefreshing(true);

        if(strKategori.equals("1")) {articleCount = articlePersistence.getListFashionArticle().size();}
        else if(strKategori.equals("2")) {articleCount = articlePersistence.getListCosmeticsArticle().size();}
        else if(strKategori.equals("3")) {articleCount = articlePersistence.getListTravelArticle().size();}
        else if(strKategori.equals("4")) {articleCount = articlePersistence.getListBeautyArticle().size();}
        else if(strKategori.equals("5")) {articleCount = articlePersistence.getListGourmetArticle().size();}
        else if(strKategori.equals("6")) {articleCount = articlePersistence.getListGoodsArticle().size();}
        else if(strKategori.equals("7")) {articleCount = articlePersistence.getListLifeArticle().size();}
        else if(strKategori.equals("8")) {articleCount = articlePersistence.getListAppsArticle().size();}

        if (articleCount <= 0)
            new GetArticle(strKategori).execute();
        else
            new GetArticleFromSharedPref(strKategori).execute();
        return view;
    }


    public class GetArticle extends AsyncTask<String, Void, JSONArray> {
        ProgressDialog pDialog;
        String category;
        public GetArticle(String category){
            this.category = category;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... arg) {
            JSONArray json = null;
            LIST_ARTICLE_MATOME = new ArrayList<DataArticle>();
            ArticlePersistence persistence = new ArticlePersistence(getActivity());
            JSONControl JSONControl = new JSONControl();
            json = JSONControl.listCategoryArticle(category);
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
                if(category.equals("1")) {persistence.setListFashionArticle(LIST_ARTICLE_MATOME);}
                else if(category.equals("2")) {persistence.setListCosmeticsArticle(LIST_ARTICLE_MATOME);}
                else if(category.equals("3")) {persistence.setListTravelArticle(LIST_ARTICLE_MATOME);}
                else if(category.equals("4")) {persistence.setListBeautyArticle(LIST_ARTICLE_MATOME);}
                else if(category.equals("5")) {persistence.setListGourmetArticle(LIST_ARTICLE_MATOME);}
                else if(category.equals("6")) {persistence.setListGoodsArticle(LIST_ARTICLE_MATOME);}
                else if(category.equals("7")) {persistence.setListLifeArticle(LIST_ARTICLE_MATOME);}
                else if(category.equals("8")) {persistence.setListAppsArticle(LIST_ARTICLE_MATOME);}


                mAdapter = new AdapterCategoryArticle(getActivity(), LIST_ARTICLE_MATOME);

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

    public class GetArticleFromSharedPref extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        AdapterCategoryArticle mAdapter;
        String category;
        List<DataArticle> LIST_ARTICLE_MATOME_PREF = null;

        public GetArticleFromSharedPref(String category){
            this.category = category;
            switch(category) {
                case "1" :
                    LIST_ARTICLE_MATOME_PREF = articlePersistence.getListFashionArticle();
                    break;
                case "2" :
                    LIST_ARTICLE_MATOME_PREF = articlePersistence.getListCosmeticsArticle();
                    break;
                case "3" :
                    LIST_ARTICLE_MATOME_PREF = articlePersistence.getListTravelArticle();
                    break;
                case "4" :
                    LIST_ARTICLE_MATOME_PREF = articlePersistence.getListBeautyArticle();
                    break;
                case "5" :
                    LIST_ARTICLE_MATOME_PREF = articlePersistence.getListGourmetArticle();
                    break;
                case "6" :
                    LIST_ARTICLE_MATOME_PREF = articlePersistence.getListGoodsArticle();
                    break;
                case "7" :
                    LIST_ARTICLE_MATOME_PREF = articlePersistence.getListLifeArticle();
                    break;
                case "8" :
                    LIST_ARTICLE_MATOME_PREF = articlePersistence.getListAppsArticle();
                    break;
                default:
                    LIST_ARTICLE_MATOME_PREF = articlePersistence.getListFashionArticle();
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Load Articles...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //pDialog.show();
        }

        @Override
        protected String doInBackground(String... arg) {
            ArticlePersistence articlePersistence = new ArticlePersistence(getActivity());
            mAdapter = new AdapterCategoryArticle(getActivity(), LIST_ARTICLE_MATOME_PREF);
            return category;
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
