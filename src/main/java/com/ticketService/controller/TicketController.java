//package com.ticketService.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.ticketService.client.FareServiceClient;
//import com.ticketService.entity.Flight;
//import com.ticketService.entity.Ticket;
//import com.ticketService.repository.TicketRepository;
//import com.ticketService.service.TicketService;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.Random;
//
//@RestController
//@RequestMapping("/api/tickets")
//public class TicketController {
//	@Autowired
//    private final TicketService ticketService;
//	@Autowired
//	public TicketRepository ticketRepository;
//    @Autowired
//	private FareServiceClient fareServiceClient;
//    @Autowired
//    private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);
//
//    public TicketController(TicketService ticketService) {
//        this.ticketService = ticketService;
//    }
//
//    // Endpoint to create a new ticket
//    @PostMapping
//    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
//        Ticket newTicket = ticketService.createTicket(ticket);
//        LOGGER.info("createTicket");
//        return ResponseEntity.ok(newTicket);
//    }
//
//    // Endpoint to get a ticket by its ID
//    @GetMapping("/{ticketId}")
//    public ResponseEntity<Ticket> getTicketById(@PathVariable Long ticketId) {
//        Optional<Ticket> ticket = ticketService.getTicketById(ticketId);
//        LOGGER.info("getTicketById");
//        return ticket.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//
//    // Endpoint to update an existing ticket
//    @PutMapping("/{ticketId}")
//    public ResponseEntity<Ticket> updateTicket(@PathVariable Long ticketId, @RequestBody Ticket ticketDetails) {
//        Ticket updatedTicket = ticketService.updateTicket(ticketId, ticketDetails);
//        LOGGER.info("updateTicket: {}",ticketId);
//        return ResponseEntity.ok(updatedTicket);
//    }
//
//    // Endpoint to delete a ticket by its ID
//    @DeleteMapping("/{ticketId}")
//    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
//        ticketService.deleteTicket(ticketId);
//        LOGGER.info("deleteTicket: {}",ticketId);
//        return ResponseEntity.noContent().build();
//    }
//    
//    @PostMapping("/{flightId}/{passengers}")
//    public ResponseEntity<Ticket> bookTicket(@PathVariable Long flightId, @PathVariable int passengers) {
//        // Get flight details from the FareServiceClient
//        Flight flight = fareServiceClient.getFlightById(flightId);
//
//        if (flight == null) {
//            // Return a 404 status code if the flight is not found
//        	LOGGER.info("flightResponse");
//        	
//            return ResponseEntity.notFound().build();
//            
//        }
//
//        // Check if there are enough available seats
//        if (flight.getAvailableSeats() < passengers) {
//            // Return a 400 Bad Request status if there aren't enough seats
//        	LOGGER.info("checking getAvailableSeats");
//            return ResponseEntity.badRequest().build();
//        }
//
//        // Calculate the total fare
//        double totalFare = flight.getPrice() * passengers;
//
//        // Create a new ticket
//        Ticket ticket = new Ticket();
//        ticket.setTicketId(new Random().nextLong());
//        ticket.setFlightId(flightId);
//        ticket.setPassengers(passengers);
//        ticket.setTotalFare(totalFare);
//
//        // Save the ticket in the repository
//        Ticket savedTicket = ticketRepository.save(ticket);
//        LOGGER.info("bookTicket");
//
//        // Return the saved ticket with a 201 Created status
//        return ResponseEntity.status(201).body(savedTicket);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Ticket>> getAllTickets() {
//        // Retrieve all tickets from the repository
//        List<Ticket> tickets = ticketRepository.findAll();
//        LOGGER.info("getAllTickets");
//
//        // Return the list of tickets with a 200 OK status
//        return ResponseEntity.ok(tickets);
//    }
//   
//
//}

package com.ticketService.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ticketService.client.FareServiceClient;
import com.ticketService.entity.Flight;
import com.ticketService.entity.Ticket;
import com.ticketService.exception.FlightNotFoundException;
import com.ticketService.exception.InsufficientSeatsException;
import com.ticketService.exception.TicketNotFoundException;
import com.ticketService.repository.TicketRepository;
import com.ticketService.service.TicketService;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private FareServiceClient fareServiceClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        Ticket newTicket = ticketService.createTicket(ticket);
        LOGGER.info("createTicket");
        return ResponseEntity.ok(newTicket);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<Optional<Ticket>> getTicketById(@PathVariable Long ticketId) {
        Optional<Ticket> ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException(ticketId);
        }
        LOGGER.info("getTicketById: {}", ticketId);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long ticketId, @RequestBody Ticket ticketDetails) {
        Ticket updatedTicket = ticketService.updateTicket(ticketId, ticketDetails);
        if (updatedTicket == null) {
            throw new TicketNotFoundException(ticketId);
        }
        LOGGER.info("updateTicket: {}", ticketId);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        boolean deleted = ticketService.deleteTicket(ticketId);
        if (!deleted) {
            throw new TicketNotFoundException(ticketId);
        }
        LOGGER.info("deleteTicket: {}", ticketId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{flightId}/{passengers}")
    public ResponseEntity<Ticket> bookTicket(@PathVariable Long flightId, @PathVariable int passengers) {
        Flight flight = fareServiceClient.getFlightById(flightId);
        if (flight == null) {
            throw new FlightNotFoundException(flightId);
        }

        if (flight.getAvailableSeats() < passengers) {
            throw new InsufficientSeatsException(flight.getAvailableSeats());
        }

        double totalFare = flight.getPrice() * passengers;
        Ticket ticket = new Ticket();
        ticket.setTicketId(new Random().nextLong());
        ticket.setFlightId(flightId);
        ticket.setPassengers(passengers);
        ticket.setTotalFare(totalFare);

        Ticket savedTicket = ticketRepository.save(ticket);
        LOGGER.info("bookTicket: {}", savedTicket.getTicketId());
        return ResponseEntity.status(201).body(savedTicket);
    }
    
    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights() {
        // Retrieve all flights using the FareServiceClient
        List<Flight> flights = fareServiceClient.getAllFlights();
        
        // Log the retrieval operation
        LOGGER.info("Retrieved all flights");
        
     // Return the list of flights with a 200 OK status
        return ResponseEntity.ok(flights);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        LOGGER.info("getAllTickets");
        return ResponseEntity.ok(tickets);
    }
}

