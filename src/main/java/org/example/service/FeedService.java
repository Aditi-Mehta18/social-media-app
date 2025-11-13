package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.CommentDAO;
import org.example.dao.LikeDAO;
import org.example.dao.PostDAO;
import org.example.dao.UserDAO;
import org.example.dto.FeedItem;
import org.example.model.Comment;
import org.example.model.Post;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedService {

    private final FollowService followService;
    private final PostDAO postDAO;
    private final LikeDAO likeDAO;
    private final CommentDAO commentDAO;
    private final UserDAO userDAO;

    public FeedService() {
        this.followService = new FollowService();
        this.postDAO = new PostDAO();
        this.likeDAO = new LikeDAO();
        this.commentDAO = new CommentDAO();
        this.userDAO = new UserDAO();
    }

    public FeedService(FollowService followService, PostDAO postDAO, LikeDAO likeDAO,
                       CommentDAO commentDAO, UserDAO userDAO) {
        this.followService = followService;
        this.postDAO = postDAO;
        this.likeDAO = likeDAO;
        this.commentDAO = commentDAO;
        this.userDAO = userDAO;
    }

    public List<FeedItem> timelineFor(int userId, int limit) throws Exception {
        if (userId <= 0) return List.of();
        List<Integer> following = followService.followingIds(userId);
        if (following.isEmpty()) return List.of();
        List<Post> posts = postDAO.findByUserIds(following, limit);
        List<FeedItem> items = new ArrayList<>();
        for (Post p : posts) {
            FeedItem item = new FeedItem();
            item.setPostId(p.getId());
            item.setAuthorId(p.getUserId());
            User author = userDAO.findById(p.getUserId());
            item.setAuthorUsername(author != null ? author.getUsername() : "unknown");
            item.setContent(p.getContent());
            item.setCreatedAt(p.getCreatedAt());
            item.setLikeCount(likeDAO.countLikes(p.getId()));
            List<Comment> comments = commentDAO.findByPostId(p.getId());
            item.setTopComments(comments.stream()
                    .limit(2)
                    .map(Comment::getText)
                    .collect(Collectors.toList()));
            items.add(item);
        }
        return items;
    }
}
