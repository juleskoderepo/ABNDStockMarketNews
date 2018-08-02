package com.example.android.stockmarketnewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
            "http://content.guardianapis.com/search";
    private static final String GUARDIAN_API_KEY = BuildConfig.APIKEY;
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
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        // Retrieve app preferences in a SharedPreference object
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieve preference object key-value pairs. The first parameter
        // is the key for this preference. The second parameter is the default value
        // for this preference.
        String queryParamOrderBy = getString(R.string.settings_order_by_key);
        String queryParamFromDate = getString(R.string.settings_from_date_key);
        String queryParamToDate = getString(R.string.settings_to_date_key);
        String queryParamPageSize = getString(R.string.settings_page_size_key);

        String orderBy = sharedPrefs.getString(
                queryParamOrderBy,
                getString(R.string.settings_order_by_default));

        String fromDate = sharedPrefs.getString(
                queryParamFromDate,
                getString(R.string.settings_from_date_default));

        String toDate = sharedPrefs.getString(
                queryParamToDate,
                getString(R.string.settings_to_date_default));

        String pageSize = sharedPrefs.getString(
                queryParamPageSize,
                getString(R.string.settings_page_size_default));

        //Retrieve other query parameters and values
        String queryParamTag = getString(R.string.query_param_tag);
        String queryParamTagValue = getString(R.string.query_param_tag_values);
        String queryParamRights = getString(R.string.query_param_rights);
        String queryParamRightsValue = getString(R.string.query_param_rights_values);
        String queryParamShowFields = getString(R.string.query_param_show_fields);
        String queryParamShowFieldsValue = getString(R.string.query_param_show_fields_values);
        String queryParamApiKey = getString(R.string.query_param_api_key);

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_NEWS_REQUEST);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it.
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter(queryParamTag, queryParamTagValue);
        uriBuilder.appendQueryParameter(queryParamRights, queryParamRightsValue);
        uriBuilder.appendQueryParameter(queryParamShowFields, queryParamShowFieldsValue);
        uriBuilder.appendQueryParameter(queryParamOrderBy, orderBy);

        // Check settings values and append parameter only if value is not null or empty
        String fromDatePref = sharedPrefs.getString(queryParamFromDate, "");
        String toDatePref = sharedPrefs.getString(queryParamToDate, "");
        String pageSizePref = sharedPrefs.getString(queryParamPageSize, "");

        if (fromDatePref != null && !fromDatePref.isEmpty()) {
            uriBuilder.appendQueryParameter(queryParamFromDate, fromDate);
        }
        if (toDatePref != null && !toDatePref.isEmpty()) {
            uriBuilder.appendQueryParameter(queryParamToDate, toDate);
        }
        if (pageSizePref != null && !pageSizePref.isEmpty()) {
            uriBuilder.appendQueryParameter(queryParamPageSize, pageSize);
        }

        uriBuilder.appendQueryParameter(queryParamApiKey, GUARDIAN_API_KEY);

        // pass complete web request URI string in NewsLoader object
        return new NewsLoader(NewsActivity.this, uriBuilder.toString());
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
