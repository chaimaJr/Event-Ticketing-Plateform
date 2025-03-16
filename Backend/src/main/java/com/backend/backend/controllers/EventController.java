package com.backend.backend.controllers;

import com.backend.backend.models.Event;
import com.backend.backend.models.ResponseMessage;
import com.backend.backend.services.EventService;
import com.backend.backend.services.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final StorageService storageService;


//    ---------- Get ----------

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    // New endpoint to serve images
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Adjust based on file type if needed
                .body(file);
    }

//    ---------- Create ----------

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> createEvent(
            @RequestPart(value = "event") String eventJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        Event event = new Event();

        if (eventJson != null && !eventJson.trim().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
                event = mapper.readValue(eventJson, Event.class);
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseMessage("Invalid event data: " + e.getMessage()));
            }
        }

        String message = "";
        if(file != null) {
            try {
                String bannerUrl = storageService.save(file);
                event.setBannerUrl(bannerUrl.replace("/uploads/", "")); // Store just "filename.jpg"
//                event.setBannerUrl(bannerUrl);
                message = "Uploaded file: " + file.getOriginalFilename();
            } catch (Exception e) {
                message = "File upload failed: " + e.getMessage();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        try {
            eventService.create(event);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Event creation failed: " + e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(message));
    }

//    @PostMapping
//    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
//        return ResponseEntity.status(201).body(eventService.create(event));
//    }

//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Event> createEvent(
//            @RequestPart("event") Event event,
//            @RequestPart(value = "banner", required = false) MultipartFile bannerImg) throws IOException {
//
//        // Debugging
//        System.out.println("Received event: " + event);
//        System.out.println("Received banner: " + (bannerImg != null ? bannerImg.getOriginalFilename() : "null"));
//        // ----------------
//
//        if (bannerImg != null && !bannerImg.isEmpty()) {
//            String bannerUrl = eventService.uploadImage(bannerImg);
//            event.setBannerUrl(bannerUrl);
//        }
//
//        return ResponseEntity.status(201).body(eventService.create(event));
//    }

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
