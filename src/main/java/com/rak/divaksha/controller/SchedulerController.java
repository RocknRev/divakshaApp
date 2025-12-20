package com.rak.divaksha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.rak.divaksha.service.SchedulerService;

public class SchedulerController {
    
    @Autowired
    private SchedulerService schedulerService;

    @Scheduled(cron = "0 0 * * * *") // Runs every hour
	public void checkAndMarkInactiveUsers() {
        schedulerService.checkAndMarkInactiveUsers();
    }
}
