package com.thoughtworks.rslist.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RsEvent {

    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    @NotNull
    private int userId;
    private int voteNum = 10;

    public RsEvent(@NotNull String eventName, @NotNull String keyWord, @NotNull int userId) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;
        this.voteNum = voteNum;
    }

    public RsEvent() {
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

//    public int getVoteNum() {
//        return voteNum;
//    }
//
//    public void setVoteNum(int voteNum) {
//        this.voteNum = voteNum;
//    }
}