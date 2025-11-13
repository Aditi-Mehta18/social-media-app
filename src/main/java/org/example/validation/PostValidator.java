package org.example.validation;

import org.example.model.Post;

public final class PostValidator {
    private PostValidator() {}

    public static boolean isValidForCreate(Post p) {
        if (p == null) return false;
        if (p.getId() == null) return false;
        if (p.getUserId() <= 0) return false;
        return p.getContent() != null && !p.getContent().isBlank();
    }
}
