package com.jds.webapp.Adapter;

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
import android.widget.TextView;

import com.jds.webapp.ArticleListClickListener;
import com.jds.webapp.DataArticle;
import com.jds.webapp.R;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

public class AdapterListArticle extends BaseAdapter {
    private FragmentActivity mAct;
    private List<DataArticle> mSourceData;
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
        convertView = mInflater.inflate(R.layout.list_article, null);
        holder = new ViewHolder();
        holder.titleText = (TextView) convertView.findViewById(R.id.titleText);
        holder.dateText = (TextView) convertView.findViewById(R.id.dateText);
        holder.authorText = (TextView) convertView.findViewById(R.id.authorText);
        holder.pvText = (TextView) convertView.findViewById(R.id.pvText);
        holder.articleListBgImage = (ImageView) convertView.findViewById(R.id.articleListBgImage);
        holder.articleListThumbnail = (ImageView) convertView.findViewById(R.id.articleListThumbnail);
        convertView.setTag(holder);

        DataArticle article = mSourceData.get(position);
        final String TITLE = article.getTitle();
        final String DATE = article.getDate();
        final String AUTHOR = article.getAuthor();
        final String PV = article.getPv();
        final String KEY = article.getKey();

        holder.titleText.setText(TITLE);
        holder.dateText.setText(DATE);
        holder.authorText.setText(AUTHOR);
        holder.pvText.setText(Html.fromHtml(" - <i>" + PV + " Views </i>"));
        //new DownloadImageTask(holder.articleListThumbnail).execute(THUMBNAIL);
        convertView.setOnClickListener(new ArticleListClickListener(mAct, KEY, TITLE, DATE, AUTHOR, PV));
        return convertView;
    }

    private static class ViewHolder {
        public TextView titleText;
        public TextView dateText;
        public TextView authorText;
        public TextView pvText;
        public ImageView articleListBgImage;
        public ImageView articleListThumbnail;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
