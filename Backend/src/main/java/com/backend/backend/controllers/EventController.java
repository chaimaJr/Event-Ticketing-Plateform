package com.backend.backend.controllers;

import com.backend.backend.models.Event;
import com.backend.backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;


//    ---------- Get ----------

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }


//    ---------- Create ----------

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.status(201).body(eventService.create(event));
    }


    //    ---------- Update ----------

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return ResponseEntity.ok(eventService.update(id, event));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Event> cancelEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.cancel(id));
    }


//    ---------- Delete ----------

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.ok("Event deleted successfully");
    }



//    ---------- GET ----------

    @GetMapping("/{id}/sold-tickets")
    public ResponseEntity<Integer> getSoldTicketsNb(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getTicketsSold(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> getEventsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(eventService.getByLocation(location));
    }


    @GetMapping("/available")
    public ResponseEntity<List<Event>> getAvailableEvents() {
        return ResponseEntity.ok(eventService.getAvailableEvents());
    }



//    ---------- Tickets ----------

    @PutMapping("/{id}/add-ticket")
    public ResponseEntity<Event> addSoldTicket(@PathVariable Long id) {
        eventService.increaseTicketsSold(id);
        return ResponseEntity.ok(eventService.getById(id));
    }

    @PutMapping("/{id}/remove-ticket")
    public ResponseEntity<Event> removeSoldTicket(@PathVariable Long id) {
        eventService.decreaseTicketsSold(id);
        return ResponseEntity.ok(eventService.getById(id));
    }



    // ---------- Exception Handling ----------

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

}
