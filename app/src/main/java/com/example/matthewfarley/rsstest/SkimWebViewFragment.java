package com.example.matthewfarley.rsstest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewFragment;

/**
 * Created by matthewfarley on 8/06/15.
 */
public class SkimWebViewFragment extends WebViewFragment {
    private String url;

    public static final String TAG = SkimWebViewFragment.class.getSimpleName();
    public static final String KEY_URL = "keyURL";

    public SkimWebViewFragment(){
        super();
    }

    public static SkimWebViewFragment newInstance(String url){
        SkimWebViewFragment swvf = new SkimWebViewFragment();
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        swvf.setArguments(args);
        return swvf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        WebView webView = (WebView)super.onCreateView(inflater, container, savedInstanceState);
        webView.getSettings().setJavaScriptEnabled(true);
        url = getArguments().getString(KEY_URL);
        webView.loadUrl(url);
        return webView;
    }
}
