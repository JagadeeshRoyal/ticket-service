package com.ticketService.exception;

public class InsufficientSeatsException extends RuntimeException {
    public InsufficientSeatsException(int availableSeats) {
        super("Not enough available seats. Available: " + availableSeats);
    }
}
