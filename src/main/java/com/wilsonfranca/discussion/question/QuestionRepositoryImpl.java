package com.wilsonfranca.discussion.question;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Question updateAnswers(final String question, final String answerId) {

        Query query = new Query(Criteria.where("_id").is(question));
        Update update = new Update().addToSet("answers", answerId).inc("totalAnswers", 1);
        Question returned = mongoTemplate.findAndModify(query, update, Question.class);

        return returned;
    }
}
