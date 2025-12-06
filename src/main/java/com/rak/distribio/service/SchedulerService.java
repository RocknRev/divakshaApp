package com.rak.distribio.service;

import com.rak.distribio.entity.User;
import com.rak.distribio.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SchedulerService {

	private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
	private static final int WEEKS_INACTIVE_THRESHOLD = 5;

	private final UserRepository userRepository;
	private final UserService userService;

	public SchedulerService(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@Scheduled(cron = "0 0 * * * *") // Runs every hour
	public void checkAndMarkInactiveUsers() {
		logger.info("Starting scheduled check for inactive users");

		OffsetDateTime cutoffDate = OffsetDateTime.now().minus(WEEKS_INACTIVE_THRESHOLD, ChronoUnit.WEEKS);
		List<User> inactiveUsers = userRepository.findInactiveUsersSince(cutoffDate);

		logger.info("Found {} users to mark as inactive", inactiveUsers.size());

		for (User user : inactiveUsers) {
			try {
				// Only mark as inactive if they haven't had a sale in the last 5 weeks
				if (user.getLastSaleAt() == null || user.getLastSaleAt().isBefore(cutoffDate)) {
					userService.markUserInactive(user.getId());
					logger.info("Marked user {} as inactive (last sale: {})", user.getId(), user.getLastSaleAt());
				}
			} catch (Exception e) {
				logger.error("Error marking user {} as inactive", user.getId(), e);
			}
		}

		logger.info("Completed scheduled check for inactive users");
	}
}

