package org.example.service;

import org.example.dao.PostDAO;
import org.example.model.Post;
import org.bson.types.ObjectId;

import java.util.List;

public class PostService {

    private final PostDAO postDAO = new PostDAO();

    public ObjectId createPost(Post post) {
        return postDAO.save(post);
    }

    public List<Post> getAllPosts() {
        return postDAO.findAll();
    }

    public Post getPost(ObjectId id) {
        return postDAO.findById(id);
    }
}