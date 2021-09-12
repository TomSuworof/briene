package com.salat.briene.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Table(name = "t_password_reset_requests")
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    @Id
    @NotNull
    @GeneratedValue
    private UUID id;

    @NotEmpty(message = ConstraintViolationMessage.PASSWORD_RESET_REQUEST_USERNAME_EMPTY)
    private String username;

    @PastOrPresent(message = ConstraintViolationMessage.PASSWORD_RESET_REQUEST_CREATED_INVALID)
    private Date created;

    public PasswordResetRequest(String username, Date created) {
        this.username = username;
        this.created = created;
    }
}