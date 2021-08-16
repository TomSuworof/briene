package com.salat.briene.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "t_password_reset_requests")
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    @Id
    @Column
    private String id;

    @Column
    private String username;

    @Column
    private Date created;
}