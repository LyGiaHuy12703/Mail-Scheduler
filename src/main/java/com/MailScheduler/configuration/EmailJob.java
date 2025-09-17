package com.MailScheduler.configuration;

import com.MailScheduler.entity.Schedules;
import com.MailScheduler.repository.ScheduleRepository;
import com.MailScheduler.service.MailServices;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class EmailJob {

    private final ScheduleRepository scheduleRepository;
    private final MailServices mailServices;

    public EmailJob(ScheduleRepository scheduleRepository, MailServices mailServices) {
        this.scheduleRepository = scheduleRepository;
        this.mailServices = mailServices;
    }

    @Transactional
    @Scheduled(cron = "0 * * * * ?")
    public void runEmailJob() throws MessagingException {
        List<Schedules> schedules = scheduleRepository.findAll();
        for(Schedules schedule : schedules){
            if(shouldSend(schedule)){
                mailServices.sendMail(
                        schedule.getReceiverEmail(),
                        schedule.getTemplate().getSubject(),
                        schedule.getTemplate().getBody()
                );
            }
        }
    }

    private boolean shouldSend(Schedules schedule) {
        String cronExpr = schedule.getCronExpression();
        if(cronExpr == null || cronExpr.isEmpty()){
            return false;
        }
        CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
        Cron cron = parser.parse(cronExpr);

        ExecutionTime executionTime = ExecutionTime.forCron(cron);

        ZonedDateTime now = ZonedDateTime.now();

        return executionTime.isMatch(now);
    }
}
