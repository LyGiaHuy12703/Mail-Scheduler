package com.MailScheduler.entity;

import com.MailScheduler.enums.ScheduleTypeEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedules")
public class Schedules {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    BigInteger id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    ScheduleTypeEnum type;

    @Column(length = 100, name = "cron_expression")
    String cronExpression;

    @Column(nullable = false, length = 150, name = "receiver_email")
    String receiverEmail;

    LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    @JsonBackReference
    EmailTemplate template;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
