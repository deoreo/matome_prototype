package com.jds.webapp.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.jds.webapp.Adapter.AdapterSavedArticle;
import com.jds.webapp.DataListSavedArticle;
import com.jds.webapp.DialogBox;
import com.jds.webapp.NetworkManager;
import com.jds.webapp.R;
import com.jds.webapp.SavedArticleThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmResults;


public class FragmentListSavedArticle extends Fragment {
    ListView mListView;
    PullRefreshLayout mSwipeRefreshLayout;
    AdapterSavedArticle mAdapter;
    RealmResults<DataListSavedArticle> mListArticle;
    private FragmentActivity mAct;
    public SavedArticleThread savedArticleThread;

    private static String TAG = "FragmentListAticle";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAct = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_saved_article, container, false);
        mSwipeRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.activity_saved_swipe_refresh_layout);
        mListView = (ListView)view.findViewById(R.id.lvSavedArticle);
        mSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkManager.getInstance(getActivity()).isConnectingToInternet()) {
                    DialogBox.getInstance().showDialog(getActivity(), null, "OK", "", "Warning", "Internet Connection Trouble!");
                }
                new GetListSavedArticle(mAct, savedArticleThread).execute();
            }
        });

        return view;
    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(Build.VERSION.SDK_INT>10) {
            savedArticleThread = new SavedArticleThread(getActivity());
            savedArticleThread.start();
        }
    }

    public class GetListSavedArticle extends AsyncTask<Void, Void, String> {
        private FragmentActivity fragmentActivity;
        private SavedArticleThread savedArticleThread;

        public GetListSavedArticle(FragmentActivity fragmentActivity, SavedArticleThread savedArticleThread){
            this.fragmentActivity = fragmentActivity;
            this.savedArticleThread = savedArticleThread;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);

        }

        @Override
        protected  String doInBackground(Void... arg) {
            return "result";
        }

        @Override
        protected void onPostExecute(final String results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            mAdapter = new AdapterSavedArticle(fragmentActivity);
            mListView.setAdapter(mAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
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
