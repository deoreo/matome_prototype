package com.jds.webapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jds.webapp.Adapter.AdapterSavedArticle;
import com.jds.webapp.ArticlePersistence;
import com.jds.webapp.DataArticle;
import com.jds.webapp.DataListSavedArticle;
import com.jds.webapp.R;
import com.jds.webapp.SavedArticleHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


public class FragmentListSavedArticle extends Fragment {
    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AdapterSavedArticle mAdapter;
    RealmResults<DataListSavedArticle> mListArticle;
    private FragmentActivity mAct;

    private static String TAG = "FragmentListAticle";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAct = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_saved_article, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_saved_swipe_refresh_layout);
        mListView = (ListView)view.findViewById(R.id.lvSavedArticle);
        mAdapter = new AdapterSavedArticle(mAct);
        mListView.setAdapter(mAdapter);
        return view;
    }


    public class GetListSavedArticle extends AsyncTask<Void, Void, AdapterSavedArticle> {
        ProgressDialog pDialog;
        private FragmentActivity fragmentActivity;

        public GetListSavedArticle(FragmentActivity fragmentActivity){
            this.fragmentActivity = fragmentActivity;
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
        protected  AdapterSavedArticle doInBackground(Void... arg) {

            return mAdapter;
        }

        @Override
        protected void onPostExecute(final AdapterSavedArticle results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            pDialog.dismiss();

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
