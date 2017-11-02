package com.wilsonfranca.discussion.answer;

import com.wilsonfranca.discussion.question.Question;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by wilson.franca on 02/11/17.
 */
@RestController
@RequestMapping("question/{questionId}/answer")
public class AnswerController {

    private static Logger logger = LoggerFactory.getLogger(AnswerController.class);

    private AnswerService answerService;


    public AnswerController(@Autowired final AnswerService answerService) {
        this.answerService = answerService;
    }

    @RequestMapping(method = RequestMethod.POST)
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

            Optional<Answer> optional = answerService.create(answerRepresentation, questionId.toHexString());

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
    public ResponseEntity<Question> get(@PathVariable final ObjectId id) {
        return null;
    }
}
