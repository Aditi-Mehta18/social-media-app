package org.example;

import org.example.model.*;
import org.example.service.*;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static UserService userService = new UserService();
    static PostService postService = new PostService();
    static LikeService likeService = new LikeService();
    static CommentService commentService = new CommentService();

    // ✅ Correct variable name
    static User loggedInUser = null;

    public static void main(String[] args) throws Exception {

        while (true) {
            System.out.println("\n=== SOCIAL MEDIA APP ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Create Post");
            System.out.println("4. View Feed");
            System.out.println("5. Like a Post");
            System.out.println("6. Comment on Post");
            System.out.println("7. Logout");
            System.out.println("8. Exit");
            System.out.print("Choose: ");

            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> createPost();
                case 4 -> viewFeed();
                case 5 -> likePost();
                case 6 -> commentOnPost();
                case 7 -> logout();
                case 8 -> System.exit(0);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ------------------------------------------------------------

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

        if (ok) System.out.println("Registered successfully.");
        else System.out.println("User already exists (email/username).");
    }


    // ------------------------------------------------------------

    static void login() throws Exception {
        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        User u = userService.login(email, pass);

        if (u == null) {
            System.out.println("Invalid credentials.");
        } else {
            loggedInUser = u;
            System.out.println("Login successful. Welcome " + u.getFullName());
        }
    }

    // ------------------------------------------------------------

    static void createPost() {
        if (loggedInUser == null) {
            System.out.println("Please log in first.");
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

        System.out.println("Post created.");
    }

    // ------------------------------------------------------------

    static void viewFeed() {
        List<Post> posts = postService.getAllPosts();

        System.out.println("\n=== FEED ===");

        if (posts.isEmpty()) {
            System.out.println("No posts yet.");
            return;
        }

        for (Post p : posts) {

            long likeCount = likeService.countLikes(p.getId());
            List<Comment> comments = commentService.getComments(p.getId());

            System.out.println("\nPost ID: " + p.getId());
            System.out.println("User ID: " + p.getUserId());
            System.out.println("Content: " + p.getContent());
            System.out.println("Likes: " + likeCount);
            System.out.println("Comments:");

            for (Comment c : comments) {
                System.out.println(" - (" + c.getUserId() + ") " + c.getText());
            }
        }
    }

    // ------------------------------------------------------------

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

        System.out.println("Liked.");
    }

    // ------------------------------------------------------------

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

        System.out.println("Comment added.");
    }

    // ------------------------------------------------------------

    static void logout() {
        loggedInUser = null;
        System.out.println("Logged out.");
    }
}