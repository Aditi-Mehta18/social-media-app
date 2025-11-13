package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.PostDAO;
import org.example.model.Post;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Test
    void createPost_returnsId_fromDao() {
        ObjectId id = new ObjectId();
        PostDAO dao = mock(PostDAO.class);
        when(dao.save(any(Post.class))).thenReturn(id);
        PostService service = new PostService(dao);
        Post p = new Post();
        p.setId(id);
        p.setUserId(1);
        p.setContent("hello");
        ObjectId out = service.createPost(p);
        assertEquals(id, out);
    }

    @Test
    void getAllPosts_returnsList_fromDao() {
        List<Post> posts = java.util.Arrays.asList(new Post(), new Post());
        PostDAO dao = mock(PostDAO.class);
        when(dao.findAll()).thenReturn(posts);
        PostService service = new PostService(dao);
        List<Post> out = service.getAllPosts();
        assertEquals(2, out.size());
    }

    @Test
    void getPost_returns_fromDao() {
        ObjectId id = new ObjectId();
        Post p = new Post();
        PostDAO dao = mock(PostDAO.class);
        when(dao.findById(id)).thenReturn(p);
        PostService service = new PostService(dao);
        Post out = service.getPost(id);
        assertEquals(p, out);
    }
}
