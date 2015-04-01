package com.jds.webapp.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jds.webapp.ArticleListClickListener;
import com.jds.webapp.BlurTransform;
import com.jds.webapp.DataArticle;
import com.jds.webapp.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class AdapterListArticle extends BaseAdapter {
    private FragmentActivity mAct;
    private List<DataArticle> mSourceData, mFilterData;
    private LayoutInflater mInflater =null;

    public AdapterListArticle(FragmentActivity activity,List<DataArticle> d) {
        mAct = activity;
        mSourceData = d;
        mInflater = (LayoutInflater) mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mSourceData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = mInflater.inflate(R.layout.list_category_article, null);
        holder = new ViewHolder();
        holder.titleText = (TextView) convertView.findViewById(R.id.titleText);
        holder.dateText = (TextView) convertView.findViewById(R.id.dateText);
        holder.authorText = (TextView) convertView.findViewById(R.id.authorText);
        holder.pvText = (TextView) convertView.findViewById(R.id.pvText);
        holder.articleListBgImage = (ImageView) convertView.findViewById(R.id.articleListBgImage);
        convertView.setTag(holder);

        DataArticle article = mSourceData.get(position);
        final String ID = article.getId();
        final String TITLE = article.getTitle();
        final String DATE = article.getDate();
        final String AUTHOR = article.getAuthor();
        final String PV = article.getPv();
        final String KEY = article.getKey();
        final String THUMBNAIL = article.getThumbnail();
        final String URL_THUMBNAIL = "http://api.matome.id/photo/"+THUMBNAIL+"?w=310&h=260&c=fill";

        holder.titleText.setText(TITLE);
        holder.dateText.setText(DATE);
        holder.authorText.setText(AUTHOR+" ");
        holder.pvText.setText(Html.fromHtml(" - <i>" + PV + " Views </i>"));
        //Picasso.with(mAct).load(URL_THUMBNAIL).into(holder.articleListThumbnail);
        Picasso.with(mAct).load(URL_THUMBNAIL).fit().into(holder.articleListBgImage);

        convertView.setOnClickListener(new ArticleListClickListener(mAct, "AdapterListArticle", ID, KEY, TITLE, DATE, AUTHOR, PV, URL_THUMBNAIL));


        return convertView;
    }

    private static class ViewHolder {
        public TextView titleText;
        public TextView dateText;
        public TextView authorText;
        public TextView pvText;
        public ImageView articleListBgImage;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

}
