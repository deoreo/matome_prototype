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
    private static  String mKeyCategorySize = "";
    private static  String mKeyCategoryJson = "";


    private static final String KEY_RECENT = "recent";

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
        String json = _sharedPref.getString("saved_" + key, "");
        DataArticle obj = gson.fromJson(json, DataArticle.class);
        return obj;
    }

    public void setListSavedArticle(List<DataArticle> newList){
        _sharedPrefEditor.putInt("Status_size", newList.size());  

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




    //////////////////////////SET CATEGORY ARTICLE///////////////////////////////////////
    public void setListCategoryArticle(String category, List<DataArticle> newList){
        switch (category) {
            case "1":
                mKeyCategorySize = "Fashion_size";
                mKeyCategoryJson = "Fashion_";
                break;
            case "2":
                mKeyCategorySize = "Cosmetics_size";
                mKeyCategoryJson = "Cosmetics_";
                break;
            case "3":
                mKeyCategorySize = "Travel_size";
                mKeyCategoryJson = "Travel_";
                break;
            case "4":
                mKeyCategorySize = "Beauty_size";
                mKeyCategoryJson = "Beauty_";
                break;
            case "5":
                mKeyCategorySize = "Gourmet_size";
                mKeyCategoryJson = "Gourmet_";
                break;
            case "6":
                mKeyCategorySize = "Goods_size";
                mKeyCategoryJson = "Goods_";
                break;
            case "7":
                mKeyCategorySize = "Life_size";
                mKeyCategoryJson = "Life_";
                break;
            case "8":
                mKeyCategorySize = "Apps_size";
                mKeyCategoryJson = "Apps_";
                break;
            default:
                mKeyCategorySize = "Fashion_size";
                mKeyCategoryJson = "Fashion_";
        }
        _sharedPrefEditor.putInt(mKeyCategorySize, newList.size());  

        for(int i=0;i<newList.size();i++)
        {
            Gson gson = new Gson();
            String dataJson = gson.toJson(newList.get(i));
            _sharedPrefEditor.putString(mKeyCategoryJson + i, dataJson);
        }
        _sharedPrefEditor.commit();
    }

/*
    public void setListFashionArticle(List<DataArticle> newList){
        _sharedPrefEditor.putInt("Fashion_size", newList.size());

        for(int i=0;i<newList.size();i++)
        {
            Gson gson = new Gson();
            String dataJson = gson.toJson(newList.get(i));
            _sharedPrefEditor.putString("Fashion_" + i, dataJson);
        }
        _sharedPrefEditor.commit();
    }
    public void setListCosmeticsArticle(List<DataArticle> newList){
        _sharedPrefEditor.putInt("Cosmetics_size", newList.size()); 

        for(int i=0;i<newList.size();i++)
        {
            Gson gson = new Gson();
            String dataJson = gson.toJson(newList.get(i));
            _sharedPrefEditor.putString("Cosmetics_" + i, dataJson);
        }
        _sharedPrefEditor.commit();
    }
    public void setListTravelArticle(List<DataArticle> newList){
        _sharedPrefEditor.putInt("Travel_size", newList.size());  

        for(int i=0;i<newList.size();i++)
        {
            Gson gson = new Gson();
            String dataJson = gson.toJson(newList.get(i));
            _sharedPrefEditor.putString("Travel_"+i, dataJson);
        }
        _sharedPrefEditor.commit();
    }
    public void setListBeautyArticle(List<DataArticle> newList){
        _sharedPrefEditor.putInt("Beauty_size", newList.size());  

        for(int i=0;i<newList.size();i++)
        {
            Gson gson = new Gson();
            String dataJson = gson.toJson(newList.get(i));
            _sharedPrefEditor.putString("Beauty_"+i, dataJson);
        }
        _sharedPrefEditor.commit();
    }
    public void setListGourmetArticle(List<DataArticle> newList){
        _sharedPrefEditor.putInt("Gourmet_size", newList.size());  

        for(int i=0;i<newList.size();i++)
        {
            Gson gson = new Gson();
            String dataJson = gson.toJson(newList.get(i));
            _sharedPrefEditor.putString("Gourmet_"+i, dataJson);
        }
        _sharedPrefEditor.commit();
    }
    public void setListGoodsArticle(List<DataArticle> newList){
        _sharedPrefEditor.putInt("Goods_size", newList.size());  

        for(int i=0;i<newList.size();i++)
        {
            Gson gson = new Gson();
            String dataJson = gson.toJson(newList.get(i));
            _sharedPrefEditor.putString("Goods_"+i, dataJson);
        }
        _sharedPrefEditor.commit();
    }
    public void setListLifeArticle(List<DataArticle> newList){
        _sharedPrefEditor.putInt("Life_size", newList.size());  

        for(int i=0;i<newList.size();i++)
        {
            Gson gson = new Gson();
            String dataJson = gson.toJson(newList.get(i));
            _sharedPrefEditor.putString("Life_"+i, dataJson);
        }
        _sharedPrefEditor.commit();
    }
    public void setListAppsArticle(List<DataArticle> newList){
        _sharedPrefEditor.putInt("Apps_size", newList.size());  

        for(int i=0;i<newList.size();i++)
        {
            Gson gson = new Gson();
            String dataJson = gson.toJson(newList.get(i));
            _sharedPrefEditor.putString("Apps_"+i, dataJson);
        }
        _sharedPrefEditor.commit();
    }

*/

    //////////////////////////Get List Category////////////////////////////////////////
    public List<DataArticle> getListCategoryArticle(String category) {
        switch (category) {
            case "1":
                mKeyCategorySize = "Fashion_size";
                mKeyCategoryJson = "Fashion_";
                break;
            case "2":
                mKeyCategorySize = "Cosmetics_size";
                mKeyCategoryJson = "Cosmetics_";
                break;
            case "3":
                mKeyCategorySize = "Travel_size";
                mKeyCategoryJson = "Travel_";
                break;
            case "4":
                mKeyCategorySize = "Beauty_size";
                mKeyCategoryJson = "Beauty_";
                break;
            case "5":
                mKeyCategorySize = "Gourmet_size";
                mKeyCategoryJson = "Gourmet_";
                break;
            case "6":
                mKeyCategorySize = "Goods_size";
                mKeyCategoryJson = "Goods_";
                break;
            case "7":
                mKeyCategorySize = "Life_size";
                mKeyCategoryJson = "Life_";
                break;
            case "8":
                mKeyCategorySize = "Apps_size";
                mKeyCategoryJson = "Apps_";
                break;
            default:
                mKeyCategorySize = "Fashion_size";
                mKeyCategoryJson = "Fashion_";
        }

        List<DataArticle> newList = new ArrayList<DataArticle>();
        int size = _sharedPref.getInt(mKeyCategorySize, 0);
        for(int i=0;i<size;i++)
        {
            Gson gson = new Gson();
            String json = _sharedPref.getString(mKeyCategoryJson +i, "");
            DataArticle obj = gson.fromJson(json, DataArticle.class);
            newList.add(obj);
        }
        return newList;
    }
    /*
    public List<DataArticle> getListFashionArticle() {
        List<DataArticle> newList = new ArrayList<DataArticle>();
        int size = _sharedPref.getInt("Fashion_size", 0);
        for(int i=0;i<size;i++)
        {
            Gson gson = new Gson();
            String json = _sharedPref.getString("Fashion_"+i, "");
            DataArticle obj = gson.fromJson(json, DataArticle.class);
            newList.add(obj);
        }
        return newList;
    }
    public List<DataArticle> getListCosmeticsArticle() {
        List<DataArticle> newList = new ArrayList<DataArticle>();
        int size = _sharedPref.getInt("Cosmetics_size", 0);
        for(int i=0;i<size;i++)
        {
            Gson gson = new Gson();
            String json = _sharedPref.getString("Cosmetics_"+i, "");
            DataArticle obj = gson.fromJson(json, DataArticle.class);
            newList.add(obj);
        }
        return newList;
    }
    public List<DataArticle> getListTravelArticle() {
        List<DataArticle> newList = new ArrayList<DataArticle>();
        int size = _sharedPref.getInt("Travel_size", 0);
        for(int i=0;i<size;i++)
        {
            Gson gson = new Gson();
            String json = _sharedPref.getString("Travel_"+i, "");
            DataArticle obj = gson.fromJson(json, DataArticle.class);
            newList.add(obj);
        }
        return newList;
    }
    public List<DataArticle> getListBeautyArticle() {
        List<DataArticle> newList = new ArrayList<DataArticle>();
        int size = _sharedPref.getInt("Beauty_size", 0);
        for(int i=0;i<size;i++)
        {
            Gson gson = new Gson();
            String json = _sharedPref.getString("Beauty_"+i, "");
            DataArticle obj = gson.fromJson(json, DataArticle.class);
            newList.add(obj);
        }
        return newList;
    }
    public List<DataArticle> getListGourmetArticle() {
        List<DataArticle> newList = new ArrayList<DataArticle>();
        int size = _sharedPref.getInt("Gourmet_size", 0);
        for(int i=0;i<size;i++)
        {
            Gson gson = new Gson();
            String json = _sharedPref.getString("Gourmet_"+i, "");
            DataArticle obj = gson.fromJson(json, DataArticle.class);
            newList.add(obj);
        }
        return newList;
    }
    public List<DataArticle> getListGoodsArticle() {
        List<DataArticle> newList = new ArrayList<DataArticle>();
        int size = _sharedPref.getInt("Goods_size", 0);
        for(int i=0;i<size;i++)
        {
            Gson gson = new Gson();
            String json = _sharedPref.getString("Goods_"+i, "");
            DataArticle obj = gson.fromJson(json, DataArticle.class);
            newList.add(obj);
        }
        return newList;
    }
    public List<DataArticle> getListLifeArticle() {
        List<DataArticle> newList = new ArrayList<DataArticle>();
        int size = _sharedPref.getInt("Life_size", 0);
        for(int i=0;i<size;i++)
        {
            Gson gson = new Gson();
            String json = _sharedPref.getString("Life_"+i, "");
            DataArticle obj = gson.fromJson(json, DataArticle.class);
            newList.add(obj);
        }
        return newList;
    }
    public List<DataArticle> getListAppsArticle() {
        List<DataArticle> newList = new ArrayList<DataArticle>();
        int size = _sharedPref.getInt("Apps_size", 0);
        for(int i=0;i<size;i++)
        {
            Gson gson = new Gson();
            String json = _sharedPref.getString("Apps_"+i, "");
            DataArticle obj = gson.fromJson(json, DataArticle.class);
            newList.add(obj);
        }
        return newList;
    }
*/

    public void setFirstArticle(DataArticle dataArticle){

            Gson gson = new Gson();
            String dataRecent = gson.toJson(dataArticle);
            _sharedPrefEditor.putString("data_recent", dataRecent);

        _sharedPrefEditor.commit();
    }

    public DataArticle getFirstArticle() {
            Gson gson = new Gson();
            String json = _sharedPref.getString("data_recent","");
            DataArticle obj = gson.fromJson(json, DataArticle.class);

        return obj;
    }
}
