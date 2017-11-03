package com.wilsonfranca.discussion.comments;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wilson on 02/11/17.
 */
@Repository
public interface CommentRepository extends MongoRepository<Comment, ObjectId>, CommentReposioryCustom {

    public Comment findByIdAndQuestionAndActiveIsTrue(String parent, ObjectId id);

    public Comment findByIdAndAnswerAndActiveIsTrue(String parent, ObjectId id);
}
