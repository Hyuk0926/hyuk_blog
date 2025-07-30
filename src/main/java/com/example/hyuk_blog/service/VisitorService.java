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
    private Map<String, Set<String>> dailyVisitors; // yyyy-MM-dd -> Set<IP>

    public VisitorService() {
        objectMapper.registerModule(new JavaTimeModule());
        loadCounts();
    }

    public synchronized boolean increaseCount(String ipAddress) {
        return increaseCount(ipAddress, null);
    }

    public synchronized boolean increaseCount(String ipAddress, String userAgent) {
        String today = LocalDate.now().toString();
        String month = YearMonth.now().toString();
        
        // IP와 User-Agent를 조합하여 더 정확한 방문자 식별
        String visitorKey = ipAddress;
        if (userAgent != null && !userAgent.isEmpty()) {
            // User-Agent의 주요 부분만 사용 (브라우저 정보)
            String browserInfo = extractBrowserInfo(userAgent);
            if (browserInfo != null) {
                visitorKey = ipAddress + "|" + browserInfo;
            }
        }
        
        // 오늘 방문한 방문자 목록 가져오기
        Set<String> todayVisitors = dailyVisitors.getOrDefault(today, new HashSet<>());
        
        // 이미 오늘 방문한 방문자인지 확인
        if (todayVisitors.contains(visitorKey)) {
            return false; // 이미 방문한 방문자
        }
        
        // 새로운 방문자로 추가
        todayVisitors.add(visitorKey);
        dailyVisitors.put(today, todayVisitors);
        
        // 카운트 증가
        dailyCounts.put(today, dailyCounts.getOrDefault(today, 0) + 1);
        monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0) + 1);
        
        saveCounts();
        return true; // 새로운 방문자
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
            dailyVisitors = new HashMap<>();
            return;
        }
        try {
            Map<String, Object> map = objectMapper.readValue(file, new TypeReference<Map<String, Object>>(){});
            dailyCounts = (Map<String, Integer>) map.getOrDefault("daily", new HashMap<>());
            monthlyCounts = (Map<String, Integer>) map.getOrDefault("monthly", new HashMap<>());
            
            // IP 방문자 데이터 로드 (기존 데이터와의 호환성을 위해)
            if (map.containsKey("dailyVisitors")) {
                Object dailyVisitorsObj = map.get("dailyVisitors");
                if (dailyVisitorsObj instanceof Map) {
                    Map<String, Object> visitorsMap = (Map<String, Object>) dailyVisitorsObj;
                    dailyVisitors = new HashMap<>();
                    
                    // 각 날짜별 방문자 목록을 Set으로 변환
                    for (Map.Entry<String, Object> entry : visitorsMap.entrySet()) {
                        String date = entry.getKey();
                        Object visitorsObj = entry.getValue();
                        
                        if (visitorsObj instanceof List) {
                            // ArrayList를 HashSet으로 변환
                            List<String> visitorsList = (List<String>) visitorsObj;
                            Set<String> visitorsSet = new HashSet<>(visitorsList);
                            dailyVisitors.put(date, visitorsSet);
                        } else if (visitorsObj instanceof Set) {
                            // 이미 Set인 경우 그대로 사용
                            dailyVisitors.put(date, (Set<String>) visitorsObj);
                        } else {
                            // 예상치 못한 타입인 경우 빈 Set으로 초기화
                            dailyVisitors.put(date, new HashSet<>());
                        }
                    }
                } else {
                    dailyVisitors = new HashMap<>();
                }
            } else {
                dailyVisitors = new HashMap<>();
            }
        } catch (IOException e) {
            dailyCounts = new HashMap<>();
            monthlyCounts = new HashMap<>();
            dailyVisitors = new HashMap<>();
        }
    }

    private void saveCounts() {
        Map<String, Object> map = new HashMap<>();
        map.put("daily", dailyCounts);
        map.put("monthly", monthlyCounts);
        map.put("dailyVisitors", dailyVisitors);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), map);
        } catch (IOException e) {
            // ignore
        }
    }

    /**
     * User-Agent에서 브라우저 정보를 추출
     */
    private String extractBrowserInfo(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        
        // 주요 브라우저 정보만 추출
        String[] browsers = {"Chrome", "Firefox", "Safari", "Edge", "Opera"};
        for (String browser : browsers) {
            if (userAgent.contains(browser)) {
                return browser;
            }
        }
        
        // 모바일 브라우저 확인
        if (userAgent.contains("Mobile")) {
            return "Mobile";
        }
        
        return "Other";
    }
} 