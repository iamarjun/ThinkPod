package com.arjun.thinkpod.model.xml;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

//strict = false, means that we don't mind skipping some elements provided in the XML inside this root
@Root(name = "channel", strict = false)
public class Channel {

    @ElementList(inline = true, name = "image", required = false)
    private List<ArtworkImage> mArtworkImages;

    @Element(name = "title", required = false)
    private String mTitle;

    @Path("description")
    @Text(required = false)
    private String mDescription;

    @Path("author")
    @Text(required = false)
    private String mITunesAuthor;

    @Element(name = "language", required = false)
    private String mLanguage;

    @ElementList(inline = true, name = "category", required = false)
    private List<Category> mCategories;

    @ElementList(inline = true, name = "item", required = false)
    private List<Item> mItemList;


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getITunesAuthor() {
        return mITunesAuthor;
    }

    public void setITunesAuthor(String iTunesAuthor) {
        mITunesAuthor = iTunesAuthor;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public void setCategories(List<Category> categories) {
        mCategories = categories;
    }

    public List<Item> getItemList() {
        return mItemList;
    }

    public void setItemList(List<Item> itemList) {
        mItemList = itemList;
    }

    public List<ArtworkImage> getArtworkImages() {
        return mArtworkImages;
    }

    public void setArtworkImages(List<ArtworkImage> artworkImages) {
        mArtworkImages = artworkImages;
    }

}

