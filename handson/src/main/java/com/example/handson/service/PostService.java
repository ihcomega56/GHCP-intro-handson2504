package com.example.handson.service;

import com.example.handson.model.Post;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PostService {
    // デモ用にシンプルなインメモリストレージを使用しています
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    public Post createDraft(String content) {
        Post post = new Post(content);
        post.setId(idGenerator.getAndIncrement());
        posts.put(post.getId(), post);
        return post;
    }
    
    public Post publishPost(Long id) {
        Post post = posts.get(id);
        if (post != null && post.isDraft()) {
            post.setDraft(false);
            post.setPublishedAt(new Date().toInstant());
            return post;
        }
        return null;
    }
    
    public boolean deletePost(Long id) {
        return posts.remove(id) != null;
    }
    
    public Post getPost(Long id) {
        return posts.get(id);
    }
    
    public List<Post> getAllPublishedPosts() {
        return posts.values().stream()
                .filter(post -> !post.isDraft())
                .collect(Collectors.toList());
    }
    
    public List<Post> getAllDraftPosts() {
        return posts.values().stream()
                .filter(Post::isDraft)
                .collect(Collectors.toList());
    }
}
