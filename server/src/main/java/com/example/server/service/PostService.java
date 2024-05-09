package com.example.server.service;


import com.example.server.dto.PostDto;
import com.example.server.model.*;
import com.example.server.repository.CommentRepository;
import com.example.server.repository.PostRepository;
import com.example.server.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatedObjectService ratedObjectService;

    @Autowired
    FileInfoService fileInfoService;

    public Post addPost(PostDto postDto) {
        Post post = new Post(postDto.getAuthorId(), postDto.getTitle(),
                postDto.getContent(), postDto.getTagIds());
        return postRepository.save(post);
    }

    public void deletePost(int id) throws IOException {
        List<Comment> comments = commentRepository.findByPostId(id);
        for (Comment comment : comments) {
            ratedObjectService.deleteRatingsOfObject(comment.getId());
            savedObjectService.deleteSavedObjectForAllUsers(comment.getId());
            commentRepository.delete(comment);
        }
        ratedObjectService.deleteRatingsOfObject(id);
        savedObjectService.deleteSavedObjectForAllUsers(id);
        Post post = getPostById(id);
        FileInfo fileInfo = fileInfoService.findById(post.getFileInfoId());
        if (fileInfo != null) {
            fileInfoService.delete(id, fileInfo);
        }
        List<Integer> picInfoIds = post.getPicInfoIds();
        if (picInfoIds != null) {
            for (Integer picInfoId : picInfoIds) {
                fileInfoService.delete(id, fileInfoService.findById(picInfoId));
            }
        }
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

    public List<Post> getPostsByPostIds(List<Integer> postIds) {
        List<Post> posts = new ArrayList<>();
        for (int postId : postIds) {
            posts.add(postRepository.getReferenceById(postId));
        }
        return posts;
    }

    public List<Post> getPostsBySelectedTags(List<Integer> tagIds) {
        List<Post> posts = getPosts();
        return posts.stream()
                .filter(post -> post.getTagIds().stream().anyMatch(tagIds::contains))
                .collect(Collectors.toList());
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

    public void incrementPostRating(int id, int userId) {
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
    }

    public void decrementPostRating(int id, int userId) {
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
    }

    public void editPost(Post post, PostDto postDto) {
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setTagIds(postDto.getTagIds());
        postRepository.save(post);
    }

    public void incrementCommentsCount(int id) {
        Post post = getPostById(id);
        post.incrementCommentsCount();
        postRepository.save(post);
    }

    public void decrementCommentsCount(int id) {
        Post post = getPostById(id);
        post.decrementCommentsCount();
        postRepository.save(post);
    }

    public Post deleteFile(int id) throws IOException {
        Post post = getPostById(id);
        fileInfoService.delete(id, fileInfoService.findById(post.getFileInfoId()));
        post.setFileInfoId(null);
        return postRepository.save(post);
    }

    public Post uploadFile(int id, MultipartFile file) throws IOException {
        Post post = getPostById(id);
        FileInfo fileInfo = fileInfoService.upload(file, id);
        if (post.getFileInfoId() != null) {
            fileInfoService.delete(id, fileInfoService.findById(post.getFileInfoId()));
        }
        post.setFileInfoId(fileInfo.getId());
        return postRepository.save(post);
    }

    public Post deletePicture(int postId, int picNumber) throws IOException {
        Post post = getPostById(postId);
        List<Integer> picInfoIds = post.getPicInfoIds();
        FileInfo picInfo = fileInfoService.findById(picInfoIds.get(picNumber));
        fileInfoService.delete(postId, picInfo);
        picInfoIds.remove(picNumber);
        post.setPicInfoIds(picInfoIds);
        return postRepository.save(post);
    }

    public void uploadPicture(int postId, MultipartFile pic) throws IOException {
        Post post = getPostById(postId);
        List<Integer> picInfoIds;
        if (post.getPicInfoIds() == null) {
            picInfoIds = new ArrayList<>();
        } else {
            picInfoIds = post.getPicInfoIds();
        }
        FileInfo fileInfo = fileInfoService.upload(pic, postId);
        picInfoIds.add(fileInfo.getId());
        post.setPicInfoIds(picInfoIds);
        postRepository.save(post);
    }

    public Post uploadPictures(int postId, List<MultipartFile> pics) throws IOException {
        for (MultipartFile pic : pics) {
            uploadPicture(postId, pic);
        }
        return getPostById(postId);
    }

    public byte[] getFileData(String key) throws IOException {
        return FileInfoService.download(key);
    }

}
