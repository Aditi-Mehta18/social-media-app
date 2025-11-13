package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.CommentDAO;
import org.example.model.Comment;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Test
    void addComment_callsDaoSave() {
        Comment comment = new Comment();
        CommentDAO dao = mock(CommentDAO.class);
        CommentService service = new CommentService(dao);
        service.addComment(comment);
        verify(dao, times(1)).save(comment);
    }

    @Test
    void getComments_returns_fromDao() {
        ObjectId postId = new ObjectId();
        List<Comment> list = Arrays.asList(new Comment(), new Comment());
        CommentDAO dao = mock(CommentDAO.class);
        when(dao.findByPostId(postId)).thenReturn(list);
        CommentService service = new CommentService(dao);
        List<Comment> out = service.getComments(postId);
        assertEquals(2, out.size());
    }
}
