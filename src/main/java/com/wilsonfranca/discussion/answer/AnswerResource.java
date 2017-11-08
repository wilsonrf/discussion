package com.wilsonfranca.discussion.answer;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.wilsonfranca.discussion.comments.Comment;
import com.wilsonfranca.discussion.comments.CommentResource;
import com.wilsonfranca.discussion.comments.CommentResourceAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by wilson on 04/11/17.
 */
public class AnswerResource extends Resource<Answer> {


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private PagedResources<CommentResource> comments;

    public AnswerResource(Answer content){
        super(content);

        CommentResourceAssembler commentResourceAssembler =
                new CommentResourceAssembler();

        List<Comment> commentses =
                Optional
                        .ofNullable(content.getEmbeddedCommentses())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(comment -> {
                            return comment;
                        })
                        .limit(Answer.MAX_COMMENTS)
                        .collect(Collectors.toList());
        if (!commentses.isEmpty()) {
            comments = new PagedResources<CommentResource>(commentResourceAssembler.toResources(commentses),
                    new PagedResources.PageMetadata(Answer.MAX_COMMENTS, 0, content.getTotalComments()));
        }
    }

    public PagedResources<CommentResource> getComments() {
        return comments;
    }

    public void setComments(PagedResources<CommentResource> comments) {
        this.comments = comments;
    }
}
