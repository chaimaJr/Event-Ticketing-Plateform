package com.backend.backend.services;

import com.backend.backend.models.Event;
import com.backend.backend.models.EventStatus;
import com.backend.backend.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Event not found"));
    }

    public Event getByLocation(String location) {
        return eventRepository.findByLocation(location);
    }

    public List<Event> getAvailableEvents() {
        return eventRepository.findAvailableEvents();
    }

    public Event create(Event event) {
        if(event.getName() == null || event.getName().isEmpty()) {
            throw new RuntimeException("Event name cannot be empty");
        }
        if(event.getDate() == null || event.getDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Event date should be in the future");
        }
        event.setTicketsSold(0);

        return eventRepository.save(event);
    }

    public Event update(Long id, Event updatedEvent) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Event not found")
        );

        if(event.getTicketsSold() > 0) {
            throw new RuntimeException("Event has tickets sold! Can't be updated");
        }

        event.setName(updatedEvent.getName());
        event.setDescription(updatedEvent.getDescription());
        event.setCategory(updatedEvent.getCategory());
        event.setStatus(updatedEvent.getStatus());
        event.setDate(updatedEvent.getDate());
        event.setLocation(updatedEvent.getLocation());
        event.setBannerUrl(updatedEvent.getBannerUrl());
        event.setPrice(updatedEvent.getPrice());
        event.setTotalTickets(updatedEvent.getTotalTickets());

        return eventRepository.saveAndFlush(event);
    }

    public Event cancel(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Event not found")
        );
        if(event.getTicketsSold() > 0) {
            throw new RuntimeException("Event has tickets sold! Can't be cancelled");
        }
        event.setStatus(EventStatus.CANCELED);

        return eventRepository.saveAndFlush(event);
    }


    public void delete(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Event not found")
        );

        if(event.getTicketsSold() > 0) {
            throw new RuntimeException("Event has tickets sold! Can't be deleted");
        }
        eventRepository.deleteById(id);
    }


    public int getTicketsSold(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Event not found")
        );
        return event.getTicketsSold();
    }

    public void increaseTicketsSold(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Event not found")
        );
        if(event.getTicketsSold() >= event.getTotalTickets()) {
            throw new RuntimeException("Event has sold all tickets");
        }

        event.setTicketsSold(event.getTicketsSold() + 1);
        if (event.getTicketsSold() == event.getTotalTickets()){
            event.setStatus(EventStatus.SOLD_OUT);
        }

        eventRepository.saveAndFlush(event);
    }
    public void decreaseTicketsSold(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Event not found")
        );
        event.setTicketsSold(event.getTicketsSold() - 1);
        eventRepository.saveAndFlush(event);
    }


}
