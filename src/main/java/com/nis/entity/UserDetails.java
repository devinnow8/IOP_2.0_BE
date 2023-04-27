package com.nis.entity;

import com.nis.model.Role;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniqueUserEmail", columnNames = {"email"}),
        @UniqueConstraint(name = "UniqueUserPhone", columnNames = {"phone_number"})
})
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userID;
    private String firstname;
    private String lastname;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
    private String password;
    @Column(name = "is_locked")
    private boolean isLocked=false;
    @Column(name = "failed_attempt")
    private int failedAttempt;
    private LocalDateTime lockTime;
    private LocalDateTime lastLogin;
    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
