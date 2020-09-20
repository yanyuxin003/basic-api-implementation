package com.thoughtworks.rslist.exception;

import lombok.Data;

@Data
//自动实现error的GET SET方法
public class Error {
    private String error;

//    public String getError() {
//        return error;
//    }
//
//    public void setError(String error) {
//        this.error = error;
//    }
}