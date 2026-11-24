package com.example.learning.posts;

import com.example.learning.web.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
class PostService {

    private final PostMapper mapper;

    PostService(PostMapper mapper) {
        this.mapper = mapper;
    }

    PageResult<Post> list(String name, int pageNum, int pageSize) {
        return new PageResult<>(mapper.findPage(name, PageResult.offset(pageNum, pageSize), pageSize),
                mapper.count(name));
    }

    Post find(long id) {
        Post post = mapper.findById(id);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        return post;
    }

    Post create(String name) {
        PostMapper.NewPost post = new PostMapper.NewPost(name);
        mapper.insert(post);
        return new Post(post.id, post.name);
    }
}
