package com.joparis2024.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	public List<Event> findByCategory(String category);
	public Optional<Event> findByEventName(String eventName);

}

