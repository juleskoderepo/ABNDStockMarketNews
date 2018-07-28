package com.example.android.stockmarketnewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    private static final String GUARDIAN_NEWS_REQUEST =
            "http://content.guardianapis.com/search?tag=business/stock-markets|business/us-markets|business/emerging-markets&rights=developer-community&from-date=2018-01-01&order-by=newest&show-fields=trailText,thumbnail,byline&api-key=37220e4c-85dd-438f-88a9-6e68a5274211";
    private static final int NEWSAPP_LOADER_ID = 1;

    private TextView emptyTV;
    private ProgressBar progressBar;

    private NewsItemAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptyTV = findViewById(R.id.empty_textview);
        progressBar = findViewById(R.id.progress_spinner);

        // Check for connectivity
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Prepare loader
            getLoaderManager().initLoader(NEWSAPP_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyTV.setText(getString(R.string.no_internet_connection));
        }

        // Find the {@link ListView} in the layout
        ListView newsListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of news stories
        newsAdapter = new NewsItemAdapter(
                this, new ArrayList<NewsItem>());

        // Set the adapter on the {@link ListView} so the list can be populated in the UI
        newsListView.setAdapter(newsAdapter);

        // Set the empty view on the {@link ListView} if the adapter is empty
        View emptyView = emptyTV;
        newsListView.setEmptyView(emptyView);

        // open link for selected news story
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem currentItem = newsAdapter.getItem(position);
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(currentItem.getWebLink()));
                startActivity(openURL);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(NewsActivity.this, GUARDIAN_NEWS_REQUEST);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsData) {

        // Hide progress spinner when load has completed
        progressBar.setVisibility(View.GONE);

        // Set text on empty view in case no results are returned.
        emptyTV.setText(getString(R.string.no_news_found));

        // exit if response is empty
        if (newsData == null) {
            return;
        }

        // update UI
        updateUI(newsData);

    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        // Loader reset so clear the adapter of previous news data
        newsAdapter.clear();
    }

    private void updateUI(List<NewsItem> newsItems) {
        // clear the adapter of any previous data
        newsAdapter.clear();

        // If there is a valid list of {@link NewsItem}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsItems != null && !newsItems.isEmpty()) {
            newsAdapter.addAll(newsItems);
        }
    }
}
