package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.PostDAO;
import org.example.model.Post;
import org.example.validation.PostValidator;

import java.util.List;

public class PostService {

    private final PostDAO postDAO;

    public PostService() {
        this.postDAO = new PostDAO();
    }

    public PostService(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    public ObjectId createPost(Post post) {
        if (!PostValidator.isValidForCreate(post)) return null;
        return postDAO.save(post);
    }

    public List<Post> getAllPosts() {
        return postDAO.findAll();
    }

    public List<Post> getPostsByUserIds(List<Integer> userIds, int limit) {
        return postDAO.findByUserIds(userIds, limit);
    }

    public Post getPost(ObjectId id) {
        return postDAO.findById(id);
    }

    public boolean editPost(ObjectId id, int userId, String newContent) {
        if (newContent == null || newContent.isBlank()) return false;
        return postDAO.updateContent(id, userId, newContent);
    }

    public boolean deletePost(ObjectId id, int userId) {
        return postDAO.delete(id, userId);
    }

    public List<Post> findByTag(String tag, int limit) {
        return postDAO.findByTag(tag, limit);
    }

    public List<String> topTags(int limit) {
        return postDAO.topTags(limit);
    }
}