package com.joseangel.spring.service;

import java.util.List;

import com.joseangel.spring.model.Route;

public interface RoutesService {

	public List<Route> getAllRoutes();
	
	public List<Route> getEmptyConnAirptRoutes();
	
}
