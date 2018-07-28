package com.example.android.stockmarketnewsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news content from The Guardian
 */
public final class QueryUtils {

    /**
     * Private constructor created so no one can create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<NewsItem> fetchNews(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract fields from the JSON response and create a {@link List} object.
        List<NewsItem> newsItems = extractNewsItems(jsonResponse);

        // return the {@link List}
        return newsItems;
    }

    private static URL createUrl(String urlString) {
        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // if url is null, return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream and parse response if the response was successful (code 200)
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving earthquake JSON results", e);
        } finally {
            // Release the connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Close the input stream. Closing the input stream could throw an IOException,
            // which is why the makeHttpRequest(URL url) method signature specifies that an
            // IOException could be thrown.
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    /**
     * Convert the {@link InputStream} into a String which contains the entire JSON response
     * from the server.
     *
     * @param inputStream JSON response from the server
     * @return String containing entire JSON response
     * @throws IOException exception could be thrown processing the input stream
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of (@link NewItem) objects that have been built up from parsing a
     * string containing the JSON response from the server.
     *
     * @param jsonResponse JSON response from the server
     * @return List<NewsItem>
     */
    private static List<NewsItem> extractNewsItems(String jsonResponse) {
        //return early if JSON string is null
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList to which news items can be added
        List<NewsItem> newsItems = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject newsResponse = new JSONObject(jsonResponse);
            JSONObject responseObject = newsResponse.getJSONObject("response");
            JSONArray newsResults = responseObject.getJSONArray("results");

            for (int i = 0; i < newsResults.length(); i++) {
                JSONObject newsObject = (JSONObject) newsResults.get(i);
                JSONObject newsFields = newsObject.getJSONObject("fields");

                String title = newsObject.getString("webTitle");
                String section = newsObject.getString("sectionName");
                String author = newsFields.getString("byline");
                String publishDate = newsObject.getString("webPublicationDate");
                String itemUrl = newsObject.getString("webUrl");
                String newsBrief = newsFields.getString("trailText");

                NewsItem newsItem = new NewsItem(title, section, itemUrl, newsBrief, author,
                        publishDate);

                newsItems.add(newsItem);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing JSON response", e);
        }

        // return list of news items
        return newsItems;
    }
}
