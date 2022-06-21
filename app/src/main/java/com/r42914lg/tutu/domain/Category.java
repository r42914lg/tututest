package com.r42914lg.tutu.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * POJO for category - corresponds to individual item from list returned by https://jservice.io/api/categories
 */
public class Category {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("clues_count")
    @Expose
    private Integer cluesCount;

    public Category(Integer id, String title, Integer cluesCount) {
        this.id = id;
        this.title = title;
        this.cluesCount = cluesCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCluesCount() {
        return cluesCount;
    }

    public void setCluesCount(Integer cluesCount) {
        this.cluesCount = cluesCount;
    }

}
