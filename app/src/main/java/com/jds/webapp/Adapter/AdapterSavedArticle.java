package com.jds.webapp.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jds.webapp.AlertDialogManager;
import com.jds.webapp.ArticleListClickListener;
import com.jds.webapp.DataArticle;
import com.jds.webapp.DataListSavedArticle;
import com.jds.webapp.Fragment.FragmentArticle;
import com.jds.webapp.Fragment.FragmentHeaderArticle;
import com.jds.webapp.Fragment.FragmentListSavedArticle;
import com.jds.webapp.R;
import com.jds.webapp.SavedArticleHandler;
import com.jds.webapp.SavedArticleThread;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class AdapterSavedArticle extends BaseAdapter {


    private FragmentActivity mAct;
    private List<DataListSavedArticle> mSourceData;
    private LayoutInflater mInflater =null;
    private Realm realm;
    boolean isEmpty = false;

    public AdapterSavedArticle(FragmentActivity activity) {
        mAct = activity;
        mInflater = (LayoutInflater) mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        realm = Realm.getInstance(mAct);
        mSourceData = realm.where(DataListSavedArticle.class).findAll();
        if(mSourceData.isEmpty() || mSourceData==null){
            isEmpty=true;
        }
        else{
            isEmpty = false;
        }

    }

    @Override
    public int getCount() {
        if(isEmpty)
            return 1;
        return mSourceData.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(isEmpty){
            convertView = mInflater.inflate(R.layout.list_row_empty, null);
            return convertView;
        }
        else{
            convertView = mInflater.inflate(R.layout.list_saved_article, null);
            holder = new ViewHolder();
            holder.titleText = (TextView) convertView.findViewById(R.id.titleText);
            holder.dateText = (TextView) convertView.findViewById(R.id.dateText);
            holder.authorText = (TextView) convertView.findViewById(R.id.authorText);
            holder.pvText = (TextView) convertView.findViewById(R.id.pvText);
            holder.deleteArticle = (ImageButton) convertView.findViewById(R.id.btnDeleteArticle);
            holder.imgArticle = (ImageView) convertView.findViewById(R.id.imageViewSaved);
            convertView.setTag(holder);

            mSourceData.get(position);
            final String KEY = mSourceData.get(position).getKey();
            final String TITLE = mSourceData.get(position).getTitle();
            final String DATE = mSourceData.get(position).getDate();
            final String AUTHOR = mSourceData.get(position).getAuthor();
            final String PV = mSourceData.get(position).getPv();
            final String THUMBNAIL = mSourceData.get(position).getThumbnail();


            holder.titleText.setText(TITLE);
            holder.dateText.setText(DATE);
            holder.authorText.setText(AUTHOR);
            //holder.pvText.setText(PV);
            Picasso.with(mAct).load(THUMBNAIL)
                    .fit()
                    .into(holder.imgArticle);
            convertView.setOnClickListener(new ArticleListClickListener(mAct, "AdapterSavedArticle", KEY, TITLE, DATE, AUTHOR, PV, THUMBNAIL));

            holder.deleteArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("ADAPTER", "Click Delete Button - " + KEY);
                    Realm realm = Realm.getInstance(mAct);
                    SavedArticleThread savedArticleThread = new SavedArticleThread(mAct);

                    savedArticleThread.start();
                    realm.beginTransaction();
                    realm.where(DataListSavedArticle.class).equalTo("key", KEY).findAll().clear();
                    realm.commitTransaction();


                    FragmentTransaction ft = mAct.getSupportFragmentManager().beginTransaction();
                    try {
                        FragmentListSavedArticle fragmentArticle = new FragmentListSavedArticle();
                        ft.replace(R.id.container, fragmentArticle);
                        ft.addToBackStack(null);
                        ft.commit();
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView titleText;
        public TextView dateText;
        public TextView authorText;
        public TextView pvText;
        public ImageView imgArticle;
        public ImageButton deleteArticle;
    }


}
