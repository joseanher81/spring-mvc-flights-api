package com.joseangel.spring.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joseangel.spring.model.Day;
import com.joseangel.spring.model.Flight;
import com.joseangel.spring.model.InterconnectionsOut;
import com.joseangel.spring.model.Leg;
import com.joseangel.spring.model.Route;
import com.joseangel.spring.model.Schedule;
import com.joseangel.spring.service.RoutesService;
import com.joseangel.spring.service.SchedulesService;

@RestController
public class FlightsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightsController.class);

	@Autowired
	private RoutesService routesService;

	@Autowired
	private SchedulesService schedulesService;

	private List<Route> routesList;

	/**
	 * Returns a list of flights departing from a given departure airport not
	 * earlier than the specified departure datetime and arriving to a given arrival
	 * airport not later than the specified arrival datetime. Layovers are 2 hours
	 * at least.
	 * 
	 * @param departure
	 *            String IATA airport code
	 * @param arrival
	 *            String IATA airport code
	 * @param depDateTime
	 *            Departure date time in ISO format
	 * @param arrDateTime
	 *            Arrival date time in ISO format
	 * @return list of all available flights meeting the given conditions
	 */
	@GetMapping("/interconnections")
	public List<InterconnectionsOut> responseRequest(@RequestParam("departure") String departure,
			@RequestParam("arrival") String arrival, @RequestParam("departureDateTime") String depDateTime,
			@RequestParam("arrivalDateTime") String arrDateTime) {

		// Out list
		List<InterconnectionsOut> responseRequestOut = new ArrayList<>();

		// Call to RouteService: get all routes from web service (only routes with no
		// connecting airport)
		routesList = routesService.getEmptyConnAirptRoutes();

		// Find all direct flights
		List<InterconnectionsOut> directFlightsList = new ArrayList<>();
		directFlightsList = findDirectFlights(departure, arrival, depDateTime, arrDateTime);

		// Find all 1 stop flights
		List<InterconnectionsOut> interConFlightsList = new ArrayList<>();
		interConFlightsList = findInterConFlights(departure, arrival, depDateTime, arrDateTime);

		// Merge lists of direct and interconnected flights
		responseRequestOut.addAll(directFlightsList);
		responseRequestOut.addAll(interConFlightsList);

		return responseRequestOut;
	}

	/**
	 * Receives an airport IATA code and returns all possible
	 * destinations from that airport
	 * 
	 * @param departure
	 *            String IATA airport code
	 * @return all possible arrival airports for given departure airport (String
	 *         IATA codes)
	 */
	private List<String> findAllPossibleArrivals(String departure) {

		LOGGER.info("Starting findAllPossibleArrivals");

		List<String> possibleArrivalsOut = new ArrayList<>();

		// Iterate the routes list to find all possible destinations for given departure
		for (Route route : routesList) {

			if (route.getAirportFrom().equals(departure)) {
				// Add airportTo to the list
				possibleArrivalsOut.add(route.getAirportTo());
			}

		}

		return possibleArrivalsOut;
	}

	/**
	 * Receives an origin an a destination airport and returns all
	 * interconnected flights with a maximum of one stop within the specified
	 * departure and arrival times if available
	 * 
	 * @param departure
	 *            String IATA airport code
	 * @param arrival
	 *            String IATA airport code
	 * @param depDateTime
	 *            Departure date time in ISO format
	 * @param arrDateTime
	 *            Arrival date time in ISO format
	 * @return InterconnectionsOut object with data about the stops and legs for the
	 *         available flights
	 */
	private List<InterconnectionsOut> findInterConFlights(String departure, String arrival, String depDateTime,
			String arrDateTime) {

		LOGGER.info("Starting findInterConFlights");

		List<InterconnectionsOut> findInterConFlightsOut = new ArrayList<>();

		// Find possible arrivals for given departure
		List<String> connectionsCandidates = findAllPossibleArrivals(departure);

		// Check whether the arrivals candidates have given arrival as a possible
		// destination
		List<String> verifiedConnections = new ArrayList<>();

		for (String candidate : connectionsCandidates) {

			// Find possible arrivals for each candidate
			List<String> candidateArrivals = findAllPossibleArrivals(candidate);

			// Verify whether given arrival is on the candidate arrivals list
			for (String candArr : candidateArrivals) {
				if (candArr.equals(arrival)) {
					verifiedConnections.add(candidate);
					break;
				}
			}

		}

		// Search direct flights from given departure to each connection and verify
		// whether is a valid interconnected flight
		for (String connection : verifiedConnections) {

			List<InterconnectionsOut> directFlightsFromDeparture = new ArrayList<>();
			directFlightsFromDeparture = findDirectFlights(departure, connection, depDateTime, arrDateTime);

			List<InterconnectionsOut> directFlightsFromConnection = new ArrayList<>();
			directFlightsFromConnection = findDirectFlights(connection, arrival, depDateTime, arrDateTime);

			if (directFlightsFromDeparture != null && directFlightsFromConnection != null) {
				// Find first valid interconnection
				for (InterconnectionsOut firstLeg : directFlightsFromDeparture) {

					for (InterconnectionsOut secondLeg : directFlightsFromConnection) {

						// Verify there is at least 2 hours gap
						LocalDateTime firstFlightArrival = LocalDateTime
								.parse(firstLeg.getLegs().get(0).getArrivalDateTime());
						LocalDateTime secondFlightDeparture = LocalDateTime
								.parse(secondLeg.getLegs().get(0).getDepartureDateTime());

						if (!firstFlightArrival.plusHours(2).isAfter(secondFlightDeparture)) {

							// The flights met conditions. Map to out object
							mapInterconnectedFlight(findInterConFlightsOut, firstLeg, secondLeg);

						}
					}
				}
			}
		}

		return findInterConFlightsOut;
	}

	/**
	 * Receives an origin an a destination airport and returns all
	 * direct flights within the specified departure and arrival times if available
	 * 
	 * @param departure
	 *            String IATA airport code
	 * @param arrival
	 *            String IATA airport code
	 * @param depDateTime
	 *            Departure date time in ISO format
	 * @param arrDateTime
	 *            Arrival date time in ISO format
	 * @return InterconnectionsOut object with data about the stops and legs for the
	 *         available flights
	 */
	private List<InterconnectionsOut> findDirectFlights(String departure, String arrival, String depDateTime,
			String arrDateTime) {

		LOGGER.info("Starting findDirectFlights");

		List<InterconnectionsOut> findDirectFlightsOut = new ArrayList<>();
		LocalDateTime start = LocalDateTime.parse(depDateTime);
		LocalDateTime end = LocalDateTime.parse(arrDateTime);

		// Search within given date range
		int monthIncrease = 0;
		while (start.plusMonths(monthIncrease).getMonthValue() <= end.getMonthValue()
				&& start.plusMonths(monthIncrease).getYear() <= end.getYear()) {

			// Call to ScheduleService: get schedules for departure and arrival airports
			// for a given month
			Schedule schedule = schedulesService.getSchedule(departure, arrival,
					start.plusMonths(monthIncrease).getYear(), start.plusMonths(monthIncrease).getMonthValue());

			// Iterate flights found on schedule
			if (schedule != null && schedule.getDays().length != 0) {

				// Iterate days of month
				for (Day day : schedule.getDays()) {

					// Iterate flights for each day
					for (Flight flight : day.getFlights()) {

						// Format departure and arrival dates for comparing
						LocalDateTime depTimeIso = LocalDateTime.of(start.plusMonths(monthIncrease).getYear(),
								start.plusMonths(monthIncrease).getMonthValue(), day.getDay(),
								LocalTime.parse(flight.getDepartureTime()).getHour(),
								LocalTime.parse(flight.getDepartureTime()).getMinute());

						LocalDateTime arrTimeIso = LocalDateTime.of(start.plusMonths(monthIncrease).getYear(),
								start.plusMonths(monthIncrease).getMonthValue(), day.getDay(),
								LocalTime.parse(flight.getArrivalTime()).getHour(),
								LocalTime.parse(flight.getArrivalTime()).getMinute());

						// Add flight to Out list (only if its dates are within specified date times
						// range)
						if (!depTimeIso.isBefore(start) && !arrTimeIso.isAfter(end)) {

							mapDirectFlight(departure, arrival, findDirectFlightsOut, depTimeIso, arrTimeIso);
						}
					}
				}
			}

			// Increase a month for iteration
			monthIncrease++;

		}

		return findDirectFlightsOut;
	}

	/**
	 * Maps an InterconnectionsOut object for direct flights
	 * 
	 * @param departure
	 *            String IATA airport code
	 * @param arrival
	 *            String IATA airport code
	 * @param flightsOutList
	 *            InterconnectionsOut list for adding direct flights
	 * @param depTimeIso
	 *            Departure date time in ISO format
	 * @param arrTimeIso
	 *            Arrival date time in ISO format
	 */
	private void mapDirectFlight(String departure, String arrival, List<InterconnectionsOut> flightsOutList,
			LocalDateTime depTimeIso, LocalDateTime arrTimeIso) {

		LOGGER.info("Starting mapDirectFlight");

		flightsOutList.add(new InterconnectionsOut());
		flightsOutList.get(flightsOutList.size() - 1).setStops(0);
		flightsOutList.get(flightsOutList.size() - 1).setLegs(new ArrayList<>());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().add(new Leg());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(0).setDepartureAirport(departure);
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(0).setArrivalAirport(arrival);
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(0).setDepartureDateTime(depTimeIso.toString());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(0).setArrivalDateTime(arrTimeIso.toString());

	}

	/**
	 * Maps an InterconnectionsOut object for interconnected flights
	 * 
	 * @param flightsOutList
	 *            InterconnectionsOut list for adding interconnected flights
	 * @param firstLeg
	 *            InterconnectionsOut object with first leg flight data
	 * @param secondLeg
	 *            InterconnectionsOut object with second leg flight data
	 */
	private void mapInterconnectedFlight(List<InterconnectionsOut> flightsOutList, InterconnectionsOut firstLeg,
			InterconnectionsOut secondLeg) {

		LOGGER.info("Starting mapInterconnectedFlight");

		flightsOutList.add(new InterconnectionsOut());
		flightsOutList.get(flightsOutList.size() - 1).setStops(1);
		flightsOutList.get(flightsOutList.size() - 1).setLegs(new ArrayList<>());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().add(new Leg());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().add(new Leg());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(0)
				.setDepartureAirport(firstLeg.getLegs().get(0).getDepartureAirport());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(0)
				.setArrivalAirport(firstLeg.getLegs().get(0).getArrivalAirport());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(0)
				.setDepartureDateTime(firstLeg.getLegs().get(0).getDepartureDateTime());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(0)
				.setArrivalDateTime(firstLeg.getLegs().get(0).getArrivalDateTime());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(1)
				.setDepartureAirport(secondLeg.getLegs().get(0).getDepartureAirport());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(1)
				.setArrivalAirport(secondLeg.getLegs().get(0).getArrivalAirport());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(1)
				.setDepartureDateTime(secondLeg.getLegs().get(0).getDepartureDateTime());
		flightsOutList.get(flightsOutList.size() - 1).getLegs().get(1)
				.setArrivalDateTime(secondLeg.getLegs().get(0).getArrivalDateTime());
	}

}
