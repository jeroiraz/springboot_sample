package com.example.demo.model;

import java.io.Serializable;
import java.util.Date;

public class Reminder implements Serializable {
    
    private String title;

    private String description;

    private Date date;

    public Reminder(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "title=" + title +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
