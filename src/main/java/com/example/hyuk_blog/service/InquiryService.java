package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.InquiryDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InquiryService {
    private static final String FILE_PATH = "data/inquiry.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private List<InquiryDto> inquiries;
    private int unreadCount = 0;

    public InquiryService() {
        objectMapper.registerModule(new JavaTimeModule());
        this.inquiries = loadInquiries();
        if (!inquiries.isEmpty()) {
            idGenerator.set(inquiries.get(inquiries.size() - 1).getId() + 1);
        }
    }

    public synchronized void addInquiry(InquiryDto inquiry) {
        inquiry.setId(idGenerator.getAndIncrement());
        inquiry.setCreatedAt(LocalDateTime.now());
        inquiries.add(inquiry);
        unreadCount++;
        saveInquiries();
    }

    public synchronized List<InquiryDto> getAllInquiries() {
        List<InquiryDto> copy = new ArrayList<>(inquiries);
        Collections.reverse(copy);
        return copy;
    }

    public synchronized int getUnreadCount() {
        return unreadCount;
    }

    public synchronized void markAllRead() {
        unreadCount = 0;
    }

    public synchronized List<InquiryDto> getRecentInquiries(int count) {
        int size = inquiries.size();
        List<InquiryDto> result = new ArrayList<>();
        for (int i = size - 1; i >= Math.max(0, size - count); i--) {
            result.add(inquiries.get(i));
        }
        return result;
    }

    private List<InquiryDto> loadInquiries() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try {
            return objectMapper.readValue(file, new TypeReference<List<InquiryDto>>(){});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void saveInquiries() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), inquiries);
        } catch (IOException e) {
            // ignore
        }
    }
} 