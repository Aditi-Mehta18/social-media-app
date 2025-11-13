package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Test
    void register_returnsFalse_whenEmailExists() throws Exception {
        UserDAO dao = mock(UserDAO.class);
        when(dao.findByEmail("e@x.com")).thenReturn(new User());
        UserService service = new UserService(dao);
        User u = new User();
        u.setEmail("e@x.com");
        u.setUsername("u1");
        boolean res = service.register(u, "pass");
        assertFalse(res);
    }

    @Test
    void register_returnsFalse_whenUsernameExists() throws Exception {
        UserDAO dao = mock(UserDAO.class);
        when(dao.findByEmail("e@x.com")).thenReturn(null);
        when(dao.findByUsername("u1")).thenReturn(new User());
        UserService service = new UserService(dao);
        User u = new User();
        u.setEmail("e@x.com");
        u.setUsername("u1");
        boolean res = service.register(u, "pass");
        assertFalse(res);
    }

    @Test
    void register_hashesPassword_andSaves_whenUnique() throws Exception {
        UserDAO dao = mock(UserDAO.class);
        when(dao.findByEmail("e@x.com")).thenReturn(null);
        when(dao.findByUsername("user1")).thenReturn(null);
        when(dao.save(any(User.class))).thenReturn(true);
        UserService service = new UserService(dao);
        User u = new User();
        u.setEmail("e@x.com");
        u.setUsername("user1");
        boolean res = service.register(u, "plain1234");
        assertTrue(res);
        assertNotNull(u.getPasswordHash());
        assertNotEquals("plain1234", u.getPasswordHash());
    }

    @Test
    void login_returnsNull_whenUserNotFound() throws Exception {
        UserDAO dao = mock(UserDAO.class);
        when(dao.findByEmail("e@x.com")).thenReturn(null);
        UserService service = new UserService(dao);
        assertNull(service.login("e@x.com", "p"));
    }

    @Test
    void login_returnsUser_whenPasswordMatches() throws Exception {
        User existing = new User();
        existing.setEmail("e@x.com");
        existing.setPasswordHash(org.mindrot.jbcrypt.BCrypt.hashpw("p", org.mindrot.jbcrypt.BCrypt.gensalt()));
        UserDAO dao = mock(UserDAO.class);
        when(dao.findByEmail("e@x.com")).thenReturn(existing);
        UserService service = new UserService(dao);
        User out = service.login("e@x.com", "p");
        assertNotNull(out);
        assertEquals(existing, out);
    }

    @Test
    void login_returnsNull_whenPasswordDoesNotMatch() throws Exception {
        User existing = new User();
        existing.setEmail("e@x.com");
        existing.setPasswordHash(org.mindrot.jbcrypt.BCrypt.hashpw("p", org.mindrot.jbcrypt.BCrypt.gensalt()));
        UserDAO dao = mock(UserDAO.class);
        when(dao.findByEmail("e@x.com")).thenReturn(existing);
        UserService service = new UserService(dao);
        User out = service.login("e@x.com", "q");
        assertNull(out);
    }

    @Test
    void getById_delegatesToDao() throws Exception {
        User existing = new User();
        existing.setUserId(42);
        UserDAO dao = mock(UserDAO.class);
        when(dao.findById(42)).thenReturn(existing);
        UserService service = new UserService(dao);
        User out = service.getById(42);
        assertEquals(42, out.getUserId());
    }
}
