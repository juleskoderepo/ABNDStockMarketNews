package com.example.android.stockmarketnewsapp;

/**
 * {@link NewsItem} represents a news story consisting of a title, relevant text,
 * the section that it belongs to, and if available, the author name, date published,
 * image URL.
 */
public class NewsItem {

    /**
     * Title or headline of the news story.
     */
    private String storyTitle;

    /**
     * Name of the news section to which the story belongs.
     */
    private String storySection;

    /**
     * Website address to the full story.
     */
    private String webLink;

    /**
     * Short description of the news story.
     */
    private String newsBrief;

    /**
     * Names of the authors of the story.
     */
    private String storyAuthor;

    /**
     * Date that the story was last published.
     */
    private String publishDate;

    /**
     * Constructor for a new NewsItem object with basic information: title,
     * section, and weblink.
     *
     * @param title   title or headline of the story
     * @param section section to which the story belongs
     * @param link web address to the full story
     */
    public NewsItem(String title, String section, String link) {
        storyTitle = title;
        storySection = section;
        webLink = link;
    }

    /**
     * Constructor for a new NewsItem object with title, section, webLink,
     * newsBrief, author, and publishDate.
     *
     * @param title       title or headline of the story
     * @param section     section to which the story belongs
     * @param link     web address to the full story
     * @param brief   short description of the news story
     * @param author      one or more contributors to the story
     * @param date date the story was last published
     */
    public NewsItem(String title, String section, String link, String brief,
                    String author, String date) {
        storyTitle = title;
        storySection = section;
        webLink = link;
        newsBrief = brief;
        storyAuthor = author;
        publishDate = date;
    }

    /**
     * Gets the story headline or title.
     *
     * @return Title of the story
     */
    public String getTitle() {
        return storyTitle;
    }

    /**
     * Gets the news section to which the story belongs.
     *
     * @return Section of the story
     */
    public String getSection() {
        return storySection;
    }

    /**
     * Gets the web address to the full story.
     *
     * @return Weblink to the story
     */
    public String getWebLink() {
        return webLink;
    }

    /**
     * Gets the short description of the news story
     *
     * @return News brief of the story
     */
    public String getNewsBrief() {
        return newsBrief;
    }

    /**
     * Gets one or more authors of the story
     *
     * @return Author of the story
     */
    public String getAuthor() {
        return storyAuthor;
    }

    /**
     * Gets the last published date of the story
     *
     * @return PublishDate of the story
     */
    public String getPublishDate() {
        return publishDate;
    }

}


