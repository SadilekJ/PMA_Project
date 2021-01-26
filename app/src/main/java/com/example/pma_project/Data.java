package com.example.pma_project;

import java.util.Date;

public class Data {
    private String date;
    private String pathToImage;
    private String title;
    private String explanation;

    public Data(String date, String pathToImage, String title, String explanation) {
        this.date = date;
        this.pathToImage = pathToImage;
        this.title = title;
        this.explanation = explanation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
