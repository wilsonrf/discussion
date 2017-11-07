package com.wilsonfranca.discussion.answer;

import com.wilsonfranca.discussion.comments.CommentController;
import com.wilsonfranca.discussion.question.QuestionController;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by wilson.franca on 02/11/17.
 */
@RestController
public class AnswerController {

    private static Logger logger = LoggerFactory.getLogger(AnswerController.class);

    private AnswerService answerService;

    public AnswerController(@Autowired final AnswerService answerService) {
        this.answerService = answerService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "question/{questionId}/answer")
    public ResponseEntity<?> create(@PathVariable final ObjectId questionId,
                                    @Valid@RequestBody final AnswerRepresentation answerRepresentation,
                                    final BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().stream()
                    .filter(Objects::nonNull)
                    .forEach(fieldError -> {
                        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                    });
            return ResponseEntity.badRequest().body(errors);
        } else {

            Optional<Answer> optional = answerService.create(answerRepresentation, questionId);

            ResponseEntity<?> responseEntity = optional.filter(Objects::nonNull)
                    .map(question -> {
                        URI resourceUri = ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(question.getId().toHexString()).toUri();
                        return ResponseEntity.created(resourceUri).build();
                    })
                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

            return responseEntity;
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "question/{questionId}/answer/{id}")
    public ResponseEntity<?> get(@PathVariable final ObjectId questionId,
                                        @PathVariable final ObjectId id) {

        Optional<Answer> answerOptional = answerService.get(id);
        ResponseEntity<?> responseEntity = answerOptional
                .filter(Objects::nonNull)
                .map(answer -> {

                   List<Link> links = Optional.ofNullable(answer.getComments())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(s -> {
                                Link link = linkTo(methodOn(CommentController.class)
                                        .get("question", id, new ObjectId(s)))
                                        .withRel("comments");
                                return link;
                            }).collect(Collectors.toList());

                    Link questionLink = linkTo(methodOn(QuestionController.class).get(questionId)).withRel("question");
                    Link self = linkTo(methodOn(AnswerController.class).get(questionId, id)).withSelfRel();
                    links.add(questionLink);
                    links.add(self);
                    Resource<?> answerResource =
                            new Resource<Answer>(answer, links);
                    return ResponseEntity.ok(answerResource);
                }).orElse(ResponseEntity.notFound().build());

        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.GET, path = "question/{questionId}/answer")
    public ResponseEntity<?> list(@PathVariable final ObjectId questionId,
                                  @PageableDefault(size = 10) Pageable pageable,
                                  PagedResourcesAssembler pagedResourcesAssembler) {

        Optional<Page<Answer>> answerOptional = answerService.list(pageable);
        ResponseEntity<?> responseEntity = answerOptional
                .filter(Objects::nonNull)
                .map(answers -> {
                   PagedResources<Resource<Answer>> resources = pagedResourcesAssembler.toResource(answers);
                    return new ResponseEntity(resources, HttpStatus.OK);
                }).orElse(ResponseEntity.notFound().build());

        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "question/{questionId}/answer/{id}")
    public ResponseEntity<?> delete(@PathVariable final ObjectId questionId,
                                    @PathVariable final ObjectId id) {

        Optional<Answer> answerOptional = answerService.delete(questionId, id);
        ResponseEntity<?> responseEntity = answerOptional
                .filter(Objects::nonNull)
                .filter(Answer::isActive)
                .map(answer -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                .orElse(ResponseEntity.noContent().build());
        return responseEntity;
    }

}
