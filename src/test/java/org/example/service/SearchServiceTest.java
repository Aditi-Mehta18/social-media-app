package org.example.service;

import org.example.dao.PostDAO;
import org.example.dao.UserDAO;
import org.example.model.Post;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearchServiceTest {

    @Test
    void searchUsers_returns_results_from_dao() throws Exception {
        UserDAO userDAO = mock(UserDAO.class);
        PostDAO postDAO = mock(PostDAO.class);
        SearchService service = new SearchService(userDAO, postDAO);

        List<User> users = Arrays.asList(new User(), new User());
        when(userDAO.searchByQuery("ad", 5)).thenReturn(users);

        List<User> out = service.searchUsers("ad", 5);
        assertEquals(2, out.size());
    }

    @Test
    void searchPosts_returns_results_from_dao() {
        UserDAO userDAO = mock(UserDAO.class);
        PostDAO postDAO = mock(PostDAO.class);
        SearchService service = new SearchService(userDAO, postDAO);

        List<Post> posts = Arrays.asList(new Post(), new Post(), new Post());
        when(postDAO.findByText("java", 10)).thenReturn(posts);

        List<Post> out = service.searchPosts("java", 10);
        assertEquals(3, out.size());
    }

    @Test
    void empty_query_returns_empty_lists() throws Exception {
        SearchService service = new SearchService(mock(UserDAO.class), mock(PostDAO.class));
        assertTrue(service.searchUsers(" ", 10).isEmpty());
        assertTrue(service.searchPosts(null, 10).isEmpty());
    }
}
