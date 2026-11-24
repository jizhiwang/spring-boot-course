package com.example.learning.posts;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!mysql & !test-db")
class MemoryPostRepository implements PostRepository {

    private final ConcurrentMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong();

    MemoryPostRepository() {
        insert("Java Developer");
        insert("QA Engineer");
        insert("Java Lead");
        insert("Product Manager");
    }

    @Override
    public List<Post> findPage(String name, long offset, int limit) {
        return posts.values().stream()
                .filter(post -> matches(post, name))
                .sorted(Comparator.comparingLong(Post::id))
                .skip(offset)
                .limit(limit)
                .toList();
    }

    @Override
    public long count(String name) {
        return posts.values().stream().filter(post -> matches(post, name)).count();
    }

    @Override
    public Optional<Post> findById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    @Override
    public Post insert(String name) {
        Post post = new Post(nextId.incrementAndGet(), name);
        posts.put(post.id(), post);
        return post;
    }

    private boolean matches(Post post, String name) {
        return name == null || post.name().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT));
    }
}
