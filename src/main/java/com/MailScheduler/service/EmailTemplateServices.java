package com.MailScheduler.service;

import com.MailScheduler.entity.EmailTemplate;
import com.MailScheduler.repository.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class EmailTemplateServices {
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    public void save(EmailTemplate template) {
        emailTemplateRepository.save(template);
    }

    public Page<EmailTemplate> findWithPagination(int page, int size) {
        return emailTemplateRepository.findAll(PageRequest.of(page, size));
    }
    public EmailTemplate findById(BigInteger id) {
        return emailTemplateRepository.findById(id).orElse(null);
    }

    public void update(BigInteger id, EmailTemplate template) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(id).orElse(null);
        if (emailTemplate != null) {
            emailTemplate.setName(template.getName());
            emailTemplate.setSubject(template.getSubject());
            emailTemplate.setBody(template.getBody());
            emailTemplateRepository.save(emailTemplate);
        }
    }

    public void deleteTemplate(BigInteger id) {
        EmailTemplate template = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (template.getSchedules() != null && !template.getSchedules().isEmpty()) {
            throw new IllegalStateException("Không thể xóa template vì đang được sử dụng trong schedules");
        }

        emailTemplateRepository.delete(template);
    }
}
