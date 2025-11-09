package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.example.config.MongoConnection;
import org.example.model.Post;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

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
}