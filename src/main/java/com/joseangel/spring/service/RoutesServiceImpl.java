package com.joseangel.spring.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.joseangel.spring.model.Route;

/**
 * Service for requesting flight routes to the specified microservice
 * @author Jose Angel Hernandez Garcia
 *
 */
@Service
public class RoutesServiceImpl implements RoutesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoutesServiceImpl.class);
	private static final String ROUTES_SERVICE_URL = "https://api.ryanair.com/core/3/routes";

	/**
	 * Returns all available routes
	 * @return list with all available routes
	 */
	@Override
	public List<Route> getAllRoutes() {

		RestTemplate restTemplate = new RestTemplate();

		// Get all routes from web service as an array of Route objects
		Route[] routes = null;
		try {
			LOGGER.info("Routes API request: " + ROUTES_SERVICE_URL);
			routes = restTemplate.getForObject(ROUTES_SERVICE_URL, Route[].class);
		} catch (RestClientException e) {
			LOGGER.error("Error getting Routes " + e.getMessage());
			e.printStackTrace();
		}

		// Transform array of Route objects to a list
		List<Route> routesList = Arrays.asList(routes);

		return routesList;
	}

	/**
	 * Returns only routes with empty connectingAirport
	 * @return list with all available routes with empty connectingAirport
	 */
	@Override
	public List<Route> getEmptyConnAirptRoutes() {

		List<Route> allRoutes = this.getAllRoutes();
		List<Route> emptyConnAirpRoutes = new ArrayList<>();

		// Iterate all routes list
		for (Route route : allRoutes) {

			// Filter only routes with connectingAirport value set to null
			if (route.getConnectingAirport() == null) {
				emptyConnAirpRoutes.add(route);
			}
		}

		return emptyConnAirpRoutes;
	}

}
