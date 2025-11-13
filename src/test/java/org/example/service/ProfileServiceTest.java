package org.example.service;

import org.example.dao.ProfileDAO;
import org.example.model.Profile;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileServiceTest {

    @Test
    void createProfile_returns_fromDao() throws Exception {
        Profile p = new Profile();
        try (MockedConstruction<ProfileDAO> mocked = mockConstruction(ProfileDAO.class, (mock, ctx) -> {
            when(mock.save(p)).thenReturn(true);
        })) {
            ProfileService service = new ProfileService();
            boolean out = service.createProfile(p);
            assertTrue(out);
        }
    }

    @Test
    void getProfileByUserId_returns_fromDao() throws Exception {
        Profile p = new Profile();
        try (MockedConstruction<ProfileDAO> mocked = mockConstruction(ProfileDAO.class, (mock, ctx) -> {
            when(mock.findByUserId(7)).thenReturn(p);
        })) {
            ProfileService service = new ProfileService();
            Profile out = service.getProfileByUserId(7);
            assertEquals(p, out);
        }
    }
}
