package com.wilsonfranca.discussion.question;

import org.bson.types.ObjectId;

/**
 * Created by wilson.franca on 02/11/17.
 */
public interface QuestionRepositoryCustom {

    public Question updateAnswers(final String text, final String author, final ObjectId questionId, final ObjectId answerId, final boolean delete);

    public Question inactivate(final ObjectId id);

    public Question update(final ObjectId id, final String text);

    public Question updateComments(final String text, final String author, ObjectId parentId, ObjectId id, final boolean delete);
}
