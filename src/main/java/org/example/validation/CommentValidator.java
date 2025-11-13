package org.example.validation;

import org.example.model.Comment;

public final class CommentValidator {
    private CommentValidator() {}

    public static boolean isValidForCreate(Comment c) {
        if (c == null) return false;
        if (c.getPostId() == null) return false;
        if (c.getUserId() <= 0) return false;
        return c.getText() != null && !c.getText().isBlank();
    }
}
