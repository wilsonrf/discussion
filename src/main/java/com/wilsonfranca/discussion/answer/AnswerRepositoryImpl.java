package com.wilsonfranca.discussion.answer;

import com.wilsonfranca.discussion.comments.Comment;
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
public class AnswerRepositoryImpl implements AnswerRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Answer inactivate(ObjectId questionId, ObjectId answerId) {

        Query query = new Query(Criteria.where("_id").is(answerId).and("question").is(questionId.toHexString()));
        Update update = new Update().set("active", false);
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
        Answer answer = mongoTemplate.findAndModify(query, update, findAndModifyOptions, Answer.class);

        return answer;
    }

    @Override
    public Answer updateComments(final Comment comment, final ObjectId parentId, final ObjectId id, final boolean delete) {

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
        Answer answer = mongoTemplate.findAndModify(query, update, findAndModifyOptions, Answer.class);

        return answer;
    }
}
