package com.MailScheduler.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_template")
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    BigInteger id;

    @Column(nullable = false, length = 100)
    String name;

    @Column(nullable = false, length = 200)
    String subject;

    @Lob
    @Column(columnDefinition = "TEXT")
    String body;

    LocalDateTime createdAt;

    @OneToMany(mappedBy = "template")
    @JsonManagedReference
    List<Schedules> schedules;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
