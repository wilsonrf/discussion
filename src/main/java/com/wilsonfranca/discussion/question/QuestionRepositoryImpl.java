package com.wilsonfranca.discussion.question;

import com.wilsonfranca.discussion.answer.Answer;
import com.wilsonfranca.discussion.comments.Comment;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by wilson.franca on 02/11/17.
 */
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Question updateAnswers(final Answer answer, ObjectId questionId, ObjectId answerId, boolean delete) {
        Query query = new Query(Criteria.where("_id").is(questionId));
        Update update = null;
        if(delete) {
            update = new Update().pull("answers", answerId)
                    .pull("embeddedAnswerses", answer)
                    .inc("totalAnswers", -1);
        } else {
            update = new Update().addToSet("answers", answerId)
                    .addToSet("embeddedAnswerses", answer)
                    .inc("totalAnswers", 1);
        }
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
        Question returned = mongoTemplate.findAndModify(query, update, findAndModifyOptions, Question.class);

        return returned;
    }

    @Override
    public Question inactivate(ObjectId id) {

        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("active", false);
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
        Question returned = mongoTemplate.findAndModify(query, update, findAndModifyOptions, Question.class);

        return returned;
    }

    @Override
    public Question update(ObjectId id, String text) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("text", text);
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
        Question returned = mongoTemplate.findAndModify(query, update, findAndModifyOptions, Question.class);

        return returned;
    }

    @Override
    public Question updateComments(final Comment comment, ObjectId parentId, ObjectId id, boolean delete) {

        Query query = new Query(Criteria.where("_id").is(parentId));
        Update update = null;

        if(delete) {
            update = new Update()
                    .pull("comments", id)
                    .pull("embeddedCommentses", comment)
                    .inc("totalComments", -1);
        } else {
            update = new Update()
                    .addToSet("comments", id)
                    .addToSet("embeddedCommentses", comment)
                    .inc("totalComments", 1);
        }

        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
        Question returned = mongoTemplate.findAndModify(query, update, findAndModifyOptions, Question.class);

        return returned;
    }

    @Override
    public Question updateAnswersComments(final Comment comment, ObjectId answerId, ObjectId questionId, boolean delete) {

        Query query = new Query(Criteria.where("_id")
                .is(questionId)
                .and("embeddedAnswerses._id")
                .is(answerId));

        Update update = null;

        if(delete) {
            update = new Update()
                    .pull("embeddedAnswerses.embeddedCommentses", comment);
        } else {
            update = new Update()
                    .addToSet("embeddedAnswerses.embeddedCommentses", comment);
        }

        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
        Question returned = mongoTemplate.findAndModify(query, update, findAndModifyOptions, Question.class);

        return returned;
    }


}
