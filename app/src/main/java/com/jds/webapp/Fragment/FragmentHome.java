package com.jds.webapp.Fragment;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jds.webapp.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class FragmentHome extends Fragment {
    WebView webview;
    TextView title,author;
    String data = "",  judul="", info="", pv="", url="";
    Boolean progressEnd;

    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressEnd = true;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_main, container, false);
        url = "http://m.matome.id/articles";
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        webview = (WebView) view.findViewById(R.id.webView2);
        webview.setVisibility(View.INVISIBLE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    progressEnd = true;
                    webview.setVisibility(View.VISIBLE);
                }
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                    webview.loadUrl("javascript:(function() { " +
                            "document.getElementsByTagName('header')[0].style.display='none'; " +
                            "document.getElementsByClassName('breadcrumb')[0].style.display='none'; " +
                            "document.getElementsByClassName('post-section')[0].style.display='none'; " +
                            "document.getElementsByTagName('footer')[0].style.display='none'; " +
                            "})()");

            }
        });
        webview.loadUrl(url);
        webview.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
                    //handler.sendEmptyMessage(1);
                    webview.goBack();
                    return true;
                }
                return false;
            }

        });

        return view;
    }

    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        }
    }
    private class LoadPage extends AsyncTask<String, Void, String> {
        Document doc = null;
        @Override
        protected String doInBackground(String... params) {

            try {
                doc = Jsoup.connect("http://matome.id/481").get();
                String primeDiv="content";
                //scrap content
                Elements content = doc.select("div[id="+primeDiv+"]");
                for (Element post : content) {
                    Elements post_content = post.getElementsByClass("post-content");
                    for(Element c : post_content){
                        judul = c.getElementsByClass("post-titleText").text();
                        Elements authors = c.getElementsByClass("authorText-line");
                        for(Element a : authors){
                            info = a.getElementsByClass("authorText-name").text();
                            pv = a.getElementsByClass("grey").text();
                        }
                        c.getElementsByClass("post-titleText").remove();
                        c.getElementsByClass("post-info").remove();
                        c.getElementsByClass("authorText-line").remove();
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

            webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
            title.setText(Html.fromHtml("<font color='#000011'><u>" + judul + "</u></font>"));
            author.setText(Html.fromHtml("<font color='#000011'><i>Oleh : "+info+"</i></font><font color='#000011'><i> ("+pv+")</i></font>"));
            Log.v("WebS", data);

        }

    }


}
