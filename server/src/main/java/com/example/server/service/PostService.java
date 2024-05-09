package com.example.server.service;


import com.example.server.dto.PostDto;
import com.example.server.model.*;
import com.example.server.repository.CommentRepository;
import com.example.server.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SavedObjectService savedObjectService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatedObjectService ratedObjectService;

    public Post addPost(PostDto postDto) {
        Post post = new Post(postDto.getAuthorId(), postDto.getTitle(),
                postDto.getContent(), postDto.getTags(), postDto.getCommentsCount());
        return postRepository.save(post);
    }

    public void deletePost(int id) {
        List<Comment> comments = commentRepository.findByPostId(id);
        for (Comment comment : comments) {
            ratedObjectService.deleteRatingsOfObject(comment.getId());
            savedObjectService.deleteSavedObjectForAllUsers(comment.getId());
            commentRepository.delete(comment);
        }
        ratedObjectService.deleteRatingsOfObject(id);
        savedObjectService.deleteSavedObjectForAllUsers(id);
        postRepository.deleteById(id);
    }

    public Post getPostById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> getPostsByAuthorId(int authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public List<Post> getPosts() {
        var posts = postRepository.findAll();
        posts.sort(Comparator.comparing(Post::getCreationTime, Comparator.reverseOrder()));
        return posts;
    }

    public List<Post> getPostsByPostIds(List<Integer> postIds) {
        return postRepository.findAllById(postIds);
//        List<Post> posts = new ArrayList<>();
//        for (int postId : postIds) {
//            posts.add(postRepository.getReferenceById(postId));
//        }
//        return posts;
    }

    public List<Post> getPostsBySelectedTags(String tags) {
        List<Integer> tagIds = Tag.tagsToTagIds(tags);
        List<Post> posts = getPosts();
        return posts.stream()
                .filter(post -> Tag.tagsToTagIds(post.getTags())
                        .stream()
                        .anyMatch(tagIds::contains))
                .collect(Collectors.toList());
    }

    public List<String> getPostTags(int id) {
        Post post = getPostById(id);
        if (post == null) {
            return null;
        } else {
            List<Integer> tagIds = Tag.tagsToTagIds(post.getTags());
            List<String> tags = new ArrayList<>();
            for (Integer tagId : tagIds) {
                tags.add(Tag.getTagName(tagId));
            }
            return tags;
        }
    }

    public int incrementPostRating(int id, int userId) {
        Post post = getPostById(id);
        Optional<RatedObject.Type> rating = ratedObjectService.getObjectRating(userId, id);
        if (rating.isEmpty()) {
            ratedObjectService.rateObject(userId, id, RatedObject.Type.LIKE);
            post.incrementRating();
        } else if (rating.get() == RatedObject.Type.LIKE) {
            ratedObjectService.deleteObjectRating(userId, id);
            post.decrementRating();
        } else {
            ratedObjectService.changeRating(userId, id);
            post.incrementRating();
            post.incrementRating();
        }
        postRepository.save(post);
        return post.getRating();
    }

    public int decrementPostRating(int id, int userId) {
        Post post = getPostById(id);
        Optional<RatedObject.Type> rating = ratedObjectService.getObjectRating(userId, id);
        if (rating.isEmpty()) {
            ratedObjectService.rateObject(userId, id, RatedObject.Type.DISLIKE);
            post.decrementRating();
        } else if (rating.get() == RatedObject.Type.DISLIKE) {
            ratedObjectService.deleteObjectRating(userId, id);
            post.incrementRating();
        } else {
            ratedObjectService.changeRating(userId, id);
            post.decrementRating();
            post.decrementRating();
        }
        postRepository.save(post);
        return post.getRating();
    }

    public void editPost(Post post, PostDto postDto) {
        post.setContent(postDto.getContent());
        post.setTags(postDto.getTags());
        postRepository.save(post);
    }

    public void changeCommentsCount(int postId, int delta) {
        Post post = getPostById(postId);
        if (post != null) {
            post.setCommentsCount(post.getCommentsCount() + delta);
        }
    }
}
