package com.wilsonfranca.discussion.question;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wilson.franca on 02/11/17.
 */
@Repository
public interface QuestionRepository  extends MongoRepository<Question, ObjectId>, QuestionRepositoryCustom {

    public Question findByIdAndActiveIsTrue(final ObjectId id);
}
