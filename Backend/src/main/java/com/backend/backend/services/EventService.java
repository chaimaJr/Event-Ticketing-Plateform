package com.backend.backend.services;

import com.backend.backend.models.Event;
import com.backend.backend.enums.EventStatus;
import com.backend.backend.repositories.EventRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;


    // -------- Get --------

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Event not found"));
    }

    public List<Event> getByLocation(String location) {
        return eventRepository.findByLocation(location);
    }

    public List<Event> getAvailableEvents() {
        return eventRepository.findAvailableEvents();
    }

    public int getTicketsSold(Long id) {
        Event event = getById(id);
        return event.getTicketsSold();
    }


    // -------- Create --------

    @Transactional
    public Event create(@NonNull Event event) {
        if(event.getName() == null || event.getName().isEmpty()) {
            throw new RuntimeException("Event name cannot be empty");
        }
        if(event.getDate() == null || event.getDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Event date should be in the future");
        }
        event.setTicketsSold(0);
        event.setStatus(EventStatus.SCHEDULED);
        return eventRepository.save(event);
    }



    // -------- Update --------

    @Transactional
    public Event update(Long id, Event updatedEvent) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Event not found")
        );
        if(event.getTicketsSold() > 0) {
            throw new RuntimeException("Event has tickets sold! Can't be updated");
        }

        // Update fields only if provided (optional partial update)
        if (updatedEvent.getName() != null) event.setName(updatedEvent.getName());
        if (updatedEvent.getDescription() != null) event.setDescription(updatedEvent.getDescription());
        if (updatedEvent.getCategory() != null) event.setCategory(updatedEvent.getCategory());
        if (updatedEvent.getStatus() != null) event.setStatus(updatedEvent.getStatus());
        if (updatedEvent.getDate() != null) event.setDate(updatedEvent.getDate());
        if (updatedEvent.getLocation() != null) event.setLocation(updatedEvent.getLocation());
        if (updatedEvent.getBannerUrl() != null) event.setBannerUrl(updatedEvent.getBannerUrl());
        if (updatedEvent.getPrice() >= 0) event.setPrice(updatedEvent.getPrice());
        if (updatedEvent.getTotalTickets() > 0) event.setTotalTickets(updatedEvent.getTotalTickets());

        return eventRepository.saveAndFlush(event);
    }


    // -------- Cancel --------

    @Transactional
    public Event cancel(Long id) {
        Event event = getById(id);

        if(event.getTicketsSold() > 0) {
            throw new RuntimeException("Event has tickets sold! Can't be cancelled");
        }
        event.setStatus(EventStatus.CANCELED);

        return eventRepository.save(event);
    }


    // -------- Delete --------

    @Transactional
    public void delete(Long id) {
        Event event = getById(id);

        if(event.getTicketsSold() > 0) {
            throw new RuntimeException("Event has tickets sold! Can't be deleted");
        }
        eventRepository.deleteById(id);
    }



    // -------- Tickets --------

    @Transactional
    public void increaseTicketsSold(Long id) {
        Event event = getById(id);

        if(event.getTicketsSold() >= event.getTotalTickets()) {
            throw new RuntimeException("Event has sold all tickets");
        }
        event.setTicketsSold(event.getTicketsSold() + 1);

        if (event.getTicketsSold() == event.getTotalTickets()){
            event.setStatus(EventStatus.SOLD_OUT);
        }

        eventRepository.save(event);
    }


    @Transactional
    public void decreaseTicketsSold(Long id) {
        Event event = getById(id);

        if(event.getTicketsSold() <= 0) {
            throw new RuntimeException("Sold tickets are already 0");
        }
        event.setTicketsSold(event.getTicketsSold() - 1);

        if(event.getStatus() == EventStatus.SOLD_OUT && event.getTicketsSold() < event.getTotalTickets()) {
            event.setStatus(EventStatus.SCHEDULED);
        }

        eventRepository.save(event);
    }


}
