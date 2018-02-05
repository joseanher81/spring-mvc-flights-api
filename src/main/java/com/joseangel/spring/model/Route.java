package com.joseangel.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

	private String airportFrom;
	private String airportTo;
	private String connectingAirport;
	private boolean newRoute;
	private boolean seasonalRoute;
	private String group;

	// Constructors
	public Route() {

	}

	public Route(String airportFrom, String airportTo, String connectingAirport, boolean newRoute,
			boolean seasonalRoute, String group) {
		this.airportFrom = airportFrom;
		this.airportTo = airportTo;
		this.connectingAirport = connectingAirport;
		this.newRoute = newRoute;
		this.seasonalRoute = seasonalRoute;
		this.group = group;
	}

	// Getters and setters
	public String getAirportFrom() {
		return airportFrom;
	}

	public void setAirportFrom(String airportFrom) {
		this.airportFrom = airportFrom;
	}

	public String getAirportTo() {
		return airportTo;
	}

	public void setAirportTo(String airportTo) {
		this.airportTo = airportTo;
	}

	public String getConnectingAirport() {
		return connectingAirport;
	}

	public void setConnectingAirport(String connectingAirport) {
		this.connectingAirport = connectingAirport;
	}

	public boolean isNewRoute() {
		return newRoute;
	}

	public void setNewRoute(boolean newRoute) {
		this.newRoute = newRoute;
	}

	public boolean isSeasonalRoute() {
		return seasonalRoute;
	}

	public void setSeasonalRoute(boolean seasonalRoute) {
		this.seasonalRoute = seasonalRoute;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
