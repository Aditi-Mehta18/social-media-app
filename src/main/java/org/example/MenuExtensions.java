package org.example;

import org.bson.types.ObjectId;
import org.example.dto.FeedItem;
import org.example.model.Comment;
import org.example.model.Post;
import org.example.model.User;
import org.example.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuExtensions {

    private static final Logger log = LoggerFactory.getLogger(MenuExtensions.class);

    public static void viewTimeline(User loggedInUser,
                                    FeedService feedService,
                                    LikeService likeService,
                                    CommentService commentService,
                                    Scanner sc) {
        try {
            List<FeedItem> items = feedService.timelineFor(loggedInUser.getUserId(), 50);
            log.info("\n=== TIMELINE ===");
            if (items.isEmpty()) {
                log.info("No posts from followees.");
                return;
            }
            for (FeedItem item : items) {
                log.info("\nPost ID: {}", item.getPostId());
                log.info("Author: {} ({})", item.getAuthorUsername(), item.getAuthorId());
                log.info("Content: {}", item.getContent());
                log.info("Likes: {}", item.getLikeCount());
                log.info("Top Comments: {}", String.join(", ", item.getTopComments() == null ? List.of() : item.getTopComments()));
            }
        } catch (Exception e) {
            log.error("Failed to load timeline: {}", e.getMessage());
        }
    }

    public static void followUser(User loggedInUser, FollowService followService, Scanner sc) {
        try {
            System.out.print("Enter user id to follow: ");
            int targetId = Integer.parseInt(sc.nextLine());
            boolean ok = followService.follow(loggedInUser.getUserId(), targetId);
            if (ok) log.info("Now following."); else log.warn("Could not follow.");
        } catch (Exception e) {
            log.error("Follow failed: {}", e.getMessage());
        }
    }

    public static void unfollowUser(User loggedInUser, FollowService followService, Scanner sc) {
        try {
            System.out.print("Enter user id to unfollow: ");
            int targetId = Integer.parseInt(sc.nextLine());
            boolean ok = followService.unfollow(loggedInUser.getUserId(), targetId);
            if (ok) log.info("Unfollowed."); else log.warn("Could not unfollow.");
        } catch (Exception e) {
            log.error("Unfollow failed: {}", e.getMessage());
        }
    }

    public static void searchUsers(SearchService searchService, Scanner sc) {
        try {
            System.out.print("Enter search query (users): ");
            String q = sc.nextLine();
            System.out.print("Limit: ");
            int limit = Integer.parseInt(sc.nextLine());
            List<org.example.model.User> users = searchService.searchUsers(q, limit);
            log.info("\n=== USER SEARCH RESULTS ===");
            if (users.isEmpty()) { log.info("No users found."); return; }
            for (User u : users) {
                log.info("{} - {} ({})", u.getUserId(), u.getUsername(), u.getFullName());
            }
        } catch (Exception e) {
            log.error("Search failed: {}", e.getMessage());
        }
    }

    public static void searchPosts(SearchService searchService, Scanner sc) {
        System.out.print("Enter search query (posts): ");
        String q = sc.nextLine();
        System.out.print("Limit: ");
        int limit = Integer.parseInt(sc.nextLine());
        List<Post> posts = searchService.searchPosts(q, limit);
        log.info("\n=== POST SEARCH RESULTS ===");
        if (posts.isEmpty()) { log.info("No posts found."); return; }
        for (Post p : posts) {
            log.info("{} by {}: {}", p.getId(), p.getUserId(), p.getContent());
        }
    }

    public static void viewMyPosts(User loggedInUser,
                                   PostService postService,
                                   LikeService likeService,
                                   CommentService commentService,
                                   Scanner sc) {
        List<Integer> ids = new ArrayList<>();
        ids.add(loggedInUser.getUserId());
        List<Post> posts = postService.getPostsByUserIds(ids, 50);
        log.info("\n=== MY POSTS ===");
        if (posts.isEmpty()) { log.info("No posts yet."); return; }
        for (Post p : posts) {
            long likeCount = likeService.countLikes(p.getId());
            List<Comment> comments = commentService.getComments(p.getId());
            log.info("\nPost ID: {}", p.getId());
            log.info("Content: {}", p.getContent());
            log.info("Likes: {}", likeCount);
            if (!comments.isEmpty()) {
                String cs = comments.stream().map(Comment::getText).collect(Collectors.joining(", "));
                log.info("Comments: {}", cs);
            }
        }
    }
}
