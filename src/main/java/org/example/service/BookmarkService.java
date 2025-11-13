package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.BookmarkDAO;
import org.example.dao.PostDAO;
import org.example.model.Post;

import java.util.List;

public class BookmarkService {

    private final BookmarkDAO bookmarkDAO;
    private final PostDAO postDAO;

    public BookmarkService() {
        this.bookmarkDAO = new BookmarkDAO();
        this.postDAO = new PostDAO();
    }

    public BookmarkService(BookmarkDAO bookmarkDAO, PostDAO postDAO) {
        this.bookmarkDAO = bookmarkDAO;
        this.postDAO = postDAO;
    }

    public boolean save(int userId, ObjectId postId) throws Exception {
        return bookmarkDAO.save(userId, postId);
    }

    public boolean remove(int userId, ObjectId postId) throws Exception {
        return bookmarkDAO.remove(userId, postId);
    }

    public List<Post> list(int userId) throws Exception {
        List<ObjectId> ids = bookmarkDAO.listPostIds(userId);
        return postDAO.findByIds(ids);
    }
}
