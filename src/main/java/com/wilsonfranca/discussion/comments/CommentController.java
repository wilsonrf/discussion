package com.wilsonfranca.discussion.comments;

import com.wilsonfranca.discussion.question.QuestionController;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by wilson on 02/11/17.
 */
@RestController
public class CommentController {

    private static final String QUESTION = "question";
    private static final String ANSWER = "answer";

    private CommentService commentService;

    public CommentController(@Autowired final CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "{parent}/{parentId}/comment")
    public ResponseEntity<?> createQuestionComment(@PathVariable String parent,
                                                   @PathVariable final ObjectId parentId,
                                                   @RequestBody final CommentRepresentation commentRepresentation) {

        ResponseEntity<?> responseEntity = null;

        if(QUESTION.equalsIgnoreCase(parent) || ANSWER.equalsIgnoreCase(parent)) {
            Optional<Comment> commentOptional = commentService.create(parent, parentId, commentRepresentation);
            responseEntity = commentOptional.filter(Objects::nonNull)
                    .map(comment -> {
                        Link self = linkTo(methodOn(CommentController.class).get(parent, parentId, comment.getId())).withSelfRel();
                        Link parentLink = null;
                        if(QUESTION.equalsIgnoreCase(parent)) {
                            //parentLink = linkTo(methodOn(QuestionController.class).get(parentId)).withRel(QUESTION);
                        } else if(ANSWER.equalsIgnoreCase(parent)) {
                            //parentLink = linkTo(methodOn(AnswerController.class).get(parentId)).withRel(QUESTION);
                        }
                        Resource<Comment> resource = new Resource<Comment>(comment, self);
                        return ResponseEntity.created(URI.create(self.getHref())).body(resource);
                    }).orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        } else {
            responseEntity = getParentBadRequestEntity(parent);
        }

        return responseEntity;

    }

    @RequestMapping(method = RequestMethod.GET, path = "{parent}/{parentId}/comment/{id}")
    public ResponseEntity<?> get(@PathVariable final String parent,
                                 @PathVariable final ObjectId parentId,
                                 @PathVariable final ObjectId id) {

        ResponseEntity<?> responseEntity = null;
        if(QUESTION.equalsIgnoreCase(parent) || ANSWER.equalsIgnoreCase(parent)) {
            Optional<Comment> commentOptional = commentService.get(parent, parentId, id);
            responseEntity = commentOptional
                    .filter(Objects::nonNull)
                    .map(comment -> {
                        Link self = linkTo(methodOn(CommentController.class).get(parent, parentId, comment.getId())).withSelfRel();
                        Link parentLink = null;
                        if(QUESTION.equalsIgnoreCase(parent)) {
                            parentLink = linkTo(methodOn(QuestionController.class).get(parentId)).withRel(QUESTION);
                        } else if(ANSWER.equalsIgnoreCase(parent)) {
                            //parentLink = linkTo(methodOn(AnswerController.class).get(parentId)).withRel(QUESTION);
                        }
                        Resource<Comment> resource = new Resource<Comment>(comment, self, parentLink);
                        return ResponseEntity.ok().body(resource);
                    }).orElse(ResponseEntity.notFound().build());
        } else {
            responseEntity = getParentBadRequestEntity(parent);
        }

        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "{parent}/{parentId}/comment/{id}")
    public ResponseEntity<?> delete(@PathVariable final String parent,
                                    @PathVariable final ObjectId parentId,
                                    @PathVariable final ObjectId id) {

        ResponseEntity<?> responseEntity = null;
        if(QUESTION.equalsIgnoreCase(parent) || ANSWER.equalsIgnoreCase(parent)) {
            Optional<Comment> commentOptional = commentService.inactivate(parent, parentId, id);
            commentOptional.filter(Objects::nonNull)
                    .filter(Comment::isActive)
                    .map(comment -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                    .orElse(ResponseEntity.noContent().build());
        } else {
            responseEntity = getParentBadRequestEntity(parent);
        }

        return responseEntity;

    }

    private ResponseEntity<?> getParentBadRequestEntity(String parent) {
        ResponseEntity<?> responseEntity;Map<String, String> errors = new HashMap<>();
        errors.put("parent.error", String.format("Parent not valid [%s]", parent));
        responseEntity = ResponseEntity.badRequest().body(errors);
        return responseEntity;
    }
}
