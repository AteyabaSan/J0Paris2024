package com.joparis2024.dto;

import java.time.LocalDateTime;

public class EventDTO {

    private Long id;
    private String name;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String category;
    private String description;
    private String session;

    // Constructors
    public EventDTO() {}

    public EventDTO(Long id, String name, String location, LocalDateTime startTime, LocalDateTime endTime, String category, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
}

