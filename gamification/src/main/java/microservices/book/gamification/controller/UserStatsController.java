package microservices.book.gamification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.service.GameService;

/**
 * This class implements a REST API for Gamification User
 * Statistics service.
 * */
@RestController
@RequestMapping("/stats")
public class UserStatsController {
	
	private final GameService gameService;
	
	public UserStatsController(final GameService gService) {
		this.gameService = gService;
	}
	
	@GetMapping
	public GameStats getStatsForUser(@RequestParam("userId") final Long userId) {
		return gameService.retrieveStatsForUser(userId);
	}
}
