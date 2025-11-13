package org.example.model;

import org.bson.types.ObjectId;

import java.util.List;

public class Post {
    private ObjectId id;
    private int userId;              // MySQL user reference
    private String content;
    private List<String> tags;
    private long createdAt;          // epoch millis

    public Post() {
    }

    public Post(ObjectId id, int userId, String content,
                List<String> tags, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.tags = tags;
        this.createdAt = createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}