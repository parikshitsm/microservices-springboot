package microservices.book.gamification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.service.LeaderBoardService;


/**
 * This class implements a REST API for the Gamification 
 * LeaderBoard service.
 * */

@RestController
@RequestMapping("/leaders")
public class LeaderBoardController {
	
	public final LeaderBoardService leaderBoardService;
	
	public LeaderBoardController(final LeaderBoardService service) {
		this.leaderBoardService = service;
	}
	
	@GetMapping
	public List<LeaderBoardRow> getLeaderBoard(){
		return leaderBoardService.getCurrentLeaderBoard();
	}
}
