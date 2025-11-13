package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.CommentDAO;
import org.example.model.Comment;

import java.util.List;

public class CommentService {

    private final CommentDAO commentDAO;

    public CommentService() {
        this.commentDAO = new CommentDAO();
    }

    public CommentService(CommentDAO commentDAO) {
        this.commentDAO = commentDAO;
    }

    public void addComment(Comment comment) {
        commentDAO.save(comment);
    }

    public List<Comment> getComments(ObjectId postId) {
        return commentDAO.findByPostId(postId);
    }
}