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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jds.webapp.Adapter.AdapterSearchArticle;
import com.jds.webapp.BlurTransform;
import com.jds.webapp.DataArticle;
import com.jds.webapp.JSONControl;
import com.jds.webapp.PageManager;
import com.jds.webapp.R;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentArticle extends Fragment {
    WebView webview;
    TextView titleText, authorText, commentText;
    String data = "",  judul="", info="", pv="";
    String key, title, date, author, bundlepv, thumbnail;
    String id, msg, nam, datecomment;
    ImageView articleImage;
    private static final String KEY_ID = "id";
    private static final String KEY_MSG = "msg";
    private static final String KEY_NAM = "nam";
    private static final String KEY_DATE = "date";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getExtras();
        View view = inflater.inflate(R.layout.activity_web, container, false);
        ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll_view);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToScrollView(scrollView);
        titleText = (TextView)view.findViewById(R.id.articletitleText);
        authorText = (TextView)view.findViewById(R.id.authorText);
        commentText = (TextView) view.findViewById(R.id.commentTextView);
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
        new GetComment(id).execute();
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCustomView();
                    }
                });
        return view;
    }
    private void getExtras() {
        Bundle bundle = getArguments();
        id = bundle.getString("id");
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
            webview.loadDataWithBaseURL(null,

                    "<style>iframe{display: inline;height: auto;max-width: 100%;}</style>"+
                    "<style>img{display: inline;height: auto;max-width: 100%;}</style>"+
                    "<style>blockquote{margin: 1em 0 0;padding: 10px 15px 18px 40px;background-color: #f5f5f5;background-image: url(http://matome.id/images/quote.png);background-position: 10px 10px;background-repeat: no-repeat;border-radius: 5px;}</style>"+
                    "<style>a{display:block;width:300px;color:#aaa;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:small;text-decoration:none;margin:auto}</style>"+
                    "<body>"+data+"</body>", "text/html", "utf-8", null);
            titleText.setText(Html.fromHtml("<font color='#000011'><u>" + judul + "</u></font>"));
            authorText.setText(Html.fromHtml("<font color='#000011'><i>" + info + "</i></font><font color='#000011'><i> (" + pv + ")</i></font>"));
            Log.v("WebView", data);
        }
    }
    private void showCustomView() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Comment")
                .customView(R.layout.dialog_customview, true)
                .positiveText("Submit")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();
        dialog.show();
    }

    public class GetComment extends AsyncTask<String, Void, JSONArray> {
        ProgressDialog pDialog;
        String key_article="";
        String comments = "";
        public GetComment(String key_article){
            this.key_article = key_article;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONArray doInBackground(String... arg) {
            JSONArray json = null;
            JSONControl JSONControl = new JSONControl();
            json = JSONControl.listComment(key_article);
            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    String id = "";
                    String msg = "";
                    String nam = "";
                    String datecomment = "";
                    String postdate = "";
                    boolean status = true;
                    try {
                        JSONObject jsonObject = json.getJSONObject(i);
                        id = jsonObject.getString(KEY_ID);
                        nam = jsonObject.getString(KEY_NAM);
                        msg = jsonObject.getString(KEY_MSG);
                        datecomment = jsonObject.getString(KEY_DATE);
                        Date date = PageManager.getInstance().convertFormatDate(datecomment);
                        postdate = new SimpleDateFormat("yyyy/MM/dd").format(date);
                        status = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        status = false;
                    }
                    if (status) {
                        comments+=Html.fromHtml(
                                "<font color='#ffffff'>"+nam+"</font><br>"+
                                "<font color='#ffffff'>"+msg+"</font><br><br>"
                        );
                    }
                }
                //mAdapter = new AdapterSearchArticle(getActivity(), LIST_ARTICLE_MATOME);
            }
            return json;
        }

        @Override
        protected void onPostExecute(final JSONArray json) {
            // TODO Auto-generated method stub
            super.onPostExecute(json);
            //commentText.setText(comments);

        }
    }


}
