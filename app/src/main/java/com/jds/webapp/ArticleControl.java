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
    private static String URL_API_SEARCH_ARTICLE = "http://api.matome.id/1/article/?per_page=1000&page=1&word=";
    private static String URL_API_CATEGORY_ARTICLE = "http://api.matome.id/1/article/?per_page=500&page=1&cat=";

    public ArticleControl() {
        _articleResponse = new ArticleResponse();
    }

    public JSONArray listArticle() {
        JSONArray json = _articleResponse.GetArticleResponse(URL_API_ARTICLE);
        return json;
    }
    public JSONArray searchArticle(String keyword) {
        JSONArray json = _articleResponse.GetArticleResponse(URL_API_SEARCH_ARTICLE + keyword);
        return json;
    }
    public JSONArray listCategoryArticle(String category) {
        JSONArray json = _articleResponse.GetArticleResponse(URL_API_CATEGORY_ARTICLE + category);
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
