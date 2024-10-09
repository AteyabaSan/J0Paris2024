package com.joparis2024.events;

import org.springframework.context.ApplicationEvent;

public class CategoryDeletedEvent extends ApplicationEvent {
    
    private static final long serialVersionUID = 1L;

    private final Long categoryId;

    public CategoryDeletedEvent(Object source, Long categoryId) {
        super(source);
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
