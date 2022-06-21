package com.r42914lg.tutu.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * POJO for detailed category - corresponds to individual item from list returned by https://jservice.io/api/category?id=ххххх
 */
public class CategoryDetailed {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("clues_count")
    @Expose
    private Integer cluesCount;
    @SerializedName("clues")
    @Expose
    private List<Clue> clues = null;

    public CategoryDetailed(Integer id, String title, Integer cluesCount, List<Clue> clues) {
        this.id = id;
        this.title = title;
        this.cluesCount = cluesCount;
        this.clues = clues;
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

    public List<Clue> getClues() {
        return clues;
    }

    public void setClues(List<Clue> clues) {
        this.clues = clues;
    }
}
