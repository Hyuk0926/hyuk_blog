package com.example.hyuk_blog.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class VisitorService {
    private static final String FILE_PATH = "data/visitor.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Integer> dailyCounts; // yyyy-MM-dd -> count
    private Map<String, Integer> monthlyCounts; // yyyy-MM -> count

    public VisitorService() {
        objectMapper.registerModule(new JavaTimeModule());
        loadCounts();
    }

    public synchronized void increaseCount() {
        String today = LocalDate.now().toString();
        String month = YearMonth.now().toString();
        dailyCounts.put(today, dailyCounts.getOrDefault(today, 0) + 1);
        monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0) + 1);
        saveCounts();
    }

    public synchronized int getTodayCount() {
        String today = LocalDate.now().toString();
        return dailyCounts.getOrDefault(today, 0);
    }

    public synchronized int getMonthCount() {
        String month = YearMonth.now().toString();
        return monthlyCounts.getOrDefault(month, 0);
    }

    public synchronized Map<String, Integer> getDailyStats(int days) {
        // 최근 days일만 반환 (yyyy-MM-dd -> count)
        LocalDate now = LocalDate.now();
        Map<String, Integer> result = new LinkedHashMap<>();
        for (int i = days - 1; i >= 0; i--) {
            String date = now.minusDays(i).toString();
            result.put(date, dailyCounts.getOrDefault(date, 0));
        }
        return result;
    }

    public synchronized Map<String, Integer> getMonthlyStats(int months) {
        // 최근 months개월만 반환 (yyyy-MM -> count)
        YearMonth now = YearMonth.now();
        Map<String, Integer> result = new LinkedHashMap<>();
        for (int i = months - 1; i >= 0; i--) {
            String ym = now.minusMonths(i).toString();
            result.put(ym, monthlyCounts.getOrDefault(ym, 0));
        }
        return result;
    }

    private void loadCounts() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            dailyCounts = new HashMap<>();
            monthlyCounts = new HashMap<>();
            return;
        }
        try {
            Map<String, Object> map = objectMapper.readValue(file, new TypeReference<Map<String, Object>>(){});
            dailyCounts = (Map<String, Integer>) map.getOrDefault("daily", new HashMap<>());
            monthlyCounts = (Map<String, Integer>) map.getOrDefault("monthly", new HashMap<>());
        } catch (IOException e) {
            dailyCounts = new HashMap<>();
            monthlyCounts = new HashMap<>();
        }
    }

    private void saveCounts() {
        Map<String, Object> map = new HashMap<>();
        map.put("daily", dailyCounts);
        map.put("monthly", monthlyCounts);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), map);
        } catch (IOException e) {
            // ignore
        }
    }
} 