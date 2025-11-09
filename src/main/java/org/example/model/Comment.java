package org.example.model;

import org.bson.types.ObjectId;

public class Comment {
    private ObjectId id;
    private ObjectId postId;
    private int userId;
    private String text;
    private long commentedAt;

    public Comment() {}

    public Comment(ObjectId id, ObjectId postId, int userId,
                   String text, long commentedAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.text = text;
        this.commentedAt = commentedAt;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public ObjectId getPostId() { return postId; }
    public void setPostId(ObjectId postId) { this.postId = postId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public long getCommentedAt() { return commentedAt; }
    public void setCommentedAt(long commentedAt) { this.commentedAt = commentedAt; }
}