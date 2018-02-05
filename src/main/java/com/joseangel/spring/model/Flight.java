package com.joseangel.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight {

	private String number;
	private String departureTime;
	private String arrivalTime;

	// Constructors
	public Flight() {

	}

	public Flight(String number, String departureTime, String arrivalTime) {
		this.number = number;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
	}

	// Getters and setters
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

}
