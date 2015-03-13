package com.jds.webapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ArticlePersistence {
    private static ArticlePersistence _articlePersistence = null;
    private SharedPreferences _sharedPref;
    private SharedPreferences.Editor _sharedPrefEditor;
    private Context _context;

    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "MatomeArticle";

    private static final String KEY_KEY = "key";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_PV = "pv";
    private static final String KEY_POST_DATE = "postDate";
    private static final String KEY_DESC = "description";

    public ArticlePersistence(Context context){
        _context = context;
        _sharedPref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        _sharedPrefEditor = _sharedPref.edit();
    }

    public static ArticlePersistence getInstance(Context context) {
        if (_articlePersistence == null)
            _articlePersistence = new ArticlePersistence(context);
        return _articlePersistence;
    }

    public void setKeyKey(String str){
        _sharedPrefEditor.putString(KEY_KEY, str);
        _sharedPrefEditor.commit();
    }

    public String getKeyKey() {
        return _sharedPref.getString(KEY_KEY, null);
    }

    public void setKeyTitle(String str){
        _sharedPrefEditor.putString(KEY_TITLE, str);
        _sharedPrefEditor.commit();
    }

    public String getKeyTitle() {
        return _sharedPref.getString(KEY_TITLE, null);
    }

    public void setKeyAuthor(String str){
        _sharedPrefEditor.putString(KEY_AUTHOR, str);
        _sharedPrefEditor.commit();
    }

    public String getKeyAuthor() {
        return _sharedPref.getString(KEY_AUTHOR, null);
    }

    public void setKeyPostDate(String str){
        _sharedPrefEditor.putString(KEY_POST_DATE, str);
        _sharedPrefEditor.commit();
    }

    public String getKeyPostDate() {
        return _sharedPref.getString(KEY_POST_DATE, null);
    }

    public void setKeyPv(String str){
        _sharedPrefEditor.putString(KEY_PV, str);
        _sharedPrefEditor.commit();
    }

    public String getKeyPv() {
        return _sharedPref.getString(KEY_PV, null);
    }

    public void setKeyDesc(String str){
        _sharedPrefEditor.putString(KEY_DESC, str);
        _sharedPrefEditor.commit();
    }

    public String getKeyDesc() {
        return _sharedPref.getString(KEY_DESC, null);
    }

    public void setSavedArticle(DataArticle dataArticle, int key){
        Gson gson = new Gson();
        String dataJson = gson.toJson(dataArticle);
        _sharedPrefEditor.putString("saved_"+key, dataJson);
        _sharedPrefEditor.commit();

    }

    public DataArticle getSavedArticle(int key){
        Gson gson = new Gson();
        String json = _sharedPref.getString("saved_"+key, "");
        DataArticle obj = gson.fromJson(json, DataArticle.class);
        return obj;
    }

    public void setListSavedArticle(List<DataArticle> newList){
        //newList = new ArrayList<DataArticle>();
        _sharedPrefEditor.putInt("Status_size", newList.size()); /* sKey is an array */

        for(int i=0;i<newList.size();i++)
        {
            Gson gson = new Gson();
            String dataJson = gson.toJson(newList.get(i));
            _sharedPrefEditor.putString("Status_"+i, dataJson);
        }
        _sharedPrefEditor.commit();
    }

    public List<DataArticle> getListSavedArticle() {
        List<DataArticle> newList = new ArrayList<DataArticle>();
        int size = _sharedPref.getInt("Status_size", 0);
        for(int i=0;i<size;i++)
        {
            Gson gson = new Gson();
            String json = _sharedPref.getString("Status_"+i, "");
            DataArticle obj = gson.fromJson(json, DataArticle.class);
            newList.add(obj);
        }
        return newList;
    }
}
