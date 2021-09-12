package com.salat.briene.entities;

import com.salat.briene.exceptions.IllegalArticleStateException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleState {
    PUBLISHED("published", "publish"),
    IN_EDITING("drafts", "save"),
    ALL("all", null);

    private String description;

    private String actionForThisState;

    public static ArticleState getFromDescription(String description) throws IllegalArticleStateException {
        return switch (description.toLowerCase()) {
            case "published" -> PUBLISHED;
            case "drafts" -> IN_EDITING;
            case "all" -> ALL;
            default -> throw new IllegalArticleStateException();
        };
    }

    public static ArticleState getFromAction(String action) throws IllegalArticleStateException {
        return switch (action.toLowerCase()) {
            case "publish" -> PUBLISHED;
            case "save" -> IN_EDITING;
            default -> throw new IllegalArticleStateException();
        };
    }
}
