package com.example.server.service;


import com.example.server.dto.PostDto;
import com.example.server.model.Comment;
import com.example.server.model.Post;
import com.example.server.model.Tag;
import com.example.server.model.User;
import com.example.server.repository.CommentRepository;
import com.example.server.repository.PostRepository;
import com.example.server.repository.SavedPostRepository;
import com.example.server.repository.TagRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SavedPostService savedPostService;
    
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    public Post addPost(PostDto postDto) {
        Post post = new Post(postDto.getAuthorId(), postDto.getContent(), postDto.getTagIds());
        return postRepository.save(post);
    }

    public void deletePost(int id) {
        List<Comment> comments = commentRepository.findByPostId(id);
        for (Comment comment : comments) {
            commentRepository.delete(comment);
        }
        savedPostService.deleteSavedPostForAllUsers(id);
        postRepository.deleteById(id);
    }

    public Post getPostById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> getPostsByAuthorId(int authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }
    
    public List<Tag> getPostTags(int id) {
        Post post = getPostById(id);
        if (post == null) {
            return null;
        } else {
            List<Integer> tagIds = post.getTagIds();
            List<Tag> tags = new ArrayList<>();
            for (Integer tagId : tagIds) {
                tags.add(tagRepository.getReferenceById(tagId));
            }
            return tags;
        }
    }

    public void incrementPostRating(int id) {
        Post post = getPostById(id);
        post.incrementRating();
        postRepository.save(post);
    }

    public void decrementPostRating(int id) {
        Post post = getPostById(id);
        post.decrementRating();
        postRepository.save(post);
    }

    public void editPost(Post post, PostDto postDto) {
        post.setContent(postDto.getContent());
        post.setTagIds(postDto.getTagIds());
        postRepository.save(post);
    }

}
