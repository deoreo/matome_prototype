package com.jds.webapp.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jds.webapp.Adapter.AdapterListArticle;
import com.jds.webapp.ArticleControl;
import com.jds.webapp.ArticlePersistence;
import com.jds.webapp.DataArticle;
import com.jds.webapp.Filter;
import com.jds.webapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragmentListArticle extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AdapterListArticle mAdapter;
    List<DataArticle> LIST_ARTICLE_MATOME = null;
    DataArticle article;
    Filter INI_FILTER;

    private static String KEY_RESULT = "result";
    private static final String KEY_KEY = "key";
    private static final String KEY_TITLE = "ttl";
    private static final String KEY_USR = "usr";
    private static final String KEY_AUTHOR = "nam";
    private static final String KEY_PV = "pv";
    private static final String KEY_POST_DATE = "pos";
    private static final String KEY_THUMBNAIL = "img";
    private static final String KEY_DESC = "dsc";
    private static final String KEY_WORD = "keyword";
    private String keyword;
    String fragmentTag;
    private ArticlePersistence articlePersistence;
    private static String TAG = "FragmentListAticle";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getExtras();
        articlePersistence = new ArticlePersistence(getActivity());
        //LIST_ARTICLE_MATOME = new ArrayList<DataArticle>();
        View view = inflater.inflate(R.layout.activity_fragment_list_article, container, false);
        mListView = (ListView) view.findViewById(R.id.lvArticle);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        try {
            fragmentTag = getFragmentManager().findFragmentByTag("search").getTag();
        } catch (NullPointerException e) {
            fragmentTag = "";
        }
        Log.v("FragmentArticle", fragmentTag);
        int articleCount = articlePersistence.getListSavedArticle().size();
        if (!fragmentTag.equals("search")) {
            if (articleCount <= 0)
                new GetArticle().execute();
            else
                new GetArticleFromSharedPref().execute();
        } else {

            LIST_ARTICLE_MATOME = articlePersistence.getListSavedArticle();
            new SearchArticle(keyword, LIST_ARTICLE_MATOME).execute();
        }
        return view;
    }


    @Override
    public void onRefresh() {
        new GetArticle().execute();
    }

    public class GetArticle extends AsyncTask<String, Void, JSONArray> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Retrieve Articles.Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... arg) {
            JSONArray json = null;
            LIST_ARTICLE_MATOME = new ArrayList<DataArticle>();
            ArticlePersistence persistence = new ArticlePersistence(getActivity());
            ArticleControl articleControl = new ArticleControl();
            json = articleControl.listArticle();
            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    String key = "";
                    String thumbnail = "";
                    String title = "";
                    String pv = "";
                    String pos = "";
                    String posdate = "";
                    String author = "";
                    String postcontent = "";
                    boolean status = true;
                    try {
                        JSONObject jsonObject = json.getJSONObject(i);
                        key = jsonObject.getString(KEY_KEY);
                        thumbnail = jsonObject.getString(KEY_THUMBNAIL);
                        title = jsonObject.getString(KEY_TITLE);
                        pv = jsonObject.getString(KEY_PV);
                        pos = jsonObject.getString(KEY_POST_DATE);
                        Date date = convertFormatDate(pos);
                        posdate = new SimpleDateFormat("yyyy/MM/dd").format(date);
                        JSONObject jsonObject1 = jsonObject.getJSONObject(KEY_USR);
                        author = jsonObject1.getString(KEY_AUTHOR);
                        try {
                            Document doc = Jsoup.connect("http://matome.id/" + key).get();
                            String primeDiv = "content";
                            //scrap content
                            Elements content = doc.select("div[id=" + primeDiv + "]");
                            for (Element post : content) {
                                Elements post_content = post.getElementsByClass("post-content");
                                for (Element c : post_content) {
                                    c.getElementsByClass("post-sns").remove();
                                    c.getElementsByClass("post-info").remove();
                                    c.getElementsByClass("author-line").remove();
                                    postcontent = c.text();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        status = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        status = false;
                    }
                    if (status) {
                        article = new DataArticle();
                        article.setKey(key);
                        article.setThumbnail(thumbnail);
                        article.setAuthor(author);
                        article.setTitle(title);
                        article.setDate(posdate);
                        article.setPv(pv);
                        article.setContent(postcontent);
                        LIST_ARTICLE_MATOME.add(article);
                    }
                }

                persistence.setListSavedArticle(LIST_ARTICLE_MATOME);
                mAdapter = new AdapterListArticle(getActivity(), LIST_ARTICLE_MATOME, "FragmentListArticleNoSearch");

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

    public class SearchArticle extends AsyncTask<String, Void, Filter> {
        ProgressDialog pDialog;
        public String keyword;
        public List<DataArticle> dataArticles;

        public SearchArticle(String keyword, List<DataArticle> dataArticles) {
            this.keyword = keyword;
            this.dataArticles = dataArticles;
        }

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
        protected Filter doInBackground(String... arg) {
            INI_FILTER = new Filter(dataArticles);
            mAdapter = new AdapterListArticle(getActivity(), INI_FILTER.getFilter(keyword), "FragmentListArticleWithSearch");
            return INI_FILTER;
        }

        @Override
        protected void onPostExecute(Filter str) {
            // TODO Auto-generated method stub
            super.onPostExecute(str);
            pDialog.dismiss();
            mListView.setAdapter(mAdapter);
            return;
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
            mAdapter = new AdapterListArticle(getActivity(), LIST_ARTICLE_MATOME, "FragmentListArticleWithSearch");
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

    private Date convertFormatDate(final String iso8601string) {
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        Date dateFromServer = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ");
            dateFromServer = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFromServer;
    }

    private void getExtras() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            keyword = bundle.getString(KEY_WORD);
        }
    }

}
