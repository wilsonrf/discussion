package com.wilsonfranca.discussion.answer;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wilson.franca on 02/11/17.
 */
@Repository
public interface AnswerRepository extends MongoRepository<Answer, ObjectId>, AnswerRepositoryCustom {

    public Answer findByIdAndActiveIsTrue(final ObjectId id);
}
