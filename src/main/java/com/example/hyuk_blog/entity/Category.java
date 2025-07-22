package com.example.hyuk_blog.entity;

public enum Category {
    HTML,
    CSS,
    JAVASCRIPT,
    REACT,
    SQL,
    JAVA,
    PYTHON,
    BLOG_CODING("BlogBuild");

    private final String displayName;

    Category() {
        this.displayName = this.name();
    }

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
