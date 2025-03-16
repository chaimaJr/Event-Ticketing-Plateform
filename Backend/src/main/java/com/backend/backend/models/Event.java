package com.backend.backend.models;

import com.backend.backend.enums.Category;
import com.backend.backend.enums.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private LocalDate date;
    private LocalTime time;
    private String location;
    private String bannerUrl;
    private float price;

    private int totalTickets;
    private int ticketsSold;

}
