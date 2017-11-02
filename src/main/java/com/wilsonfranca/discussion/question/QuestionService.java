package com.wilsonfranca.discussion.question;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by wilson.franca on 02/11/17.
 */
@Service
public class QuestionService {

    private static Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private QuestionRepository questionRepository;

    public QuestionService(@Autowired final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Creates and return a question
     *
     * @param questionRepresentation
     * @return
     */
    public Optional<Question> create(QuestionRepresentation questionRepresentation) {

        Question question = null;

        try {

            question = new Question();

            question.setText(questionRepresentation.getText());

            questionRepository.save(question);

        } catch (Exception e) {
            logger.error("Error when creating a question", e);
        }

        return Optional.ofNullable(question);
    }

    public Optional<Question> get(final ObjectId id) {

        Question question = null;

        try {

            question = questionRepository.findOne(id);

        } catch (Exception e) {
            logger.error("Error when getting the question [{}]", id.toHexString());
        }

        return Optional.ofNullable(question);
    }
}
