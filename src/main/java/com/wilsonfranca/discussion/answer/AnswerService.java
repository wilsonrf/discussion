package com.wilsonfranca.discussion.answer;

import com.wilsonfranca.discussion.question.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<Answer> create(AnswerRepresentation answerRepresentation, String questionId) {

        Answer answer = null;

        try {

            answer = new Answer();

            answer.setText(answerRepresentation.getText());
            answer.setQuestion(questionId);

            answerRepository.save(answer);

            questionRepository.updateAnswers(questionId, answer.getId().toHexString());

        } catch (Exception e) {
            logger.error("Error when creating a question", e);
        }

        return Optional.ofNullable(answer);
    }
}
