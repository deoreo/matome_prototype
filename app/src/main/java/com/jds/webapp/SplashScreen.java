package com.jds.webapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.jds.webapp.Fragment.FragmentMain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SplashScreen extends Activity {
    private Context mContext;
    private ProgressBar mProgressBar;
    private int mWaited = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
        mContext = this;
        mProgressBar = (ProgressBar) findViewById(R.id.splash_progress);


        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "er.sp.attendee.android",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.v("Current KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

	@Override
	protected void onStart() {
		super.onStart();

		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
                    for (int i = 0; i <= 1000; i++) {
                        sleep(1);
                        mProgressBar.setProgress(mWaited/10);
                        mWaited+=1;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
                    // Goes to List of Events
                    Intent goEventList = new Intent(getApplicationContext(), FragmentMain.class);
                    goEventList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(goEventList);
                    finish();
				}
			}
		};
		splashThread.start();
	}

	@Override
	public void onBackPressed() {

	}

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
