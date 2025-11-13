package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.LikeDAO;
import org.example.model.Like;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LikeServiceTest {

    @Test
    void addLike_callsDaoSave() {
        Like like = new Like();
        LikeDAO dao = mock(LikeDAO.class);
        LikeService service = new LikeService(dao);
        service.addLike(like);
        verify(dao, times(1)).save(like);
    }

    @Test
    void countLikes_returns_fromDao() {
        ObjectId postId = new ObjectId();
        LikeDAO dao = mock(LikeDAO.class);
        when(dao.countLikes(postId)).thenReturn(5L);
        LikeService service = new LikeService(dao);
        long out = service.countLikes(postId);
        assertEquals(5L, out);
    }
}
