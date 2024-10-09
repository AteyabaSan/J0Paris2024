package com.joparis2024.events;

import com.joparis2024.dto.CategoryDTO;
import org.springframework.context.ApplicationEvent;

public class CategoryUpdatedEvent extends ApplicationEvent {
    
    private static final long serialVersionUID = 1L;

    private final CategoryDTO categoryDTO;

    public CategoryUpdatedEvent(Object source, CategoryDTO categoryDTO) {
        super(source);
        this.categoryDTO = categoryDTO;
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }
}
