package com.example.matthewfarley.rsstest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.matthewfarley.rsstest.Data.RssContract;
import com.example.matthewfarley.rsstest.Data.RssContract.ArticleEntry;
import com.example.matthewfarley.rsstest.Data.RssDBHelper;
import com.example.matthewfarley.rsstest.Service.RssService;


/**
 * A placeholder fragment containing a simple view.
 */
public class SkimRssFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ARTICLE_LOADER = 0;
    private static final String FEED_URL = "http://racerxonline.com/feeds/rss/category/breaking-news";

    private RssCursorAdapter mRssCursorAdapter;
    private ListView mListView;

    private static String[] ARTICLE_PROJECTION = {
            ArticleEntry.ROW_ID,
            ArticleEntry.TITLE,
            ArticleEntry.DESCRIPTION,
            ArticleEntry.PUBLISH_DATE,
            ArticleEntry.ARTICLE_URL,
            ArticleEntry.ARTICLE_IS_READ,
            ArticleEntry.THUMBNAIL_URL
    };

    public SkimRssFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        setRetainInstance(true);
        refreshListWithFeedUrl(FEED_URL);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getActivity().deleteDatabase(RssDBHelper.DATABASE_NAME);
                refreshListWithFeedUrl(FEED_URL);
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_skim_rss, container, false);

        mListView = (ListView)view.findViewById(R.id.feed_list_view);
        // Change to cursor adapter
        mRssCursorAdapter = new RssCursorAdapter(getActivity(), null, 0);
        mListView.setAdapter(mRssCursorAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mRssCursorAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    SkimWebViewFragment swvf = SkimWebViewFragment.newInstance(cursor.getString(ArticleEntry.COL_ARTICLE_URL));
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, swvf, SkimWebViewFragment.TAG);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                // Need set articles to read in the database
//                Article article = (Article)mListView.getItemAtPosition(position);
//                article.setRead(true);
//                mRssCursorAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void refreshListWithFeedUrl(String url){

        if (isNetworkAvailable()){
            Intent intent = new Intent(getActivity(), RssService.class);
            intent.putExtra(RssService.RSS_FEED_URL_KEY,
                    url);
            getActivity().startService(intent);
        }else{
            Context context = getActivity().getApplicationContext();
            CharSequence text = getActivity().getResources().getString(R.string.msg_no_connection);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(ARTICLE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //String sortOrder = ArticleEntry.PUBLISH_DATE + " ASC";
        return new CursorLoader(
              getActivity(),
                ArticleEntry.CONTENT_URI,
                ARTICLE_PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRssCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRssCursorAdapter.swapCursor(null);
    }
}
