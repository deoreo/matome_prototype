package com.jds.webapp;

import org.json.JSONArray;

public class JSONControl {
    private JSONResponse _JSONResponse;
    private static String URL_API_ARTICLE = "http://api.matome.id/1/article/";
    private static String URL_API_SEARCH_ARTICLE = "http://api.matome.id/1/article/?per_page=1000&page=1&word=";
    private static String URL_API_CATEGORY_ARTICLE = "http://api.matome.id/1/article/?per_page=500&page=1&cat=";
    private static String URL_API_GET_COMMENT = "http://api.matome.id/1/comment/";

    public JSONControl() {
        _JSONResponse = new JSONResponse();
    }

    public JSONArray listArticle() {
        JSONArray json = _JSONResponse.GETResponse(URL_API_ARTICLE);
        return json;
    }
    public JSONArray searchArticle(String keyword) {
        JSONArray json = _JSONResponse.GETResponse(URL_API_SEARCH_ARTICLE + keyword);
        return json;
    }
    public JSONArray listCategoryArticle(String category) {
        JSONArray json = _JSONResponse.GETResponse(URL_API_CATEGORY_ARTICLE + category);
        return json;
    }

    public JSONArray listComment(String article_key) {
        JSONArray json = _JSONResponse.GETResponse(URL_API_GET_COMMENT + article_key);
        return json;
    }

    /*
    public JSONObject registerUser(String name, String email, String password) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = _JSONResponse.GetUserResponse(URL_ARTICLE);
        return json;
    }*/



}
