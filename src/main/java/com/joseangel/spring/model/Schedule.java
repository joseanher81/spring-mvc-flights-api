package com.joseangel.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule {
	
	private int month;
	private Day[] days;
	
	// Constructors
	public Schedule() {

	}
	
	public Schedule(int month, Day[] days) {
		this.month = month;
		this.days = days;
	}

	// Getters and setters
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Day[] getDays() {
		return days;
	}

	public void setDays(Day[] days) {
		this.days = days;
	}
	
	
	
}
