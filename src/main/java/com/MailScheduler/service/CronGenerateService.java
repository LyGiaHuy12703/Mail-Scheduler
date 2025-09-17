package com.MailScheduler.service;


import org.springframework.stereotype.Service;

@Service
public class CronGenerateService {
    public String generateCronSchedule(String type) {
        switch (type) {
            case "DAILY":
                return String.format("0 0 8 * * ?");
            case "WEEKLY":
                //Mặc định sáng thứ 2 run (MON)
                return String.format("0 0 9 ? * MON");
            case "MONTHLY":
                // mặc định chạy ngày 1 hàng tháng
                return String.format("0 1 0 1 * ?");
            case "CRON":
                //User tự nhập không cần format
                return null;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}
