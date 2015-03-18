package com.jds.webapp.Fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jds.webapp.BlurTransform;
import com.jds.webapp.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class FragmentArticle extends Fragment {
    WebView webview;
    TextView titleText, authorText;
    String data = "",  judul="", info="", pv="";
    String key, title, date, author, bundlepv, thumbnail;
    ImageView articleImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getExtras();
        View view = inflater.inflate(R.layout.activity_web, container, false);
        titleText = (TextView)view.findViewById(R.id.articletitleText);
        authorText = (TextView)view.findViewById(R.id.authorText);
        webview = (WebView) view.findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setBuiltInZoomControls(true);

       // webview.getSettings().setLoadWithOverviewMode(true);
        //webview.getSettings().setUseWideViewPort(true);
        //webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(true);

        articleImage = (ImageView) view.findViewById(R.id.articleImage);
        Picasso.with(getActivity()).load(thumbnail)
                .fit()
                .into(articleImage);
        new LoadPage().execute(key);
        return view;
    }
    private void getExtras() {
        Bundle bundle = getArguments();
        key = bundle.getString("key");
        title = bundle.getString("title");
        date = bundle.getString("date");
        author = bundle.getString("author");
        bundlepv = bundle.getString("pv");
        thumbnail = bundle.getString("thumbnail");
    }


    private class LoadPage extends AsyncTask<String, Void, String> {
        Document doc = null;
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait. . .");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String key = params[0];
            try {
                doc = Jsoup.connect("http://matome.id/"+key).get();
                String primeDiv="content";
                //scrap content
                Elements content = doc.select("div[id="+primeDiv+"]");
                for (Element post : content) {
                    Elements post_content = post.getElementsByClass("post-content");
                    for(Element c : post_content){
                        judul = c.getElementsByClass("post-title").text();
                        Elements authors = c.getElementsByClass("author-line");
                        for(Element a : authors){
                            info = a.getElementsByClass("author-name").text();
                            pv = a.getElementsByClass("grey").text();
                        }
                        c.getElementsByClass("post-title").remove();
                        c.getElementsByClass("post-info").remove();
                        c.getElementsByClass("author-line").remove();
                        data = c.html();
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return key;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            webview.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>"+
                    "<style>blockquote{margin: 1em 0 0;padding: 10px 15px 18px 40px;background-color: #f5f5f5;background-image: url(http://matome.id/images/quote.png);background-position: 10px 10px;background-repeat: no-repeat;border-radius: 5px;}</style>"+
                    "<style>a{display:block;width:300px;color:#aaa;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:small;text-decoration:none;margin:auto}</style>"+
                    "<body>"+data+"</body>", "text/html", "utf-8", null);
            titleText.setText(Html.fromHtml("<font color='#000011'><u>" + judul + "</u></font>"));
            authorText.setText(Html.fromHtml("<font color='#000011'><i>" + info + "</i></font><font color='#000011'><i> (" + pv + ")</i></font>"));
            Log.v("WebS", data);



        }

    }
}
