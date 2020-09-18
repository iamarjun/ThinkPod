package com.arjun.thinkpod.model.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "category", strict = false)
public class Category {

    @Attribute(name = "text", required = false)
    private String mText;

    public Category() {
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
