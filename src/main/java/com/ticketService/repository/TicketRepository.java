package com.ticketService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticketService.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{

	
}
