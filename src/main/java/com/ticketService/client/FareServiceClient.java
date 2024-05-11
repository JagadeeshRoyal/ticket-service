package com.ticketService.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ticketService.entity.Flight;


@FeignClient(name = "fare-service")
public interface FareServiceClient {
	 @GetMapping("/flights/{id}")
	 public Flight getFlightById(@PathVariable Long id);
	 
	 @GetMapping("/flights")
	    public List<Flight> getAllFlights();
}
