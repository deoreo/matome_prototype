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
import android.webkit.WebView;
import android.widget.TextView;

import com.jds.webapp.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class FragmentArticle extends Fragment {
    WebView webview;
    TextView titleText, authorText;
    String data = "",  judul="", info="", pv="";
    String key, title, date, author, bundlepv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getExtras();
        View view = inflater.inflate(R.layout.activity_web, container, false);
        titleText = (TextView)view.findViewById(R.id.textView);
        authorText = (TextView)view.findViewById(R.id.authorText);
        webview = (WebView) view.findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
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
    }

    private class LoadArticles extends AsyncTask<String, Void, String> {
        Document doc = null;
        @Override
        protected String doInBackground(String... params) {

            try {
                doc = Jsoup.connect("http://matome.id/fashion").get();
                data =doc.html();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
            titleText.setText(Html.fromHtml("<font color='#000011'><u>" + judul + "</u></font>"));
            authorText.setText(Html.fromHtml("<font color='#000011'><i>Oleh : " + info + "</i></font><font color='#000011'><i> (" + pv + ")</i></font>"));
            Log.v("data", data);

        }

    }

    private class LoadPage extends AsyncTask<String, Void, String> {
        Document doc = null;
        Document doc1 = null;
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

            try {
                doc = Jsoup.connect("http://matome.id/"+params[0]).get();
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

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
            titleText.setText(Html.fromHtml("<font color='#000011'><u>" + judul + "</u></font>"));
            authorText.setText(Html.fromHtml("<font color='#000011'><i>Oleh : " + info + "</i></font><font color='#000011'><i> (" + pv + ")</i></font>"));
            Log.v("WebS", data);

        }

    }
}
