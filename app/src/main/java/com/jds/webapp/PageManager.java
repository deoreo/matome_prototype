package com.jds.webapp;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("CommitPrefEdits")
public class PageManager {
    private static PageManager sInstance;

    // Stated variables
    public String fromFragment, fromCategory="Fashion";
    public int rowCategory=0;

	public PageManager() { }

    public static PageManager getInstance() {
        if (sInstance == null) {
            sInstance = new PageManager();
        }
        return sInstance;
    }



    public Date convertFormatDate(final String iso8601string) {
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        Date dateFromServer = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ");
            dateFromServer = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFromServer;
    }


}