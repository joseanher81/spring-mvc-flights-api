package com.joseangel.spring.model;

import java.util.List;

public class InterconnectionsOut {

	private int stops;
	private List<Leg> legs;
	
	// Constructors
	public InterconnectionsOut() {

	}
	
	public InterconnectionsOut(int stops, List<Leg> legs) {
		this.stops = stops;
		this.legs = legs;
	}

	// Getters and setters
	public int getStops() {
		return stops;
	}

	public void setStops(int stops) {
		this.stops = stops;
	}

	public List<Leg> getLegs() {
		return legs;
	}

	public void setLegs(List<Leg> legs) {
		this.legs = legs;
	}
	
	
}
