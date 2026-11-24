package com.example.learning.posts;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
interface PostMapper {

    List<Post> findPage(@Param("name") String name, @Param("offset") long offset, @Param("limit") int limit);

    long count(@Param("name") String name);

    Post findById(@Param("id") long id);

    int insert(NewPost post);

    class NewPost {
        public Long id;
        public final String name;

        NewPost(String name) {
            this.name = name;
        }
    }
}
