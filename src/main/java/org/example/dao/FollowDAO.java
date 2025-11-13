package org.example.dao;

import org.example.config.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FollowDAO {

    private final Connection conn = MySQLConnection.getConnection();

    public boolean follow(int userId, int targetId) throws Exception {
        String sql = "INSERT INTO follows (user_id, target_id, created_at) VALUES (?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, targetId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean unfollow(int userId, int targetId) throws Exception {
        String sql = "DELETE FROM follows WHERE user_id = ? AND target_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, targetId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Integer> followingIds(int userId) throws Exception {
        String sql = "SELECT target_id FROM follows WHERE user_id = ?";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt(1));
                }
            }
        }
        return ids;
    }
}
