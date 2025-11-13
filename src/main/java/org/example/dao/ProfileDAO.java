package org.example.dao;

import org.example.config.MySQLConnection;
import org.example.model.Profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileDAO {

    private final Connection conn = MySQLConnection.getConnection();

    public boolean save(Profile profile) throws Exception {
        String sql = "INSERT INTO profiles (user_id, bio, location, created_at) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, profile.getUserId());
        ps.setString(2, profile.getBio());
        ps.setString(3, profile.getLocation());
        ps.setString(4, profile.getCreatedAt());

        return ps.executeUpdate() > 0;
    }

    public Profile findByUserId(int userId) throws Exception {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Profile(
                    rs.getInt("profile_id"),
                    rs.getInt("user_id"),
                    rs.getString("bio"),
                    rs.getString("location"),
                    rs.getString("created_at")
            );
        }
        return null;
    }

    public boolean update(Profile profile) throws Exception {
        String sql = "UPDATE profiles SET bio = ?, location = ? WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, profile.getBio());
        ps.setString(2, profile.getLocation());
        ps.setInt(3, profile.getUserId());
        return ps.executeUpdate() > 0;
    }
}