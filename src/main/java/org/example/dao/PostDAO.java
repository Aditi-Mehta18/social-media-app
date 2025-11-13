package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.example.config.MongoConnection;
import org.example.model.Post;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Filters.text;

public class PostDAO {

    private final MongoCollection<Post> collection;

    public PostDAO() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("posts", Post.class);
    }

    public ObjectId save(Post post) {
        collection.insertOne(post);
        return post.getId();
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        collection.find().into(posts);
        return posts;
    }

    public Post findById(ObjectId id) {
        return collection.find(eq("_id", id)).first();
    }

    public List<Post> findByUserIds(List<Integer> userIds, int limit) {
        List<Post> posts = new ArrayList<>();
        if (userIds == null || userIds.isEmpty()) return posts;
        var find = collection.find(in("userId", userIds)).sort(descending("createdAt"));
        if (limit > 0) find.limit(limit);
        find.into(posts);
        return posts;
    }

    public List<Post> findByText(String query, int limit) {
        List<Post> posts = new ArrayList<>();
        if (query == null || query.isBlank()) return posts;
        var find = collection.find(text(query)).sort(descending("createdAt"));
        if (limit > 0) find.limit(limit);
        find.into(posts);
        return posts;
    }
}