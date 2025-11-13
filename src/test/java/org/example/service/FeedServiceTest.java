package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.CommentDAO;
import org.example.dao.LikeDAO;
import org.example.dao.PostDAO;
import org.example.dao.UserDAO;
import org.example.dto.FeedItem;
import org.example.model.Comment;
import org.example.model.Post;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FeedServiceTest {

    @Test
    void timeline_empty_when_no_followees() throws Exception {
        FollowService followService = mock(FollowService.class);
        PostDAO postDAO = mock(PostDAO.class);
        LikeDAO likeDAO = mock(LikeDAO.class);
        CommentDAO commentDAO = mock(CommentDAO.class);
        UserDAO userDAO = mock(UserDAO.class);

        when(followService.followingIds(10)).thenReturn(List.of());

        FeedService service = new FeedService(followService, postDAO, likeDAO, commentDAO, userDAO);
        List<FeedItem> out = service.timelineFor(10, 10);
        assertTrue(out.isEmpty());
        verifyNoInteractions(postDAO, likeDAO, commentDAO, userDAO);
    }

    @Test
    void timeline_returns_posts_with_aggregates() throws Exception {
        int userId = 10;
        int authorId = 20;
        ObjectId postId = new ObjectId();

        FollowService followService = mock(FollowService.class);
        PostDAO postDAO = mock(PostDAO.class);
        LikeDAO likeDAO = mock(LikeDAO.class);
        CommentDAO commentDAO = mock(CommentDAO.class);
        UserDAO userDAO = mock(UserDAO.class);

        when(followService.followingIds(userId)).thenReturn(List.of(authorId));

        Post p = new Post();
        p.setId(postId);
        p.setUserId(authorId);
        p.setContent("hello world");
        p.setCreatedAt(System.currentTimeMillis());
        when(postDAO.findByUserIds(eq(List.of(authorId)), anyInt())).thenReturn(List.of(p));

        when(likeDAO.countLikes(postId)).thenReturn(3L);

        Comment c1 = new Comment(); c1.setText("nice");
        Comment c2 = new Comment(); c2.setText("great");
        when(commentDAO.findByPostId(postId)).thenReturn(Arrays.asList(c1, c2));

        User author = new User(); author.setUserId(authorId); author.setUsername("author");
        when(userDAO.findById(authorId)).thenReturn(author);

        FeedService service = new FeedService(followService, postDAO, likeDAO, commentDAO, userDAO);
        List<FeedItem> out = service.timelineFor(userId, 10);

        assertEquals(1, out.size());
        FeedItem item = out.get(0);
        assertEquals(postId, item.getPostId());
        assertEquals("author", item.getAuthorUsername());
        assertEquals(3L, item.getLikeCount());
        assertEquals(2, item.getTopComments().size());
        assertEquals(List.of("nice", "great"), item.getTopComments());
    }
}
