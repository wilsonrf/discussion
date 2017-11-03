package com.wilsonfranca.discussion.question;

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
    public Question updateAnswers(final String text, final String author, ObjectId questionId, ObjectId answerId, boolean delete) {
        Query query = new Query(Criteria.where("_id").is(questionId));
        Update update = null;
        if(delete) {
            update = new Update().pull("answers", answerId)
                    .pull("embeddedAnswerses", new Question.EmbeddedAnswers(text, author))
                    .inc("totalAnswers", -1);
        } else {
            update = new Update().addToSet("answers", answerId)
                    .addToSet("embeddedAnswerses", new Question.EmbeddedAnswers(text, author))
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
    public Question updateComments(final String text, final String author, ObjectId parentId, ObjectId id, boolean delete) {

        Query query = new Query(Criteria.where("_id").is(parentId));
        Update update = null;

        if(delete) {
            update = new Update()
                    .pull("comments", id)
                    .pull("embeddedCommentses", new Question.EmbeddedComments(text, author))
                    .inc("totalComments", -1);
        } else {
            update = new Update()
                    .addToSet("comments", id)
                    .addToSet("embeddedCommentses", new Question.EmbeddedComments(text, author))
                    .inc("totalComments", 1);
        }

        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
        Question returned = mongoTemplate.findAndModify(query, update, findAndModifyOptions, Question.class);

        return returned;
    }


}
