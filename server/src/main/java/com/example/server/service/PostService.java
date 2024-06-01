package com.example.server.service;


import com.example.server.dto.PostDto;
import com.example.server.model.*;
import com.example.server.repository.CommentRepository;
import com.example.server.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final String DIRECTORY_PATH = "src\\main\\resources\\files\\";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SavedObjectService savedObjectService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatedObjectService ratedObjectService;

    @Autowired
    private UserService userService;

    @Autowired
    FileInfoService fileInfoService;

    public Post addPost(PostDto postDto) throws IOException {
        Post post = new Post(postDto.getAuthorId(), postDto.getTitle(),
                postDto.getContent(), postDto.getTags(), postDto.getCommentsCount());
        postRepository.save(post);
        if (postDto.isResumeNeeded()) {
            User author = userService.getUser(postDto.getAuthorId());
            FileInfo resumeInfo = fileInfoService.findById(author.getResumeInfoId());
            MultipartFile multipartFile = null;
            try {
                multipartFile = new MockMultipartFile(resumeInfo.getFileName(), userService.getResumeData(postDto.getAuthorId() + "\\" + resumeInfo.getKey()));
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException();
            }
            uploadFile(post.getId(), multipartFile);
            FileInfo fileInfo = fileInfoService.findById(post.getFileInfoId());
            fileInfo.setFileName(resumeInfo.getFileName());
            fileInfo.setFileType(resumeInfo.getFileType());
            post.setFileName(resumeInfo.getFileName());
            fileInfoService.saveFileInfo(fileInfo);
        }
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
        FileInfo fileInfo = null;
        if (post.getFileInfoId() != null) {
            fileInfo = fileInfoService.findById(post.getFileInfoId());
        }
        if (fileInfo != null) {
            fileInfoService.delete(id, fileInfo, DIRECTORY_PATH + id + "\\");
        }
        List<Integer> picInfoIds = post.getPicInfoIds();
        if (picInfoIds != null) {
            for (Integer picInfoId : picInfoIds) {
                fileInfoService.delete(id, fileInfoService.findById(picInfoId), DIRECTORY_PATH + id + "\\");
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
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setTags(postDto.getTags());
        postRepository.save(post);
    }

    public Post deleteFile(int id) throws IOException {
        Post post = getPostById(id);
        fileInfoService.delete(id, fileInfoService.findById(post.getFileInfoId()),
                DIRECTORY_PATH + id + "\\");
        post.setFileInfoId(null);
        return postRepository.save(post);
    }

    public Post uploadFile(int id, MultipartFile file) throws IOException {
        Post post = getPostById(id);
        FileInfo fileInfo = fileInfoService.upload(file, id, DIRECTORY_PATH + id + "\\");
        if (post.getFileInfoId() != null) {
            fileInfoService.delete(id,
                    fileInfoService.findById(post.getFileInfoId()), DIRECTORY_PATH + id + "\\");
        }
        post.setFileInfoId(fileInfo.getId());
        post.setFileName(fileInfo.getFileName());
        return postRepository.save(post);
    }

    public Post deletePicture(int postId, int picNumber) throws IOException {
        Post post = getPostById(postId);
        List<Integer> picInfoIds = post.getPicInfoIds();
        FileInfo picInfo = fileInfoService.findById(picInfoIds.get(picNumber));
        fileInfoService.delete(postId, picInfo, DIRECTORY_PATH + postId + "\\");
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
        FileInfo fileInfo = fileInfoService.upload(pic, postId, DIRECTORY_PATH + postId + "\\");
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
        return FileInfoService.download(key, DIRECTORY_PATH);
    }

    public void changeCommentsCount(int postId, int delta) {
        Post post = getPostById(postId);
        if (post != null) {
            post.setCommentsCount(post.getCommentsCount() + delta);
        }
    }
}
