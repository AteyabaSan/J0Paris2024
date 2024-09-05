package com.joparis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joparis2024.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
