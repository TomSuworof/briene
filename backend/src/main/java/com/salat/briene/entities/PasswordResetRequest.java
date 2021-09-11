package com.salat.briene.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Entity
@Getter
@Table(name = "t_password_reset_requests")
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    @Id
    @NotNull
    private String id;

    @NotEmpty(message = "Request cannot correspond to empty user")
    private String username;

    @PastOrPresent(message = "Request cannot be created in future")
    private Date created;
}