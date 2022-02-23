package com.salat.briene.entities;

public interface ConstraintViolationMessage {
    String ARTICLE_TITLE_EMPTY =  "Title cannot be empty: it is shown in HTML title";
    String ARTICLE_CONTENT_EMPTY = "Content cannot be empty";
    String ARTICLE_PUBLICATION_DATE_INVALID = "Article cannot be published in future";

    String PASSWORD_RESET_REQUEST_USERNAME_EMPTY = "Request cannot correspond to empty user";
    String PASSWORD_RESET_REQUEST_CREATED_INVALID = "Request cannot be created in future";

    String ROLE_NAME_EMPTY = "Role cannot be called by empty string";

    String TAG_NAME_EMPTY = "Tag cannot be called by empty string";

    String USER_USERNAME_EMPTY = "Username cannot be empty";
    String USER_EMAIL_EMPTY = "Email cannot be empty";
    String USER_EMAIL_INVALID = "Invalid email";
    String USER_BIO_LIMIT = "Bio should be less than 255 characters";
    String USER_PASSWORD_EMPTY = "Password cannot be empty";
}
