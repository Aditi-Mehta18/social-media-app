package org.example;

import org.example.dto.FeedItem;
import org.example.model.Comment;
import org.example.model.Post;
import org.example.model.User;
import org.example.model.Profile;
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
            if (ok) log.info("Now following.");
            else log.warn("Could not follow.");
        } catch (Exception e) {
            log.error("Follow failed: {}", e.getMessage());
        }
    }

    public static void unfollowUser(User loggedInUser, FollowService followService, Scanner sc) {
        try {
            System.out.print("Enter user id to unfollow: ");
            int targetId = Integer.parseInt(sc.nextLine());
            boolean ok = followService.unfollow(loggedInUser.getUserId(), targetId);
            if (ok) log.info("Unfollowed.");
            else log.warn("Could not unfollow.");
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
            if (users.isEmpty()) {
                log.info("No users found.");
                return;
            }
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
        if (posts.isEmpty()) {
            log.info("No posts found.");
            return;
        }
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
        if (posts.isEmpty()) {
            log.info("No posts yet.");
            return;
        }
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

    public static void createOrUpdateProfile(User loggedInUser, ProfileService profileService, Scanner sc) {
        try {
            Profile existing = profileService.getProfileByUserId(loggedInUser.getUserId());
            System.out.print("Bio (leave blank to keep): ");
            String bio = sc.nextLine();
            System.out.print("Location (leave blank to keep): ");
            String loc = sc.nextLine();

            if (existing == null) {
                Profile p = new Profile();
                p.setUserId(loggedInUser.getUserId());
                p.setBio(bio == null ? "" : bio);
                p.setLocation(loc == null ? "" : loc);
                p.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()).toString());
                boolean ok = profileService.createProfile(p);
                if (ok) log.info("Profile created."); else log.warn("Could not create profile.");
            } else {
                if (bio != null && !bio.isBlank()) existing.setBio(bio);
                if (loc != null && !loc.isBlank()) existing.setLocation(loc);
                boolean ok = profileService.updateProfile(existing);
                if (ok) log.info("Profile updated."); else log.warn("No changes applied.");
            }
        } catch (Exception e) {
            log.error("Profile operation failed: {}", e.getMessage());
        }
    }

    public static void viewMyProfile(User loggedInUser, ProfileService profileService) {
        try {
            Profile p = profileService.getProfileByUserId(loggedInUser.getUserId());
            log.info("\n=== MY PROFILE ===");
            if (p == null) { log.info("No profile found."); return; }
            log.info("User ID: {}", p.getUserId());
            log.info("Bio: {}", p.getBio());
            log.info("Location: {}", p.getLocation());
            log.info("CreatedAt: {}", p.getCreatedAt());
        } catch (Exception e) {
            log.error("Load profile failed: {}", e.getMessage());
        }
    }

    public static void viewUserProfile(ProfileService profileService, Scanner sc) {
        try {
            System.out.print("Enter user id: ");
            int uid = Integer.parseInt(sc.nextLine());
            Profile p = profileService.getProfileByUserId(uid);
            log.info("\n=== USER PROFILE ===");
            if (p == null) { log.info("No profile found."); return; }
            log.info("User ID: {}", p.getUserId());
            log.info("Bio: {}", p.getBio());
            log.info("Location: {}", p.getLocation());
            log.info("CreatedAt: {}", p.getCreatedAt());
        } catch (Exception e) {
            log.error("Load profile failed: {}", e.getMessage());
        }
    }

    // --- Post edit/delete ---
    public static void editPost(User loggedInUser, PostService postService, Scanner sc) {
        try {
            System.out.print("Enter Post ID to edit: ");
            String id = sc.nextLine();
            System.out.print("New content: ");
            String content = sc.nextLine();
            boolean ok = postService.editPost(new org.bson.types.ObjectId(id), loggedInUser.getUserId(), content);
            if (ok) log.info("Post updated."); else log.warn("Could not update post (check ownership/ID).");
        } catch (Exception e) {
            log.error("Edit failed: {}", e.getMessage());
        }
    }

    public static void deletePost(User loggedInUser, PostService postService, Scanner sc) {
        try {
            System.out.print("Enter Post ID to delete: ");
            String id = sc.nextLine();
            boolean ok = postService.deletePost(new org.bson.types.ObjectId(id), loggedInUser.getUserId());
            if (ok) log.info("Post deleted."); else log.warn("Could not delete post (check ownership/ID).");
        } catch (Exception e) {
            log.error("Delete failed: {}", e.getMessage());
        }
    }

    // --- Tags / Trending ---
    public static void trendingTags(PostService postService, Scanner sc) {
        System.out.print("Limit (e.g., 10): ");
        String inp = sc.nextLine();
        int limit = 10;
        try { if (!inp.isBlank()) limit = Integer.parseInt(inp); } catch (Exception ignored) {}
        List<String> tags = postService.topTags(limit);
        log.info("\n=== TRENDING TAGS ===");
        if (tags.isEmpty()) { log.info("No tags yet."); return; }
        int i = 1; for (String t : tags) log.info("{}. {}", i++, t);
    }

    public static void viewPostsByTag(PostService postService, LikeService likeService, CommentService commentService, Scanner sc) {
        System.out.print("Enter tag: ");
        String tag = sc.nextLine();
        System.out.print("Limit: ");
        int limit = Integer.parseInt(sc.nextLine());
        List<Post> posts = postService.findByTag(tag, limit);
        log.info("\n=== POSTS FOR TAG: {} ===", tag);
        if (posts.isEmpty()) { log.info("No posts for this tag."); return; }
        for (Post p : posts) {
            long likeCount = likeService.countLikes(p.getId());
            List<Comment> comments = commentService.getComments(p.getId());
            log.info("\nPost ID: {}", p.getId());
            log.info("User ID: {}", p.getUserId());
            log.info("Content: {}", p.getContent());
            log.info("Likes: {}", likeCount);
            if (!comments.isEmpty()) {
                String cs = comments.stream().map(Comment::getText).collect(Collectors.joining(", "));
                log.info("Comments: {}", cs);
            }
        }
    }

    // --- Bookmarks ---
    public static void saveBookmark(User loggedInUser, BookmarkService bookmarkService, Scanner sc) {
        try {
            System.out.print("Enter Post ID to bookmark: ");
            String id = sc.nextLine();
            boolean ok = bookmarkService.save(loggedInUser.getUserId(), new org.bson.types.ObjectId(id));
            if (ok) log.info("Saved bookmark."); else log.warn("Bookmark already exists or failed.");
        } catch (Exception e) {
            log.error("Save bookmark failed: {}", e.getMessage());
        }
    }

    public static void unsaveBookmark(User loggedInUser, BookmarkService bookmarkService, Scanner sc) {
        try {
            System.out.print("Enter Post ID to remove from bookmarks: ");
            String id = sc.nextLine();
            boolean ok = bookmarkService.remove(loggedInUser.getUserId(), new org.bson.types.ObjectId(id));
            if (ok) log.info("Removed bookmark."); else log.warn("No such bookmark or failed.");
        } catch (Exception e) {
            log.error("Unsave bookmark failed: {}", e.getMessage());
        }
    }

    public static void viewBookmarks(User loggedInUser, BookmarkService bookmarkService, LikeService likeService, CommentService commentService) {
        try {
            List<Post> posts = bookmarkService.list(loggedInUser.getUserId());
            log.info("\n=== MY BOOKMARKS ===");
            if (posts.isEmpty()) { log.info("No bookmarks yet."); return; }
            for (Post p : posts) {
                long likeCount = likeService.countLikes(p.getId());
                List<Comment> comments = commentService.getComments(p.getId());
                log.info("\nPost ID: {}", p.getId());
                log.info("User ID: {}", p.getUserId());
                log.info("Content: {}", p.getContent());
                log.info("Likes: {}", likeCount);
                if (!comments.isEmpty()) {
                    String cs = comments.stream().map(Comment::getText).collect(Collectors.joining(", "));
                    log.info("Comments: {}", cs);
                }
            }
        } catch (Exception e) {
            log.error("Load bookmarks failed: {}", e.getMessage());
        }
    }
}
