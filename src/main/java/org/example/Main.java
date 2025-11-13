package org.example;

import org.example.model.*;
import org.example.service.*;
import org.bson.types.ObjectId;
import org.example.dto.FeedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static UserService userService = new UserService();
    static PostService postService = new PostService();
    static LikeService likeService = new LikeService();
    static CommentService commentService = new CommentService();
    static FollowService followService = new FollowService();
    static FeedService feedService = new FeedService();
    static SearchService searchService = new SearchService();

    static User loggedInUser = null;

    public static void main(String[] args) throws Exception {

        while (true) {
            if (loggedInUser == null) {
                log.info("\n=== SOCIAL MEDIA APP ===");
                log.info("1. Register");
                log.info("2. Login");
                log.info("3. Exit");
                System.out.print("Choose: ");

                int ch = Integer.parseInt(sc.nextLine());
                switch (ch) {
                    case 1 -> { register(); if (loggedInUser != null) authenticatedMenu(); }
                    case 2 -> { login(); if (loggedInUser != null) authenticatedMenu(); }
                    case 3 -> System.exit(0);
                    default -> log.warn("Invalid choice.");
                }
            } else {
                authenticatedMenu();
            }
        }
    }

    static void authenticatedMenu() {
        while (loggedInUser != null) {
            log.info("\n=== SOCIAL MEDIA APP ===");
            log.info("Logged in as: {}", loggedInUser.getUsername());
            log.info("1. Create Post");
            log.info("2. View Feed (All Posts)");
            log.info("3. Like a Post");
            log.info("4. Comment on Post");
            log.info("5. View Timeline (Followees)");
            log.info("6. Follow a User");
            log.info("7. Unfollow a User");
            log.info("8. Search Users");
            log.info("9. Search Posts");
            log.info("10. View My Posts");
            log.info("11. Logout");
            System.out.print("Choose: ");

            int ch = Integer.parseInt(sc.nextLine());
            switch (ch) {
                case 1 -> createPost();
                case 2 -> viewFeed();
                case 3 -> likePost();
                case 4 -> commentOnPost();
                case 5 -> MenuExtensions.viewTimeline(loggedInUser, feedService, likeService, commentService, sc);
                case 6 -> MenuExtensions.followUser(loggedInUser, followService, sc);
                case 7 -> MenuExtensions.unfollowUser(loggedInUser, followService, sc);
                case 8 -> MenuExtensions.searchUsers(searchService, sc);
                case 9 -> MenuExtensions.searchPosts(searchService, sc);
                case 10 -> MenuExtensions.viewMyPosts(loggedInUser, postService, likeService, commentService, sc);
                case 11 -> logout();
                default -> log.warn("Invalid choice.");
            }
        }
    }


    static void register() throws Exception {
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Full Name: ");
        String fullName = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        User u = new User();
        u.setUsername(username);
        u.setFullName(fullName);
        u.setEmail(email);

        boolean ok = userService.register(u, pass);

        if (ok) {
            log.info("Registered successfully. Please login from the main menu.");
        } else {
            log.warn("User already exists (email/username).");
        }
    }


    static void login() throws Exception {
        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        User u = userService.login(email, pass);

        if (u == null) {
            log.warn("Invalid credentials.");
        } else {
            loggedInUser = u;
            log.info("Login successful. Welcome {}", u.getFullName());
            authenticatedMenu();
        }
    }



    static void createPost() {
        if (loggedInUser == null) {
            log.warn("Please log in first.");
            return;
        }

        System.out.print("Enter post content: ");
        String content = sc.nextLine();

        Post p = new Post();
        p.setId(new ObjectId());
        p.setUserId(loggedInUser.getUserId());
        p.setContent(content);
        p.setTags(List.of());
        p.setCreatedAt(System.currentTimeMillis());

        postService.createPost(p);

        log.info("Post created.");
    }


    static void viewFeed() {
        List<Post> posts = postService.getAllPosts();

        log.info("\n=== FEED ===");

        if (posts.isEmpty()) {
            log.info("No posts yet.");
            return;
        }

        for (Post p : posts) {

            long likeCount = likeService.countLikes(p.getId());
            List<Comment> comments = commentService.getComments(p.getId());

            log.info("\nPost ID: {}", p.getId());
            log.info("User ID: {}", p.getUserId());
            log.info("Content: {}", p.getContent());
            log.info("Likes: {}", likeCount);
            log.info("Comments:");

            for (Comment c : comments) {
                log.info(" - ({}) {}", c.getUserId(), c.getText());
            }
        }
    }


    static void likePost() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter Post ID to Like: ");
        String id = sc.nextLine();

        Like like = new Like();
        like.setId(new ObjectId());
        like.setPostId(new ObjectId(id));
        like.setUserId(loggedInUser.getUserId());
        like.setLikedAt(System.currentTimeMillis());

        likeService.addLike(like);

        log.info("Liked.");
    }

    static void commentOnPost() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
            return;
        }

        System.out.print("Enter Post ID: ");
        String id = sc.nextLine();

        System.out.print("Comment: ");
        String text = sc.nextLine();

        Comment c = new Comment();
        c.setId(new ObjectId());
        c.setPostId(new ObjectId(id));
        c.setUserId(loggedInUser.getUserId());
        c.setText(text);
        c.setCommentedAt(System.currentTimeMillis());

        commentService.addComment(c);

        log.info("Comment added.");
    }


    static void logout() {
        loggedInUser = null;
        log.info("Logged out.");
    }
}