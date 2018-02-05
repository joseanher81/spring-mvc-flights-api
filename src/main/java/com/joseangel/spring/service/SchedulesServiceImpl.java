package com.joseangel.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.joseangel.spring.model.Schedule;

/**
 * Service for requesting to the specified microservice a list of available
 * flights for a given departure airport (IATA code), an arrival airport (IATA
 * code), a year and a month
 * 
 * @author Jose Angel Hernandez Garcia
 *
 */
@Service
public class SchedulesServiceImpl implements SchedulesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesServiceImpl.class);
	private static final String SCHEDULE_SERVICE_ROOT_URL = "https://api.ryanair.com/timetable/3/schedules/";

	/**
	 * Returns available flights for given a departure airport, arrival
	 * airport, a year and a month
	 * 
	 * @param departure
	 *            String departure airport in IATA code
	 * @param arrival
	 *            String arrival airport in IATA code
	 * @param year
	 *            int with a specific year
	 * @param month
	 *            int a specific month in the given year
	 * @return Schedule object with all the flights that meet the given conditions
	 */
	@Override
	public Schedule getSchedule(String departure, String arrival, int year, int month) {

		RestTemplate restTemplate = new RestTemplate();

		// Create url String for web service call
		String scheduleServiceUrl = SCHEDULE_SERVICE_ROOT_URL + departure + "/" + arrival + "/years/"
				+ Integer.toString(year) + "/months/" + Integer.toString(month);

		// Get schedules from web service as an array of Schedule objects
		Schedule schedule = null;
		try {
			LOGGER.info("Schedule API request: " + scheduleServiceUrl);
			schedule = restTemplate.getForObject(scheduleServiceUrl, Schedule.class);
		} catch (RestClientException e) {
			LOGGER.error("Error getting Schedules " + e.getMessage());
			e.printStackTrace();
		}

		return schedule;
	}

}
