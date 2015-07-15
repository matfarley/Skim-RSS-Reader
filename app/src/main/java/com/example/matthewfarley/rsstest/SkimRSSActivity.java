package com.example.matthewfarley.rsstest;

import android.app.Activity;
import android.os.Bundle;

import com.example.matthewfarley.rsstest.Data.RssDbHelper;


public class SkimRssActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Clear database each time the app is opened.
        this.deleteDatabase(RssDbHelper.DATABASE_NAME);
        setContentView(R.layout.skim_rss_activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SkimRssFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
