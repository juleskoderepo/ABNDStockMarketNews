package com.example.android.stockmarketnewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    private String url;

    NewsLoader(Context context, String requestUrl) {
        super(context);
        url = requestUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        //return early if request URL is null
        if (url == null) {
            return null;
        }

        List<NewsItem> newsItemList;
        newsItemList = QueryUtils.fetchNews(url);

        return newsItemList;
    }
}
