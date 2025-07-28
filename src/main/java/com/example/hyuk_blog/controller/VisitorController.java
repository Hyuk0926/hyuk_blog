package com.example.hyuk_blog.controller;

import com.example.hyuk_blog.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/visitor")
public class VisitorController {
    @Autowired
    private VisitorService visitorService;

    @PostMapping("/increase")
    public void increase(HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        visitorService.increaseCount(ipAddress, userAgent);
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

    /**
     * 클라이언트의 실제 IP 주소를 가져오는 메서드
     * 프록시나 로드밸런서를 고려하여 X-Forwarded-For 헤더도 확인
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
} 