package com.wilsonfranca.discussion.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wilsonfranca.discussion.answer.Answer;
import com.wilsonfranca.discussion.comments.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by wilson.franca on 02/11/17.
 */
@Document
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Question {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private String text;

    //TODO use here @CreatedBy
    private String author;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private Instant dateCreated;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private Instant lastUpdated;

    // reference to all answers of this question
    @JsonIgnore
    private List<String> answers;

    // reference to all comments of this question
    @JsonIgnore
    private List<String> comments;

    @JsonIgnore
    private List<Answer> embeddedAnswerses;

    @JsonIgnore
    private List<Comment> embeddedCommentses;

    @JsonIgnore
    private int totalAnswers;

    @JsonIgnore
    private int totalComments;

    @JsonIgnore
    private boolean active;

    @JsonIgnore
    @Transient
    protected static final int MAX_ANSWERS = 10;

    @JsonIgnore
    @Transient
    protected static final int MAX_COMMENTS = 5;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Answer> getEmbeddedAnswerses() {

        return Optional.ofNullable(this.embeddedAnswerses).orElse(new ArrayList<>());
    }

    public void setEmbeddedAnswerses(List<Answer> embeddedAnswerses) {
        this.embeddedAnswerses = embeddedAnswerses;
    }

    public List<Comment> getEmbeddedCommentses() {
        return Optional.ofNullable(this.embeddedCommentses).orElse(new ArrayList<>());
    }

    public void setEmbeddedCommentses(List<Comment> embeddedCommentses) {
        this.embeddedCommentses = embeddedCommentses;
    }

}
