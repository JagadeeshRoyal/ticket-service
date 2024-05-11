package com.ticketService.exception;

public class InvalidTicketIdException extends RuntimeException {
    public InvalidTicketIdException(Long ticketId) {
        super("Invalid ticket ID: " + ticketId);
    }
}
