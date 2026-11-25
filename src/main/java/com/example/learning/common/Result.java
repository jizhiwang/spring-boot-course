package com.example.learning.common;

import java.util.HashMap;

import com.example.learning.constants.HttpStatus;

public class Result extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "code";
    public static final String MSG_TAG = "msg";
    public static final String DATA_TAG = "data";

    public Result(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    public static Result success(Object data) {
        return new Result(HttpStatus.SUCCESS, "操作成功", data);
    }
}
