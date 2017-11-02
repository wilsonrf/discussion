package com.wilsonfranca.discussion.question;

/**
 * Created by wilson.franca on 02/11/17.
 */
public interface QuestionRepositoryCustom {

    public Question updateAnswers(final String questionId, final String answerId);

}
