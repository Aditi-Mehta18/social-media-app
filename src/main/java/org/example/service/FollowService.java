package org.example.service;

import org.example.dao.FollowDAO;

import java.util.List;

public class FollowService {

    private final FollowDAO followDAO;

    public FollowService() {
        this.followDAO = new FollowDAO();
    }

    public FollowService(FollowDAO followDAO) {
        this.followDAO = followDAO;
    }

    public boolean follow(int userId, int targetId) throws Exception {
        if (userId <= 0 || targetId <= 0 || userId == targetId) return false;
        return followDAO.follow(userId, targetId);
    }

    public boolean unfollow(int userId, int targetId) throws Exception {
        if (userId <= 0 || targetId <= 0 || userId == targetId) return false;
        return followDAO.unfollow(userId, targetId);
    }

    public List<Integer> followingIds(int userId) throws Exception {
        if (userId <= 0) return java.util.Collections.emptyList();
        return followDAO.followingIds(userId);
    }
}
