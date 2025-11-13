package org.example.dto;

import org.bson.types.ObjectId;

import java.util.List;

public class FeedItem {
    private ObjectId postId;
    private int authorId;
    private String authorUsername;
    private String content;
    private long createdAt;
    private long likeCount;
    private List<String> topComments;

    public ObjectId getPostId() {
        return postId;
    }

    public void setPostId(ObjectId postId) {
        this.postId = postId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public List<String> getTopComments() {
        return topComments;
    }

    public void setTopComments(List<String> topComments) {
        this.topComments = topComments;
    }
}
