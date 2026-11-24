package com.example.learning.posts;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mysql", "test-db"})
class MyBatisPostRepository implements PostRepository {

    private final PostMapper mapper;

    MyBatisPostRepository(PostMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Post> findPage(String name, long offset, int limit) {
        return mapper.findPage(name, offset, limit);
    }

    @Override
    public long count(String name) {
        return mapper.count(name);
    }

    @Override
    public Optional<Post> findById(long id) {
        return Optional.ofNullable(mapper.findById(id));
    }

    @Override
    public Post insert(String name) {
        PostMapper.NewPost post = new PostMapper.NewPost(name);
        mapper.insert(post);
        return new Post(post.id, post.name);
    }
}
