package org.example.service;

import org.example.dao.ProfileDAO;
import org.example.model.Profile;

public class ProfileService {

    private final ProfileDAO profileDAO;

    public ProfileService() {
        this.profileDAO = new ProfileDAO();
    }

    public ProfileService(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    public boolean createProfile(Profile profile) throws Exception {
        return profileDAO.save(profile);
    }

    public Profile getProfileByUserId(int userId) throws Exception {
        return profileDAO.findByUserId(userId);
    }

    public boolean updateProfile(Profile profile) throws Exception {
        return profileDAO.update(profile);
    }
}