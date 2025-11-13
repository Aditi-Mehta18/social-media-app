package org.example.dao;

import org.example.config.MySQLConnection;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection conn = MySQLConnection.getConnection();

    public boolean save(User user) throws Exception {
        String sql = "INSERT INTO users (username, full_name, email, password_hash) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, user.getUsername());
        ps.setString(2, user.getFullName());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPasswordHash());

        return ps.executeUpdate() > 0;
    }


    public User findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM users WHERE email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);

        ResultSet rs = ps.executeQuery();
        return rs.next() ? map(rs) : null;
    }

    public User findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        return rs.next() ? map(rs) : null;
    }

    public User login(String email, String hash) throws Exception {
        String sql = "SELECT * FROM users WHERE email = ? AND password_hash = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, hash);

        ResultSet rs = ps.executeQuery();
        return rs.next() ? map(rs) : null;
    }

    public User findById(int id) throws Exception {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        return rs.next() ? map(rs) : null;
    }

    public List<User> searchByQuery(String q, int limit) throws Exception {
        String sql = "SELECT * FROM users WHERE username LIKE ? OR full_name LIKE ? ORDER BY user_id LIMIT ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        String like = "%" + q + "%";
        ps.setString(1, like);
        ps.setString(2, like);
        ps.setInt(3, Math.max(1, limit));
        List<User> list = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    private User map(ResultSet rs) throws Exception {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("created_at")
        );
    }
}