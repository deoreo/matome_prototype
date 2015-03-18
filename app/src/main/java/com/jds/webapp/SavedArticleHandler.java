/*
 * Copyright 2014 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jds.webapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import io.realm.Realm;

public class SavedArticleHandler extends Handler {

    public static final int ADD_ARTICLE = 1;
    public static final int LOAD_ARTICLE = 2;
    public static final int REMOVE_ARTICLE = 3;
    public static final String ACTION = "action";

    private String key, title, date, author, pv,thumbnail;
    private Realm realm;

    public SavedArticleHandler(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void handleMessage(Message msg) {
        final Bundle bundle = msg.getData();
        final int action = bundle.getInt(ACTION);
        key = bundle.getString("key");
        title = bundle.getString("title");
        date = bundle.getString("date");
        author = bundle.getString("author");
        pv = bundle.getString("pv");
        thumbnail = bundle.getString("thumbnail");

        switch (action) {
            case ADD_ARTICLE:
                realm.beginTransaction();
                DataListSavedArticle dataListSavedArticle = realm.createObject(DataListSavedArticle.class);
                dataListSavedArticle.setKey(key);
                dataListSavedArticle.setTitle(title);
                dataListSavedArticle.setDate(date);
                dataListSavedArticle.setAuthor(author);
                dataListSavedArticle.setPv(pv);
                dataListSavedArticle.setThumbnail(thumbnail);
                realm.commitTransaction();
                break;
            case LOAD_ARTICLE:
                realm.where(DataListSavedArticle.class).findAll();
                break;
            case REMOVE_ARTICLE:
                realm.beginTransaction();
                realm.where(DataListSavedArticle.class).equalTo("key", key).findAll().clear();
                realm.commitTransaction();
                break;
        }
    }
}
