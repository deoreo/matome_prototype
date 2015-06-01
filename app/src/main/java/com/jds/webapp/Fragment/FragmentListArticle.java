package com.jds.webapp.Fragment;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jds.webapp.Adapter.AdapterListArticle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class FragmentListArticle extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        int articleCount = articlePersistence.getListSavedArticle().size();
        if (!NetworkManager.getInstance(getActivity()).isConnectingToInternet()) {
            DialogBox.getInstance().showDialog(getActivity(), null, "OK", "", "Warning", "Internet Connection Trouble!");
            mListView.setAdapter(null);
        }
        new GetArticle().execute();
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

    @Override
    public void onRefresh() {
        if (!NetworkManager.getInstance(getActivity()).isConnectingToInternet()) {
            DialogBox.getInstance().showDialog(getActivity(), null, "OK", "", "Warning", "Internet Connection Trouble!");
        }
        new GetArticle().execute();
    }

    public class GetArticle extends AsyncTask<String, Void, JSONArray> {
        ProgressDialog pDialog;

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
                json = JSONControl.listArticle(getActivity());
            } catch (Exception e) {
//                DialogBox.getInstance().showDialog(getActivity(),null,"","OK","Warning", "Please reload");
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
                    }
                }
                try {
                    persistence.setListSavedArticle(LIST_ARTICLE_MATOME);
                    mAdapter = new AdapterListArticle(getActivity(), LIST_ARTICLE_MATOME);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            else{
                LIST_ARTICLE_MATOME = null;
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
