package org.example.model;

import org.bson.types.ObjectId;

public class Like {
    private ObjectId id;
    private ObjectId postId;
    private int userId;
    private long likedAt;

    public Like() {}

    public Like(ObjectId id, ObjectId postId, int userId, long likedAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.likedAt = likedAt;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public ObjectId getPostId() { return postId; }
    public void setPostId(ObjectId postId) { this.postId = postId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public long getLikedAt() { return likedAt; }
    public void setLikedAt(long likedAt) { this.likedAt = likedAt; }
}