package org.example.service;

import org.example.dao.PostDAO;
import org.example.dao.UserDAO;
import org.example.model.Post;
import org.example.model.User;

import java.util.List;

public class SearchService {

    private final UserDAO userDAO;
    private final PostDAO postDAO;

    public SearchService() {
        this.userDAO = new UserDAO();
        this.postDAO = new PostDAO();
    }

    public SearchService(UserDAO userDAO, PostDAO postDAO) {
        this.userDAO = userDAO;
        this.postDAO = postDAO;
    }

    public List<User> searchUsers(String query, int limit) throws Exception {
        if (query == null || query.isBlank()) return List.of();
        return userDAO.searchByQuery(query, limit);
    }

    public List<Post> searchPosts(String query, int limit) {
        if (query == null || query.isBlank()) return List.of();
        return postDAO.findByText(query, limit);
    }
}
