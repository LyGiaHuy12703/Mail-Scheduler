package com.MailScheduler.controller;

import com.MailScheduler.entity.Schedules;
import com.MailScheduler.repository.EmailTemplateRepository;
import com.MailScheduler.repository.ScheduleRepository;
import com.MailScheduler.service.CronGenerateService;
import com.MailScheduler.service.ScheduleServices;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigInteger;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleServices scheduleServices;
    private final EmailTemplateRepository emailTemplateRepository;
    private final CronGenerateService cronGenerateService;
    private final ScheduleRepository scheduleRepository;

    public ScheduleController(ScheduleServices scheduleServices, EmailTemplateRepository emailTemplateRepository, CronGenerateService cronGenerateService, ScheduleRepository scheduleRepository) {
        this.scheduleServices = scheduleServices;
        this.emailTemplateRepository = emailTemplateRepository;
        this.cronGenerateService = cronGenerateService;
        this.scheduleRepository = scheduleRepository;
    }
    //Hiển thị danh sách phân trang
    @GetMapping("/list")
    public String getSchedules(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        Page<Schedules> schedules = scheduleServices.getAllSchedules(page, size);
        model.addAttribute("schedules", schedules);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", schedules.getTotalPages());
        return "schedules/list";
    }
    //Hiển thị form tạo mới
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("schedule", new Schedules());
        model.addAttribute("emailTemplates", emailTemplateRepository.findAll());
        model.addAttribute("isNew", true);
        return "schedules/form";
    }
    //Lưu dữ liệu mới
    @PostMapping("/create")
    public String saveSchedule(@ModelAttribute Schedules schedule) {
        if(!"cron".equalsIgnoreCase(schedule.getType().toString())){
            String cron = cronGenerateService.generateCronSchedule(schedule.getType().toString());
            schedule.setCronExpression(cron);
        }
        scheduleRepository.save(schedule);
        return "redirect:/schedules/list?page=0&size=5";
    }
    //Hiển thị form update
    @GetMapping("/update/{id}")
    public String update(@PathVariable BigInteger id, Model model) {
        Schedules schedule = scheduleServices.findById(id);
        if(schedule == null){
            return "redirect:/schedules/list?page=0&size=5";
        }
        model.addAttribute("schedule", schedule);
        model.addAttribute("emailTemplates", emailTemplateRepository.findAll());
        model.addAttribute("isNew", false);
        return "schedules/form";
    }
    //Lưu update
    @PostMapping("/update/{id}")
    public String updateSchedule(@PathVariable BigInteger id, @ModelAttribute Schedules schedule) {
        scheduleServices.updateSchedule(id, schedule);
        return "redirect:/schedules/list?page=0&size=5";
    }
    //Xóa schedule
    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable BigInteger id, RedirectAttributes redirectAttributes) {
        try {
            scheduleServices.deleteSchedule(id);
            redirectAttributes.addFlashAttribute("message", "Schedule đã được xóa thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Schedule đang được sử dụng!");
        }
        return "redirect:/schedules/list?page=0&size=5";
    }

}
