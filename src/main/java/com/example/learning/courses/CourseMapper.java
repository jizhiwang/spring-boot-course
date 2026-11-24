package com.example.learning.courses;

import java.util.List;

import org.apache.ibatis.annotations.*;

@Mapper
interface CourseMapper {

    @Select("""
            SELECT id, name FROM learn_course
            WHERE LOCATE(LOWER(#{name}), LOWER(name)) > 0 OR #{name} = ''
            ORDER BY name, id LIMIT #{limit} OFFSET #{offset}
            """)
    List<Course> findPage(@Param("name") String name, @Param("offset") long offset, @Param("limit") int limit);

    @Select("""
            SELECT COUNT(*) FROM learn_course
            WHERE LOCATE(LOWER(#{name}), LOWER(name)) > 0 OR #{name} = ''
            """)
    long count(String name);

    @Select("SELECT id, name FROM learn_course WHERE id = #{id}")
    Course find(String id);

    @Select("SELECT label FROM learn_course_label WHERE course_id = #{id} ORDER BY label")
    List<String> findLabels(String id);

    @Insert("INSERT INTO learn_course (id, name) VALUES (#{id}, #{name})")
    void insert(Course course);

    @Update("UPDATE learn_course SET name = #{name} WHERE id = #{id}")
    int update(Course course);

    @Delete("DELETE FROM learn_course_label WHERE course_id = #{id}")
    void deleteLabels(String id);

    @Insert("INSERT INTO learn_course_label (course_id, label) VALUES (#{id}, #{label})")
    void insertLabel(@Param("id") String id, @Param("label") String label);
}
