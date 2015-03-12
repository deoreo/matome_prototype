package com.jds.webapp.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jds.webapp.Adapter.AdapterListArticle;
import com.jds.webapp.ArticleControl;
import com.jds.webapp.ArticlePersistence;
import com.jds.webapp.DataArticle;
import com.jds.webapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragmentListArticle extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AdapterListArticle mAdapter;
    List<DataArticle> mListArticle = null;
    DataArticle article ;
    private static String KEY_RESULT = "result";
    private static final String KEY_KEY = "key";
    private static final String KEY_TITLE = "ttl";
    private static final String KEY_USR = "usr";
    private static final String KEY_AUTHOR = "nam";
    private static final String KEY_PV = "pv";
    private static final String KEY_POST_DATE = "pos";
    private static final String KEY_THUMBNAIL = "img";
    private static final String KEY_DESC = "dsc";

    private static String TAG = "FragmentListAticle";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_list_article, container, false);
        mListView = (ListView)view.findViewById(R.id.lvArticle);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        new GetArticle().execute();
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
            pDialog.setMessage("Load Articles...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... arg) {
            JSONArray json = null;

            mListArticle = new ArrayList<DataArticle>();
            ArticleControl articleControl = new ArticleControl();
            json = articleControl.listArticle();
            return json;
        }

        @Override
        protected void onPostExecute(final JSONArray json) {
            // TODO Auto-generated method stub
            super.onPostExecute(json);
            pDialog.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);

            ArticlePersistence articlePersistence = new ArticlePersistence(getActivity().getApplicationContext());
            if (json != null) {

                    for(int i=0; i<json.length(); i++){
                        String key="";
                        String thumbnail = "";
                        String title = "";
                        String pv = "";
                        String pos = "";
                        String posdate = "";
                        String author = "";
                        boolean status=true;
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
                            status = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            status = false;
                        }
                        if(status) {
                            article = new DataArticle();
                            article.setKey(key);
                            article.setThumbnail(thumbnail);
                            article.setAuthor(author);
                            article.setTitle(title);
                            article.setDate(posdate);
                            article.setPv(pv);
                            mListArticle.add(article);
                        }
                    }
                    mAdapter = new AdapterListArticle(getActivity(), mListArticle);
                    mListView.setAdapter(mAdapter);


            } else {

            }
            return;
        }
    }

    private Date convertFormatDate(final String iso8601string){
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


}
