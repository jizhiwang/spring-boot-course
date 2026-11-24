package com.example.learning.courses;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
interface CourseMapper {

    List<Course> findPage(@Param("name") String name, @Param("offset") long offset, @Param("limit") int limit);

    long count(@Param("name") String name);

    Course find(@Param("id") String id);

    List<String> findLabels(@Param("id") String id);

    void insert(Course course);

    int update(Course course);

    void deleteLabels(@Param("id") String id);

    void insertLabel(@Param("id") String id, @Param("label") String label);
}
