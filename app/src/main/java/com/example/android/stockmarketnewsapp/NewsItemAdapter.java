package com.example.android.stockmarketnewsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NewsItemAdapter extends ArrayAdapter<NewsItem> {

    TextView title;
    TextView section;
    TextView author;
    TextView date;
    TextView summaryLine;

    private static final String LOG_TAG = NewsItemAdapter.class.getSimpleName();

    NewsItemAdapter(Activity context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if existing view is being reused, otherwise inflate the view
        View newsItemView = convertView;
        if (newsItemView == null) {
            newsItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        // Get the {@link NewsItem} object located at this position in the list
        NewsItem currentItem = getItem(position);

        // Get Views in UI and populate with values from the current item
        title = newsItemView.findViewById(R.id.story_title);
        title.setText(currentItem.getTitle());

        section = newsItemView.findViewById(R.id.news_section);
        String sectionInfo = getContext().getString(R.string.section) + currentItem.getSection();
        section.setText(sectionInfo);

        author = newsItemView.findViewById(R.id.author);
        String byline = getContext().getString(R.string.byline) + currentItem.getAuthor();
        author.setText(byline);

        date = newsItemView.findViewById(R.id.publish_date);
        String dateStr = currentItem.getPublishDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

        try {
            // Convert the date string to a Date object based on the specified date pattern
            Date dateObject = format.parse(dateStr);
            String formattedDate = formatDate(dateObject);
            date.setText(formattedDate);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Parse exception - date format");
        }

        summaryLine = newsItemView.findViewById(R.id.news_brief);
        summaryLine.setText(currentItem.getNewsBrief());

        return newsItemView;
    }

    private String formatDate(Object dateObj) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
        return dateFormat.format(dateObj);
    }
}
