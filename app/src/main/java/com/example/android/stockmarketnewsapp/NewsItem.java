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
    private String Title;

    /**
     * Name of the news section to which the story belongs.
     */
    private String Section;

    /**
     * Website address to the full story.
     */
    private String WebLink;

    /**
     * Short description of the news story.
     */
    private String NewsBrief;

    /**
     * Names of the authors of the story.
     */
    private String Author;

    /**
     * Date that the story was last published.
     */
    private String PublishDate;

    /**
     * Constructor for a new NewsItem object with basic information: title,
     * section, and weblink.
     *
     * @param title   title or headline of the story
     * @param section section to which the story belongs
     * @param webLink web address to the full story
     */
    public NewsItem(String title, String section, String webLink) {
        Title = title;
        Section = section;
    }

    /**
     * Constructor for a new NewsItem object with title, section, webLink,
     * newsBrief, author, and publishDate.
     *
     * @param title       title or headline of the story
     * @param section     section to which the story belongs
     * @param webLink     web address to the full story
     * @param newsBrief   short description of the news story
     * @param author      one or more contributors to the story
     * @param publishDate date the story was last published
     */
    public NewsItem(String title, String section, String webLink, String newsBrief,
                    String author, String publishDate) {
        Title = title;
        Section = section;
        WebLink = webLink;
        NewsBrief = newsBrief;
        Author = author;
        PublishDate = publishDate;
    }

    /**
     * Gets the story headline or title.
     *
     * @return Title of the story
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Gets the news section to which the story belongs.
     *
     * @return Section of the story
     */
    public String getSection() {
        return Section;
    }

    /**
     * Gets the web address to the full story.
     *
     * @return Weblink to the story
     */
    public String getWebLink() {
        return WebLink;
    }

    /**
     * Gets the short description of the news story
     *
     * @return NewsBrief of the story
     */
    public String getNewsBrief() {
        return NewsBrief;
    }

    /**
     * Gets one or more authors of the story
     *
     * @return Author of the story
     */
    public String getAuthor() {
        return Author;
    }

    /**
     * Gets the last published date of the story
     *
     * @return PublishDate of the story
     */
    public String getPublishDate() {
        return PublishDate;
    }

}


