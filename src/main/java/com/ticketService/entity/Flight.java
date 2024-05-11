package com.ticketService.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private Long id;
    private String name;
    private String source;
    private String destination;
    private LocalDate doj;
    private int availableSeats;
    private double price;
    private String classType;
}
