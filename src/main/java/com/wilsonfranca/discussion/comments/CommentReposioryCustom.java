package com.wilsonfranca.discussion.comments;

import org.bson.types.ObjectId;

/**
 * Created by wilson on 02/11/17.
 */
public interface CommentReposioryCustom {

    public Comment inactivate(String parent, ObjectId parentId, ObjectId id);
}
