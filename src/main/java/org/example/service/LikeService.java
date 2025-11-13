package org.example.service;

import org.example.dao.LikeDAO;
import org.example.model.Like;
import org.bson.types.ObjectId;

public class LikeService {

    private final LikeDAO likeDAO;

    public LikeService() { this.likeDAO = new LikeDAO(); }
    public LikeService(LikeDAO likeDAO) { this.likeDAO = likeDAO; }

    public void addLike(Like like) {
        likeDAO.save(like);
    }

    public long countLikes(ObjectId postId) {
        return likeDAO.countLikes(postId);
    }
}