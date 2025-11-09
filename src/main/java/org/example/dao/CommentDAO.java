package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.example.config.MongoConnection;
import org.example.model.Comment;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class CommentDAO {

    private final MongoCollection<Comment> collection;

    public CommentDAO() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("comments", Comment.class);
    }

    public void save(Comment comment) {
        collection.insertOne(comment);
    }

    public List<Comment> findByPostId(org.bson.types.ObjectId postId) {
        List<Comment> comments = new ArrayList<>();
        collection.find(eq("postId", postId)).into(comments);
        return comments;
    }
}