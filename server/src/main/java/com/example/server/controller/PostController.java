package com.example.server.controller;


import com.example.server.dto.PostDto;
import com.example.server.model.FileInfo;
import com.example.server.model.Post;
import com.example.server.model.SavedObject;
import com.example.server.service.FileInfoService;
import com.example.server.model.Tag;
import com.example.server.service.PostService;
import com.example.server.service.SavedObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RestController
public class PostController {

    private final PostService postService;

    private final SavedObjectService savedObjectService;

    private final FileInfoService fileInfoService;

    @Autowired
    public PostController(PostService postService,
                          SavedObjectService savedObjectService,
                          FileInfoService fileInfoService) {
        this.postService = postService;
        this.savedObjectService = savedObjectService;
        this.fileInfoService = fileInfoService;
    }

    @GetMapping("/post/getAll")
    public ResponseEntity<List<Post>> getAll(@RequestParam(defaultValue = "-1") int prevId,
                                                    @RequestParam(defaultValue = "10") int size) {
        List<Post> list = postService.getPostsAfterId(prevId, size);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @GetMapping("/post/getForUser")
    ResponseEntity<List<Post>> getPostsByUserTags(@RequestParam("userId") int userId,
                                              @RequestParam(defaultValue = "-1") int prevId,
                                              @RequestParam(defaultValue = "10") int size) {
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Post> list = postService.getPostsByUserTagsAfterId(userId, prevId, size);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @RequestMapping("/post/getByTags")
    ResponseEntity<List<Post>> getPostsByTags(@RequestParam("tags") String tags,
                                              @RequestParam(defaultValue = "-1") int prevId,
                                              @RequestParam(defaultValue = "10") int size) {
        List<Post> list = postService.getPostsBySelectedTagsAfterId(tags, prevId, size);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @PostMapping("/post/add")
    ResponseEntity<Post> addPost(@RequestBody PostDto postDto) {
        if (!Objects.equals(AuthController.getCurrentUserId(), postDto.getAuthorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Post post;
        try {
            post = postService.addPost(postDto);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping(value = "/post/{id}")
    ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/post/{id}/file")
    ResponseEntity<?> getPostFile(@PathVariable Integer id) {
        Post post = postService.getPostById(id);
        if (post.getFileInfoId() == null) {
            return ResponseEntity.ok(null);
        }
        FileInfo fileInfo = fileInfoService.findById(post.getFileInfoId());
        try {
            byte[] fileData = postService.getFileData(id + "\\" + fileInfo.getKey());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(fileInfo.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileInfo.getFileName() + "\"")
                    .body(fileData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(value = "/post/{id}/file/add")
    ResponseEntity<Post> addPostFile(@PathVariable Integer id, @ModelAttribute MultipartFile file) {
        Post post = postService.getPostById(id);
        if (!Objects.equals(AuthController.getCurrentUserId(), post.getAuthorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(postService.uploadFile(id, file));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/post/{id}/file/delete")
    ResponseEntity<Post> deletePostFile(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(postService.deleteFile(id));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(value = "/post/{id}/pic/add")
    ResponseEntity<Post> addPostPictures(@PathVariable Integer id, @ModelAttribute List<MultipartFile> pics) {
        try {
            return ResponseEntity.ok(postService.uploadPictures(id, pics));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/post/{id}/pic/{number}")
    ResponseEntity<?> getPostPicture(@PathVariable Integer id, @PathVariable Integer number) {
        Post post = postService.getPostById(id);
        if (post.getPicInfoIds() == null || post.getPicInfoIds().size() <= number) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        FileInfo picInfo = fileInfoService.findById(post.getPicInfoIds().get(number));
        try {
            byte[] fileData = postService.getFileData(id + "\\" + picInfo.getKey());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(picInfo.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + picInfo.getFileName() + "\"")
                    .body(fileData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(value = "/post/{id}/pic/{number}/delete")
    ResponseEntity<Post> deletePostPicture(@PathVariable Integer id, @PathVariable Integer number) {
        try {
            return ResponseEntity.ok(postService.deletePicture(id, number));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping("/post/{id}/edit")
    ResponseEntity<Post> editPost(@PathVariable Integer id, @RequestBody PostDto postDto) {
        Post post = postService.getPostById(id);
        if (!Objects.equals(AuthController.getCurrentUserId(), post.getAuthorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            postService.editPost(post, postDto);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
    }

    @PostMapping("/post/{id}/delete")
    ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        if (!Objects.equals(AuthController.getCurrentUserId(), postService.getPostById(id).getAuthorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            postService.deletePost(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @RequestMapping(value = "/post/{id}/like")
    ResponseEntity<Integer> likePost(@PathVariable Integer id, @RequestParam("userId") int userId) {
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        int rating = postService.incrementPostRating(id, userId);
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    @RequestMapping(value = "/post/{id}/dislike")
    ResponseEntity<Integer> dislikePost(@PathVariable Integer id, @RequestParam("userId") int userId) {
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        int rating = postService.decrementPostRating(id, userId);
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    @RequestMapping(value = "/post/{id}/save")
    ResponseEntity<Void> savePost(@PathVariable Integer id, @RequestParam("userId") int userId) {
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        savedObjectService.saveObject(userId, id, SavedObject.Type.POST);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/saved/posts")
    ResponseEntity<List<Post>> getSavedPost(@RequestParam("userId") int userId) {
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Integer> savedPostIds = savedObjectService.getSavedObjectByUserId(userId, SavedObject.Type.POST);
        List<Post> savedPosts = postService.getPostsByPostIds(savedPostIds);
        if (savedPosts == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        savedPosts.sort(Comparator.comparing(Post::getCreationTime, Comparator.reverseOrder()));
        return new ResponseEntity<>(savedPosts, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/posts")
    ResponseEntity<List<Post>> getUserPosts(@RequestParam("userId") int userId) {
        List<Post> userPosts = postService.getPostsByAuthorId(userId);
        if (userPosts == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userPosts.sort(Comparator.comparing(Post::getCreationTime, Comparator.reverseOrder()));
        return new ResponseEntity<>(userPosts, HttpStatus.OK);
    }

    @GetMapping("/getTags")
    public List<String> getTags() {
        return Tag.getAllTags();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPostsUsingTrigram(@RequestParam("content") String content) {
        List<Post> posts = postService.getPostsByContentUsingTrigram(content);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
    }

    @GetMapping("/posts/search/filtered")
    public ResponseEntity<List<Post>> searchPostsWithFilter(
            @RequestParam("content") String content,
            @RequestParam("tags") String tags
    ) {
        List<Post> posts = postService.getPostsByContentAndTags(content, tags);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
    }
}
