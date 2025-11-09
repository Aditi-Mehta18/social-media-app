package org.example.service;

import org.example.dao.ProfileDAO;
import org.example.model.Profile;

public class ProfileService {

    private final ProfileDAO profileDAO = new ProfileDAO();

    public boolean createProfile(Profile profile) throws Exception {
        return profileDAO.save(profile);
    }

    public Profile getProfileByUserId(int userId) throws Exception {
        return profileDAO.findByUserId(userId);
    }
}