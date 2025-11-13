package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.AggregateIterable;
import org.bson.types.ObjectId;
import org.example.config.MongoConnection;
import org.example.model.Post;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Accumulators.*;

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

    public boolean updateContent(ObjectId id, int userId, String content) {
        var result = collection.updateOne(and(eq("_id", id), eq("userId", userId)), set("content", content));
        return result.getModifiedCount() > 0;
    }

    public boolean delete(ObjectId id, int userId) {
        var result = collection.deleteOne(and(eq("_id", id), eq("userId", userId)));
        return result.getDeletedCount() > 0;
    }

    public List<Post> findByTag(String tag, int limit) {
        List<Post> posts = new ArrayList<>();
        if (tag == null || tag.isBlank()) return posts;
        var find = collection.find(eq("tags", tag)).sort(descending("createdAt"));
        if (limit > 0) find.limit(limit);
        find.into(posts);
        return posts;
    }

    public List<String> topTags(int limit) {
        List<String> tags = new ArrayList<>();
        List<Bson> pipeline = new ArrayList<>();
        pipeline.add(unwind("$tags"));
        pipeline.add(group("$tags", sum("count", 1)));
        pipeline.add(sort(descending("count")));
        if (limit > 0) pipeline.add(limit(limit));
        AggregateIterable<Document> agg = collection.aggregate(pipeline, Document.class);
        for (Document d : agg) {
            tags.add(d.getString("_id"));
        }
        return tags;
    }

    public List<Post> findByIds(List<ObjectId> ids) {
        List<Post> posts = new ArrayList<>();
        if (ids == null || ids.isEmpty()) return posts;
        collection.find(in("_id", ids)).sort(descending("createdAt")).into(posts);
        return posts;
    }
}