package com.MailScheduler.repository;

import com.MailScheduler.entity.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedules, BigInteger> {
}
