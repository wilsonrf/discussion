package com.wilsonfranca.discussion.comments;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * Created by wilson on 05/11/17.
 */
public class CommentResourceAssembler extends ResourceAssemblerSupport<Comment, CommentResource> {

    public CommentResourceAssembler(){
        super(CommentController.class, CommentResource.class);
    }

    @Override
    public CommentResource toResource(Comment entity) {

        CommentResource commentResource =
                new CommentResource(entity);

        return commentResource;
    }
}
