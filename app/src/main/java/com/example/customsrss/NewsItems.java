package com.example.customsrss;
import com.google.firebase.database.IgnoreExtraProperties;
public class NewsItems {
    String title, description;

    public NewsItems() {

    }

    public NewsItems(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        title = title;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        description = description;
    }

}