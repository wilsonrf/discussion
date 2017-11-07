package com.wilsonfranca.discussion.question;

import com.wilsonfranca.discussion.answer.Answer;
import com.wilsonfranca.discussion.comments.Comment;
import org.bson.types.ObjectId;

/**
 * Created by wilson.franca on 02/11/17.
 */
public interface QuestionRepositoryCustom {

    public Question updateAnswers(final Answer answer, final ObjectId questionId, final ObjectId answerId, final boolean delete);

    public Question inactivate(final ObjectId id);

    public Question update(final ObjectId id, final String text);

    public Question updateComments(final Comment comment, ObjectId parentId, ObjectId id, final boolean delete);

    public Question updateAnswersComments(final Comment comment, ObjectId answerId, final ObjectId questionId, final boolean delete);
}
