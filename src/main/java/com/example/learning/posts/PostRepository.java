package com.example.learning.posts;

import java.util.List;
import java.util.Optional;

interface PostRepository {

    List<Post> findPage(String name, long offset, int limit);

    long count(String name);

    Optional<Post> findById(long id);

    Post insert(String name);
}
