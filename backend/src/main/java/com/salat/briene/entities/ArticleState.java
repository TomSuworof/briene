package com.salat.briene.entities;

import com.salat.briene.exceptions.IllegalArticleStateException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleState {
    PUBLISHED("publish", "published"),
    IN_EDITING("save", "drafts"),
    ALL(null, "all");

    private final String actionForThisState;
    private final String description;

    public static ArticleState getFromAction(String action) throws IllegalArticleStateException {
        try {
            for (ArticleState state : values()) {
                if (state.getActionForThisState().equalsIgnoreCase(action)) {
                    return state;
                }
            }
        } catch (NullPointerException ignored) {
        }
        throw new IllegalArticleStateException();
    }

    public static ArticleState getFromDescription(String description) throws IllegalArticleStateException {
        for (ArticleState state : values()) {
            if (state.getDescription().equalsIgnoreCase(description)) {
                return state;
            }
        }
        throw new IllegalArticleStateException();
    }
}
