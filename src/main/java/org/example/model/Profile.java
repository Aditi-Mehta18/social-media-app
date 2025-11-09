package org.example.model;

public class Profile {
    private int profileId;
    private int userId;       // FK -> users.user_id
    private String bio;
    private String location;
    private String createdAt;

    public Profile() {}

    public Profile(int profileId, int userId, String bio,
                   String location, String createdAt) {
        this.profileId = profileId;
        this.userId = userId;
        this.bio = bio;
        this.location = location;
        this.createdAt = createdAt;
    }

    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}