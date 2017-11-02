package com.wilsonfranca.discussion.question;

import com.wilsonfranca.discussion.answer.AnswerController;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by wilson.franca on 02/11/17.
 */
@RestController
@RequestMapping("question")
public class QuestionController {

    private static Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private QuestionService questionService;


    public QuestionController(@Autowired final QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@Valid @RequestBody final QuestionRepresentation questionRepresentation,
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

            Optional<Question> optional = questionService.create(questionRepresentation);

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

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> get(@PathVariable final ObjectId id) {
        Optional<Question> questionOptional = questionService.get(id);
        ResponseEntity<?> responseEntity = questionOptional
                .filter(Objects::nonNull)
                .map(question -> {
                    List<Link> links = new ArrayList<Link>();
                    Optional.ofNullable(question.getAnswers())
                            .orElse(Collections.emptyList())
                            .stream()
                            .forEach(s -> {
                                Link link =
                                        linkTo(methodOn(AnswerController.class).get(new ObjectId(s)))
                                        .withRel("answer");
                                links.add(link);
                            });
                    Link self = linkTo(methodOn(QuestionController.class).get(id)).withSelfRel();
                    links.add(self);
                    Resource<Question> questionResource =
                            new Resource<Question>(question, links);
                    return ResponseEntity.ok(questionResource);
                }).orElse(ResponseEntity.notFound().build());

        return responseEntity;
    }

}
