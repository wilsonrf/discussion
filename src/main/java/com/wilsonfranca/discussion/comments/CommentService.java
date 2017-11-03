package com.wilsonfranca.discussion.comments;

import com.wilsonfranca.discussion.answer.AnswerRepository;
import com.wilsonfranca.discussion.question.QuestionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by wilson on 02/11/17.
 */
@Service
public class CommentService {

    private static final String QUESTION = "question";
    private static final String ANSWER = "answer";

    private CommentRepository commentRepository;
    private AnswerRepository answerRepository;
    private QuestionRepository questionRepository;

    public CommentService(@Autowired final CommentRepository commentRepository,
                          @Autowired AnswerRepository answerRepository,
                          @Autowired QuestionRepository questionRepository) {
        this.commentRepository = commentRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public Optional<Comment> create(String parent, ObjectId parentId, CommentRepresentation commentRepresentation) {

        Comment comment = null;

        comment = new Comment();
        comment.setText(commentRepresentation.getText());
        comment.setActive(true);

        commentRepository.save(comment);

        if(QUESTION.equalsIgnoreCase(parent)) {
            comment.setQuestion(parentId.toHexString());
            questionRepository.updateComments(comment.getText(), comment.getAuthor(), parentId, comment.getId(), false);
        } else if(ANSWER.equalsIgnoreCase(parent)) {
            comment.setAnswer(parentId.toHexString());
        }


        return Optional.ofNullable(comment);
    }

    public Optional<Comment> get(String parent, ObjectId parentId, ObjectId id) {

        Comment comment = null;

        if(QUESTION.equalsIgnoreCase(parent)) {
            comment = commentRepository.findByIdAndQuestionAndActiveIsTrue(parentId.toHexString(), id);
        } else if(ANSWER.equalsIgnoreCase(parent)) {
            comment = commentRepository.findByIdAndAnswerAndActiveIsTrue(parentId.toHexString(), id);
        }

        return Optional.ofNullable(comment);
    }

    public Optional<Comment> inactivate(String parent, ObjectId parentId, ObjectId id) {
        Comment comment = null;
        if(QUESTION.equalsIgnoreCase(parent)) {
            comment = commentRepository.findByIdAndQuestionAndActiveIsTrue(parentId.toHexString(), id);
            questionRepository.updateComments(comment.getText(), comment.getAuthor(), parentId, id, true);
        } else if(ANSWER.equalsIgnoreCase(parent)) {
            comment = commentRepository.findByIdAndAnswerAndActiveIsTrue(parentId.toHexString(), id);
            answerRepository.updateComments(parentId, id, true);
        }

        return Optional.ofNullable(comment);
    }
}
