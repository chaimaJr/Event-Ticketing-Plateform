package com.backend.backend.repositories;

import com.backend.backend.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository  extends JpaRepository<Ticket, Long> {
}
