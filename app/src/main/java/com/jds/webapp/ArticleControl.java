package com.jds.webapp;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleControl {
    private ArticleResponse _articleResponse;
    private static String URL_API_ARTICLE = "http://api.matome.id/1/article/";


    public ArticleControl() {
        _articleResponse = new ArticleResponse();
    }

    public JSONArray listArticle() {
        JSONArray json = _articleResponse.GetArticleResponse(URL_API_ARTICLE);
        return json;
    }

    /*
    public JSONObject registerUser(String name, String email, String password) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = _articleResponse.GetUserResponse(URL_ARTICLE);
        return json;
    }*/



}
