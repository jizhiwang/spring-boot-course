package com.example.learning.web;

import java.util.List;

public record PageResult<T>(List<T> rows, long total) {

    public static long offset(int pageNum, int pageSize) {
        return (pageNum - 1L) * pageSize;
    }
}
