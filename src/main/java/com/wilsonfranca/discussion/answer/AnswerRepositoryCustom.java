package com.wilsonfranca.discussion.answer;

import org.bson.types.ObjectId;

/**
 * Created by wilson on 02/11/17.
 */
public interface AnswerRepositoryCustom {

    public Answer inactivate(final ObjectId questionId, final ObjectId answerId);

    public Answer updateComments(ObjectId parentId, ObjectId id, boolean b);
}
