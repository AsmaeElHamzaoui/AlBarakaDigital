package com.AlBarakaDigital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard/client")
    public String clientDashboard() {
        return "dashboard-client";
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard() {
        return "dashboard-admin";
    }

    @GetMapping("/dashboard/agent")
    public String agentDashboard() {
        return "dashboard-agent";
    }

}