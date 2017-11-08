package com.wilsonfranca.discussion.question;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * Created by wilson on 05/11/17.
 */
public class QuestionResourceAssembler extends ResourceAssemblerSupport<Question, QuestionResource> {

    public QuestionResourceAssembler() {
        super(QuestionController.class, QuestionResource.class);
    }

    @Override
    public QuestionResource toResource(Question entity) {
        QuestionResource questionResource = createResourceWithId(entity.getId(), entity);
        return questionResource;
    }
}
