package com.example.hyuk_blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("statusCode", 404);
        model.addAttribute("errorTitle", "페이지를 찾을 수 없습니다");
        model.addAttribute("errorDescription", "요청하신 페이지가 존재하지 않거나 이동되었을 수 있습니다.");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("requestUri", ex.getRequestURL());
        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        model.addAttribute("statusCode", 403);
        model.addAttribute("errorTitle", "접근이 거부되었습니다");
        model.addAttribute("errorDescription", "이 페이지에 접근할 권한이 없습니다.");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, Model model) {
        model.addAttribute("statusCode", 405);
        model.addAttribute("errorTitle", "허용되지 않는 요청 방법입니다");
        model.addAttribute("errorDescription", "요청하신 HTTP 메서드가 이 페이지에서 지원되지 않습니다.");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("statusCode", 500);
        model.addAttribute("errorTitle", "서버 오류가 발생했습니다");
        model.addAttribute("errorDescription", "서버에서 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("exception", ex.getMessage());
        return "error";
    }
} 