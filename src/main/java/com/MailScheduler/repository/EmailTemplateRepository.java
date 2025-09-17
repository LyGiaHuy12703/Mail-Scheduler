package com.MailScheduler.repository;

import com.MailScheduler.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, BigInteger> {
}
