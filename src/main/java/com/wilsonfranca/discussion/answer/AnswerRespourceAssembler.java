package com.wilsonfranca.discussion.answer;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * Created by wilson on 05/11/17.
 */
public class AnswerRespourceAssembler extends ResourceAssemblerSupport<Answer, AnswerResource> {

    public AnswerRespourceAssembler() {
        super(AnswerController.class, AnswerResource.class);
    }

    @Override
    public AnswerResource toResource(Answer entity) {

        AnswerResource answerResource =
                new AnswerResource(entity);

        return answerResource;
    }

}
