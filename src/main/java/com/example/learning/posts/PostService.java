package com.example.learning.posts;

import com.example.learning.web.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
class PostService {

    private final PostRepository repository;

    PostService(PostRepository repository) {
        this.repository = repository;
    }

    PageResult<Post> list(String name, int pageNum, int pageSize) {
        return new PageResult<>(repository.findPage(name, PageResult.offset(pageNum, pageSize), pageSize),
                repository.count(name));
    }

    Post find(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    Post create(String name) {
        return repository.insert(name);
    }
}
