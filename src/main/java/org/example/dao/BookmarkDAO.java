package org.example.dao;

import org.bson.types.ObjectId;
import org.example.config.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookmarkDAO {

    private final Connection conn = MySQLConnection.getConnection();

    public boolean save(int userId, ObjectId postId) throws Exception {
        String sql = "INSERT INTO bookmarks (user_id, post_id, created_at) VALUES (?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, postId.toHexString());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean remove(int userId, ObjectId postId) throws Exception {
        String sql = "DELETE FROM bookmarks WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, postId.toHexString());
            return ps.executeUpdate() > 0;
        }
    }

    public List<ObjectId> listPostIds(int userId) throws Exception {
        String sql = "SELECT post_id FROM bookmarks WHERE user_id = ? ORDER BY created_at DESC";
        List<ObjectId> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(new ObjectId(rs.getString(1)));
                }
            }
        }
        return ids;
    }
}
