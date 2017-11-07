package com.wilsonfranca.discussion.answer;

import com.wilsonfranca.discussion.comments.Comment;
import org.bson.types.ObjectId;

/**
 * Created by wilson on 02/11/17.
 */
public interface AnswerRepositoryCustom {

    public Answer inactivate(final ObjectId questionId, final ObjectId answerId);

    public Answer updateComments(final Comment comment, ObjectId parentId, ObjectId id, boolean b);
}
