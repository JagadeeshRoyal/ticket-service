package com.ticketService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketService.entity.Ticket;
import com.ticketService.repository.TicketRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // Method to create a new ticket
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    // Method to get a ticket by its ID
    public Optional<Ticket> getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    // Method to get all tickets
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // Method to update an existing ticket
    public Ticket updateTicket(Long ticketId, Ticket ticketDetails) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        ticket.setFlightId(ticketDetails.getFlightId());
        ticket.setPassengers(ticketDetails.getPassengers());
        ticket.setTotalFare(ticketDetails.getTotalFare());
        return ticketRepository.save(ticket);
    }

    // Method to delete a ticket by its ID
    public boolean deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
		return false;
    }

}
