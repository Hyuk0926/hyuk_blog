package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/visitor")
public class VisitorController {
    @Autowired
    private VisitorService visitorService;

    @PostMapping("/increase")
    public void increase() {
        visitorService.increaseCount();
    }

    @GetMapping("/stats/daily")
    public Map<String, Integer> getDaily(@RequestParam(defaultValue = "7") int days) {
        return visitorService.getDailyStats(days);
    }

    @GetMapping("/stats/monthly")
    public Map<String, Integer> getMonthly(@RequestParam(defaultValue = "6") int months) {
        return visitorService.getMonthlyStats(months);
    }

    @GetMapping("/today")
    public int today() { return visitorService.getTodayCount(); }

    @GetMapping("/month")
    public int month() { return visitorService.getMonthCount(); }
} 