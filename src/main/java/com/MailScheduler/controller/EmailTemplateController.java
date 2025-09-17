package com.MailScheduler.controller;

import com.MailScheduler.entity.EmailTemplate;
import com.MailScheduler.service.EmailTemplateServices;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigInteger;

@Controller
public class EmailTemplateController {
    private final EmailTemplateServices emailTemplateServices;

    public EmailTemplateController(EmailTemplateServices emailTemplateServices) {
        this.emailTemplateServices = emailTemplateServices;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
    //Danh sách phân trang
    @GetMapping("/templates/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size) {
        Page<EmailTemplate> templatePage  = emailTemplateServices.findWithPagination(page, size);
        model.addAttribute("templatePage", templatePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", templatePage.getTotalPages());
        return "templates/list";
    }
    //Hiển thị form tạo mới
    @GetMapping("/templates/create")
    public String createForm(Model model) {
        model.addAttribute("template", new EmailTemplate());
        model.addAttribute("isNew", true);
        return "templates/form";
    }
    //Hiển thị form update
    @GetMapping("/templates/update/{id}")
    public String updateForm(Model model, @PathVariable("id") BigInteger id) {
        EmailTemplate template = emailTemplateServices.findById(id);
        if (template == null) {
            return "redirect:/templates/list?page=0&size=5";
        }
        model.addAttribute("template", template);
        model.addAttribute("isNew", false);
        return "templates/form";
    }
    //Lưu tạo mới
    @PostMapping("/templates/create")
    public String save(@ModelAttribute EmailTemplate template) {
        emailTemplateServices.save(template);
        return "redirect:/templates/list?page=0&size=5";
    }
    //Lưu update
    @PostMapping("/templates/update/{id}")
    public String update(@ModelAttribute EmailTemplate template, @PathVariable("id") BigInteger id) {
        emailTemplateServices.update(id, template);
        return "redirect:/templates/list?page=0&size=5";
    }
    //Xóa template
    @GetMapping("/templates/delete/{id}")
    public String deleteTemplate(@PathVariable("id") BigInteger id, RedirectAttributes redirectAttributes) {
        try {
            emailTemplateServices.deleteTemplate(id);
            redirectAttributes.addFlashAttribute("message", "Template đã được xóa thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Template đang được sử dụng!");
        }
        return "redirect:/templates/list";
    }
}
