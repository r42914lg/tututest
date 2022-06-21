package com.r42914lg.tutu.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * POJO for clue - list of Clues is a member of CategoryDetailed
 */
public class Clue {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("airdate")
    @Expose
    private String airdate;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("game_id")
    @Expose
    private Object gameId;
    @SerializedName("invalid_count")
    @Expose
    private Integer invalidCount;

    public Clue(Integer id, String answer, String question) {
        this.id = id;
        this.answer = answer;
        this.question = question;
    }

    public String asString() {
        return String.format("Question: %s; Answer: %s \n\n", question, answer);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getAirdate() {
        return airdate;
    }

    public void setAirdate(String airdate) {
        this.airdate = airdate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Object getGameId() {
        return gameId;
    }

    public void setGameId(Object gameId) {
        this.gameId = gameId;
    }

    public Integer getInvalidCount() {
        return invalidCount;
    }

    public void setInvalidCount(Integer invalidCount) {
        this.invalidCount = invalidCount;
    }

}
