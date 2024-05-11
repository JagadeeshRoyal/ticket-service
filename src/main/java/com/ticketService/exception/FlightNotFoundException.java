package com.ticketService.exception;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(Long flightId) {
        super("Flight not found with ID: " + flightId);
    }
}
