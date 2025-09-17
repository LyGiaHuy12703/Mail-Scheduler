package com.MailScheduler.service;

import com.MailScheduler.entity.Schedules;
import com.MailScheduler.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ScheduleServices {
    @Autowired
    private ScheduleRepository scheduleRepository;

    public Page<Schedules> getAllSchedules(int page, int size) {
        return scheduleRepository.findAll(PageRequest.of(page, size));
    }

    public Schedules findById(BigInteger id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public void updateSchedule(BigInteger id, Schedules s) {
        Schedules schedules = scheduleRepository.findById(id).orElse(null);
        if (schedules != null) {
            schedules.setName(s.getName());
            schedules.setCronExpression(String.valueOf(s.getCronExpression()));
            schedules.setType(s.getType());
            schedules.setReceiverEmail(s.getReceiverEmail());
            schedules.setTemplate(s.getTemplate());
            scheduleRepository.save(schedules);
        }
    }

    public void deleteSchedule(BigInteger id) {
        Schedules schedules = scheduleRepository.findById(id).orElse(null);
        if (schedules != null) {
            scheduleRepository.delete(schedules);
        }
    }
}
