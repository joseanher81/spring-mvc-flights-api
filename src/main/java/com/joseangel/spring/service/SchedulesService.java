package com.joseangel.spring.service;

import com.joseangel.spring.model.Schedule;

public interface SchedulesService {

	public Schedule getSchedule(String departure, String arrival, int year, int month);

}
