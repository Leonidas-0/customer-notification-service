package com.levanz.customer.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class WebController {

    @GetMapping({"/", "/dashboard"})
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/customers/{id}")
    public String customerDetail(@PathVariable Long id, Model model) {
        model.addAttribute("customerId", id);
        return "customer-detail";
    }
    
    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }
    
    @GetMapping("/admins")
    public String admins() {
        return "admins";
    }
}