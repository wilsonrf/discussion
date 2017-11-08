package com.wilsonfranca.discussion.question;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wilsonfranca.discussion.answer.Answer;
import com.wilsonfranca.discussion.answer.AnswerResource;
import com.wilsonfranca.discussion.answer.AnswerRespourceAssembler;
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
public class QuestionResource extends Resource<Question> {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private PagedResources<AnswerResource> answers;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private PagedResources<CommentResource> comments;

    public QuestionResource(Question content) {
        super(content);

        AnswerRespourceAssembler answerRespourceAssembler =
                new AnswerRespourceAssembler();

        CommentResourceAssembler commentResourceAssembler =
                new CommentResourceAssembler();

        List<Answer> answerses =
                Optional
                        .ofNullable(content.getEmbeddedAnswerses())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(embeddedAnswers -> {
                            return embeddedAnswers;
                        }).limit(Question.MAX_ANSWERS)
                        .collect(Collectors.toList());

        List<Comment> commentses =
                Optional
                        .ofNullable(content.getEmbeddedCommentses())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(comment -> {
                            return comment;
                        })
                        .limit(Question.MAX_COMMENTS)
                        .collect(Collectors.toList());

        if (!answerses.isEmpty()) {
            answers = new PagedResources<AnswerResource>(answerRespourceAssembler.toResources(answerses),
                    new PagedResources.PageMetadata(Question.MAX_ANSWERS, 0, content.getTotalAnswers()));
        }

        if(!commentses.isEmpty()) {
            comments = new PagedResources<CommentResource>(commentResourceAssembler.toResources(commentses),
                    new PagedResources.PageMetadata(Question.MAX_COMMENTS, 0, content.getTotalComments()));
        }
    }

    public PagedResources<AnswerResource> getAnswers() {
        return answers;
    }

    public void setAnswers(PagedResources<AnswerResource> answers) {
        this.answers = answers;
    }

    public PagedResources<CommentResource> getComments() {
        return comments;
    }

    public void setComments(PagedResources<CommentResource> comments) {
        this.comments = comments;
    }
}
