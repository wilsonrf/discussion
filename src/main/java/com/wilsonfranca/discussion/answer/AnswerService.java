package com.wilsonfranca.discussion.answer;

import com.wilsonfranca.discussion.question.QuestionRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by wilson.franca on 02/11/17.
 */
@Service
public class AnswerService {

    private static Logger logger = LoggerFactory.getLogger(AnswerService.class);

    private AnswerRepository answerRepository;

    private QuestionRepository questionRepository;

    public AnswerService(@Autowired final AnswerRepository answerRepository,
                         @Autowired final QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public Optional<Answer> create(AnswerRepresentation answerRepresentation, ObjectId questionId) {

        Answer answer = null;

        try {

            answer = new Answer();

            answer.setText(answerRepresentation.getText());
            answer.setQuestion(questionId.toHexString());
            answer.setActive(true);
            answerRepository.save(answer);

            // add answers references
            questionRepository.updateAnswers(answer, questionId, answer.getId(), false);

        } catch (Exception e) {
            logger.error("Error when creating a question", e);
        }

        return Optional.ofNullable(answer);
    }

    public Optional<Answer> get(final ObjectId id) {

        Answer answer = null;

        try {

            answer = answerRepository.findByIdAndActiveIsTrue(id);

        } catch (Exception e) {
            logger.error("Error when getting the question [{}]", id.toHexString());
        }

        return Optional.ofNullable(answer);
    }

    public Optional<Answer> delete(final ObjectId questionId, final ObjectId id) {
        Answer answer = null;

        try {

            answer = answerRepository.inactivate(questionId, id);

            // delete answers references
            questionRepository.updateAnswers(answer, questionId, answer.getId(), true);

        } catch (Exception e) {

        }

        return Optional.ofNullable(answer);
    }

    public Optional<Page<Answer>> list(Pageable pageable) {

        Page<Answer> answers = null;

        try {
            answers = answerRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Error to list all questions", e);
        }

        return Optional.ofNullable(answers);

    }
}
