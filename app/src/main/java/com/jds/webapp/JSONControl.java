package com.jds.webapp;

import android.app.Activity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

public class JSONControl {
    private JSONResponse _JSONResponse;
    private static String URL_API_ARTICLE = "http://api.matome.id/1/article/";
    private static String URL_API_SEARCH_ARTICLE = "http://api.matome.id/1/article/?per_page=1000&page=1&word=";
    private static String URL_API_CATEGORY_ARTICLE = "http://api.matome.id/1/article/?per_page=500&page=1&cat=";
    private static String URL_API_COMMENT = "http://api.matome.id/1/comment";

    public JSONControl() {
        _JSONResponse = new JSONResponse();
    }

    public JSONArray listArticle(Activity activity) {
        JSONArray json = null;
        try {
            json = _JSONResponse.GETResponse(URL_API_ARTICLE);
        } catch (ConnectException e) {
            DialogBox.getInstance().showDialog(activity, null, "", "OK", "Warning", "Please reload");
        }
        return json;
    }
    public JSONArray searchArticle(String keyword) throws ConnectException {
        JSONArray json = _JSONResponse.GETResponse(URL_API_SEARCH_ARTICLE + keyword);
        return json;
    }
    public JSONArray listCategoryArticle(String category) throws ConnectException {
        JSONArray json = _JSONResponse.GETResponse(URL_API_CATEGORY_ARTICLE + category);
        return json;
    }

    public JSONArray listComment(String article_key) throws ConnectException {
        JSONArray json = _JSONResponse.GETResponse(URL_API_COMMENT +"/"+article_key);
        return json;
    }


    public void postComment(String art, String msg, String nam) {

        try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("art", art));
                params.add(new BasicNameValuePair("msg", msg));
                params.add(new BasicNameValuePair("nam", nam));
                JSONObject jsonObj = _JSONResponse.POSTResponse(URL_API_COMMENT, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
