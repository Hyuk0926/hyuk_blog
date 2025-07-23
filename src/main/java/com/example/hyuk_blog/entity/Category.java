package com.example.hyuk_blog.entity;

public enum Category {
    HTML("HTML", "HTML"),
    CSS("CSS", "CSS"),
    JAVASCRIPT("JavaScript", "JavaScript"),
    REACT("React", "React"),
    SQL("SQL", "SQL"),
    JAVA("Java", "Java"),
    PYTHON("Python", "Python"),
    BLOG_CODING("BlogBuild", "ブログ構築");

    private final String displayName;
    private final String displayNameJp;

    Category(String displayName, String displayNameJp) {
        this.displayName = displayName;
        this.displayNameJp = displayNameJp;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayNameJp() {
        return displayNameJp;
    }
}
