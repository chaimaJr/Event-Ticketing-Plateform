package com.backend.backend.repositories;

import com.backend.backend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByLocation(String location);

    @Query("SELECT e FROM Event e WHERE e.ticketsSold < e.totalTickets")
    List<Event> findAvailableEvents();
}
