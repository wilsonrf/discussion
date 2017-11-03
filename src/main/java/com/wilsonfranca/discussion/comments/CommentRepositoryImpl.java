package com.wilsonfranca.discussion.comments;

import com.wilsonfranca.discussion.answer.Answer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by wilson on 02/11/17.
 */
public class CommentRepositoryImpl implements CommentReposioryCustom {

    private static final String QUESTION = "question";
    private static final String ANSWER = "answer";

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Comment inactivate(String parent, ObjectId parentId, ObjectId id) {


        Query query = null;

        if(QUESTION.equalsIgnoreCase(parent)) {
            query = new Query(Criteria.where("_id").is(id).and("question").is(parentId));
        } else if(ANSWER.equalsIgnoreCase(parent)) {
            query = new Query(Criteria.where("_id").is(id).and("answer").is(parentId));
        }

        Update update = new Update().set("active", false);
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
        Comment comment = mongoTemplate.findAndModify(query, update, findAndModifyOptions, Comment.class);

        return comment;
    }
}
