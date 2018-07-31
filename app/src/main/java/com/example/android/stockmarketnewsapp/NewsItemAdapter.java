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

    private static final String LOG_TAG = NewsItemAdapter.class.getSimpleName();

    NewsItemAdapter(Activity context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        NewsViewHolder viewHolder;

        // Check if existing view is being reused, otherwise inflate the view
        View newsItemView = convertView;
        if (newsItemView == null) {
            newsItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);

            // set up the ViewHolder
            viewHolder = new NewsViewHolder();
            viewHolder.titleTextView = newsItemView.findViewById(R.id.story_title);
            viewHolder.sectionTextView = newsItemView.findViewById(R.id.news_section);
            viewHolder.authorTextView = newsItemView.findViewById(R.id.author);
            viewHolder.dateTextView = newsItemView.findViewById(R.id.publish_date);
            viewHolder.summaryTextView = newsItemView.findViewById(R.id.news_brief);

            // store the holder with the view
            newsItemView.setTag(viewHolder);
        } else {
            viewHolder = (NewsViewHolder) newsItemView.getTag();
        }

        // Get the {@link NewsItem} object located at this position in the list
        NewsItem currentItem = getItem(position);

        if(currentItem != null){
            //get the TextViews from the ViewHolder and set the text values
            viewHolder.titleTextView.setText(currentItem.getTitle());
            viewHolder.sectionTextView.setText(currentItem.getSection());
            viewHolder.authorTextView.setText(currentItem.getAuthor());
            viewHolder.summaryTextView.setText(currentItem.getNewsBrief());

            String dateStr = currentItem.getPublishDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

            try {
                // Convert the date string to a Date object based on the specified date pattern
                Date dateObject = format.parse(dateStr);
                String formattedDate = formatDate(dateObject);
                viewHolder.dateTextView.setText(formattedDate);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Parse exception - date format");
            }
            
        }

        return newsItemView;
    }

    private String formatDate(Object dateObj) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
        return dateFormat.format(dateObj);
    }

    // this ViewHolder caches the TextViews of our NewItem states
    static class NewsViewHolder{
        TextView titleTextView;
        TextView sectionTextView;
        TextView authorTextView;
        TextView dateTextView;
        TextView summaryTextView;
    }
}
