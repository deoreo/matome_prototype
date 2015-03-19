package com.jds.webapp;

import android.annotation.SuppressLint;

@SuppressLint("CommitPrefEdits")
public class PageManager {
    private static PageManager sInstance;

    // Stated variables
    public String fromFragment, fromSearch;

	public PageManager() { }

    public static PageManager getInstance() {
        if (sInstance == null) {
            sInstance = new PageManager();
        }
        return sInstance;
    }


}