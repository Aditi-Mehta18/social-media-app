package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.example.config.MongoConnection;
import org.example.model.Like;

import static com.mongodb.client.model.Filters.eq;

public class LikeDAO {

    private final MongoCollection<Like> collection;

    public LikeDAO() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("likes", Like.class);
    }

    public void save(Like like) {
        collection.insertOne(like);
    }

    public long countLikes(org.bson.types.ObjectId postId) {
        return collection.countDocuments(eq("postId", postId));
    }
}