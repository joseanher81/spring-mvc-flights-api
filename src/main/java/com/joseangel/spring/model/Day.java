package com.joseangel.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Day {

	private int day;
	private Flight[] flights;
	
	// Constructors
	public Day() {

	}
	
	public Day(int day, Flight[] flights) {
		this.day = day;
		this.flights = flights;
	}

	// Getters and setters
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public Flight[] getFlights() {
		return flights;
	}

	public void setFlights(Flight[] flights) {
		this.flights = flights;
	}
	
	
}
