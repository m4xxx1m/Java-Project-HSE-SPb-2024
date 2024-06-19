package com.example.server.service;

import com.example.server.dto.PostDto;
import com.example.server.model.Post;
import com.example.server.repository.CommentRepository;
import com.example.server.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
@SpringBootTest
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private SavedObjectService savedObjectService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private RatedObjectService ratedObjectService;

    @Mock
    private FileInfoService fileInfoService;

    @Test
    public void testGetPostById() {
        Post post = new Post(1, "title", "content", "tags", 0);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        Post resultPost = postService.getPostById(1);
        assertEquals(post.getTitle(), resultPost.getTitle());
    }

    @Test
    public void testGetPosts() {
        Post post1 = new Post(1, "title1", "content1", "tags1", 0);
        Post post2 = new Post(2, "title2", "content2", "tags2", 0);
        Page<Post> page = new PageImpl<>(Arrays.asList(post1, post2));
        when(postRepository.findAll(any(Pageable.class))).thenReturn(page);
        List<Post> resultPosts = postService.getPostsAfterId(-1, 10);
        assertEquals(2, resultPosts.size());
    }

    @Test
    public void testAddPost() throws IOException {
        PostDto postDto = new PostDto(1, "title", "content", "tags", 0, false);
        Post post = new Post(1, "title", "content", "tags", 0);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        Post resultPost = postService.addPost(postDto);
        assertEquals(post.getTitle(), resultPost.getTitle());
    }

    @Test
    public void testDeletePost() throws IOException {
        Post post = new Post(1, "title", "content", "tags", 0);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        assertDoesNotThrow(() -> postService.deletePost(1));
    }

    @Test
    public void testEditPost() throws IOException {
        Post post = new Post(1, "title", "content", "tags", 0);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        PostDto postDto = new PostDto(1, "title", "content", "tags", 0, false);
        postService.editPost(post, postDto);
        Mockito.verify(postRepository, Mockito.times(1)).save(post);
    }

    @Test
    public void testIncrementPostRating() {
        Post post = new Post(1, "title", "content", "tags", 0);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        postService.incrementPostRating(1, 1);
        assertEquals(1, post.getRating());
    }

    @Test
    public void testDecrementPostRating() {
        Post post = new Post(1, "title", "content", "tags", 0);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        postService.decrementPostRating(1, 1);
        assertEquals(-1, post.getRating());
    }

    @Test
    public void testGetPostByAuthorId() {
        Post post1 = new Post(1, "title", "content", "tags", 0);
        Post post2 = new Post(1, "title", "content", "tags", 0);
        Post post3 = new Post(2, "title", "content", "tags", 0);
        when(postRepository.findByAuthorId(1)).thenReturn(Arrays.asList(post1, post2));
        List<Post> resultPosts = postService.getPostsByAuthorId(1);
        assertEquals(2, resultPosts.size());
    }

    @Test
    public void testGetPostsBySelectedTagsAfterId() {
        Post post1 = new Post(1, "title", "content", "1100000", 0);
        Post post2 = new Post(2, "title", "content", "0100000", 0);
        Page<Post> page = new PageImpl<>(Arrays.asList(post1, post2));
        when(postRepository.findByTagsLike(eq("1100000"), any(Pageable.class))).thenReturn(page.getContent());
        List<Post> resultPosts = postService.getPostsBySelectedTagsAfterId("1100000", -1, 10);
        assertEquals(2, resultPosts.size());
    }

    @Test
    public void testGetPostsByContent() {
        Post post1 = new Post(1, "title", "content", "tags", 0);
        Post post2 = new Post(2, "title", "content1", "tags", 0);
        Page<Post> page = new PageImpl<>(Arrays.asList(post1, post2));
        when(postRepository.findByContentUsingTrigram(eq("content"))).thenReturn(page.getContent());
        List<Post> resultPosts = postService.getPostsByContentUsingTrigram("content");
        assertEquals(2, resultPosts.size());
    }

    @Test
    public void testUpdateCommentsCount() {
        Post post = new Post(1, "title", "content", "tags", 0);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        postService.changeCommentsCount(1, 1);
        assertEquals(1, post.getCommentsCount());
    }

}
