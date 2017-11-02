package com.wilsonfranca.discussion.common;

import javax.validation.constraints.NotNull;

/**
 * Created by wilson.franca on 02/11/17.
 */
public abstract class TextRepresentation {

    @NotNull
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
