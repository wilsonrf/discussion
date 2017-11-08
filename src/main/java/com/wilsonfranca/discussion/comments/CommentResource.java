package com.wilsonfranca.discussion.comments;

import org.springframework.hateoas.Resource;

/**
 * Created by wilson on 05/11/17.
 */
public class CommentResource extends Resource<Comment> {

    public CommentResource(final Comment comment) {
        super(comment);
    }
}
