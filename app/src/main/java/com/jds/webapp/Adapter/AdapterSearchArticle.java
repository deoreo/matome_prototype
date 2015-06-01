package com.jds.webapp.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jds.webapp.ArticleListClickListener;
import com.jds.webapp.DataArticle;
import com.jds.webapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterSearchArticle extends BaseAdapter {
    private FragmentActivity mAct;
    private List<DataArticle> mSourceData, mFilterData;
    private LayoutInflater mInflater = null;

    public AdapterSearchArticle(FragmentActivity activity, List<DataArticle> d) {
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
        convertView.setTag(position);

        DataArticle article = mSourceData.get(position);
        final String ID = article.getId();
        final String TITLE = article.getTitle();
        final String DATE = article.getDate();
        final String AUTHOR = article.getAuthor();
        final String PV = article.getPv();
        final String KEY = article.getKey();
        final String THUMBNAIL = article.getThumbnail();
        final String URL_THUMBNAIL = "http://api.matomeindo.com/photo/" + THUMBNAIL + "?w=310&h=260&c=fill";

        holder.titleText.setText(TITLE);
        holder.dateText.setText(DATE);
        holder.authorText.setText(AUTHOR + " ");
        holder.pvText.setText(Html.fromHtml(" - <i>" + PV + " Views </i>"));

        //Picasso.with(mAct).load(URL_THUMBNAIL).error(R.drawable.bear).fit().into(holder.articleListBgImage);
        Glide.with(mAct).load(URL_THUMBNAIL).error(R.drawable.bear).fitCenter().centerCrop().into(holder.articleListBgImage);
        convertView.setOnClickListener(new ArticleListClickListener(mAct, "AdapterSearchArticle", ID, KEY, TITLE, DATE, AUTHOR, PV, URL_THUMBNAIL));


        return convertView;
    }

    private static class ViewHolder {
        public TextView titleText;
        public TextView dateText;
        public TextView authorText;
        public TextView pvText;
        public ImageView articleListBgImage;
    }

}
