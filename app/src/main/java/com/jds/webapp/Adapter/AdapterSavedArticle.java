package com.jds.webapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jds.webapp.ArticleListClickListener;
import com.jds.webapp.DataArticle;
import com.jds.webapp.DataListSavedArticle;
import com.jds.webapp.R;

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

    public AdapterSavedArticle(FragmentActivity activity) {
        mAct = activity;
        mInflater = (LayoutInflater) mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        realm = Realm.getInstance(mAct);
        mSourceData = realm.where(DataListSavedArticle.class).findAll();
    }


    @Override
    public int getCount() {
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
        convertView = mInflater.inflate(R.layout.list_saved_article, null);
        holder = new ViewHolder();
        holder.titleText = (TextView) convertView.findViewById(R.id.titleText);
        holder.dateText = (TextView) convertView.findViewById(R.id.dateText);
        holder.authorText = (TextView) convertView.findViewById(R.id.authorText);
        holder.pvText = (TextView) convertView.findViewById(R.id.pvText);
        convertView.setTag(holder);

        mSourceData.get(position);
        final String KEY = mSourceData.get(position).getKey();
        final String TITLE = mSourceData.get(position).getTitle();
        final String DATE = mSourceData.get(position).getDate();
        final String AUTHOR = mSourceData.get(position).getAuthor();
        final String PV = mSourceData.get(position).getPv();

        holder.titleText.setText(TITLE);
        holder.dateText.setText(DATE);
        holder.authorText.setText(AUTHOR);
        //holder.pvText.setText(PV);
        convertView.setOnClickListener(new ArticleListClickListener(mAct, KEY,TITLE,DATE,AUTHOR,PV));
        return convertView;
    }

    private static class ViewHolder {
        public TextView titleText;
        public TextView dateText;
        public TextView authorText;
        public TextView pvText;
    }


}
