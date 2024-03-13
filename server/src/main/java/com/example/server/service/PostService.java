package com.example.server.service;


import com.example.server.dto.ContentObjDto;
import com.example.server.model.Post;
import com.example.server.model.User;
import com.example.server.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post addPost(ContentObjDto postDto) {
        Post post = new Post(postDto.getAuthorId(), postDto.getContent());
        return postRepository.save(post);
    }

    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    public Post getPostById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> getPostsByAuthorId(User authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public void incrementPostRating(long id) {
        Post post = getPostById(id);
        post.incrementRating();
    }

    public void decrementPostRating(long id) {
        Post post = getPostById(id);
        post.decrementRating();
    }

    public void addCommentToPost(long postId, long commentId) {
        Post post = getPostById(postId);
        List<Long> comments = post.getComments();
        comments.add(commentId);
        post.setComments(comments);
        postRepository.save(post);
    }

}
