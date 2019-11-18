package microservices.book.gamification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;

@Service
@Slf4j
public class GameServiceImpl implements GameService{
	
	public static final int LUCKY_NUMBER = 42;
	private ScoreCardRepository scoreCardRepo;
	private BadgeCardRepository badgeCardRepo;
	private MultiplicationResultAttemptClient mltpResAttemptClient;
	
	public GameServiceImpl(ScoreCardRepository scoreCardRepository, 
			BadgeCardRepository badgeCardRepository, MultiplicationResultAttemptClient attemptClient) {
		this.scoreCardRepo = scoreCardRepository;
		this.badgeCardRepo = badgeCardRepository;
		this.mltpResAttemptClient = attemptClient;
	}

	@Override
	public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
		//For the first version, we will give points only if its correct
		if(correct) {
			ScoreCard scoreCard = new ScoreCard(userId, attemptId);
			scoreCardRepo.save(scoreCard);
			log.info("User with id{} scored {} points for attempt id {}",
					userId, scoreCard.getScore(), attemptId);
			List<BadgeCard> badgeCards = processForBadges(userId, attemptId);
			return new GameStats(userId, scoreCard.getScore(),
					badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
		}
		return null;
	}

	@Override
	public GameStats retrieveStatsForUser(Long userId) {
		int score = scoreCardRepo.getTotalScoreForUser(userId);
		List<BadgeCard> badgeCards = badgeCardRepo.findByUserIdOrderByBadgeTimestampDesc(userId);
		return new GameStats(userId, score, 
				badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
	}
	
	
	/**
	 * Checks the total score and the different score cards obtained 
	 * to give new badges in case their conditions are met.
	 * */
	private List<BadgeCard> processForBadges(final Long userId, final Long attemptId){
		List<BadgeCard> badgeCards = new ArrayList<BadgeCard>();
		int totalScore = scoreCardRepo.getTotalScoreForUser(userId);
		log.info("New score for user {} is {}", userId, totalScore);
		
		List<ScoreCard> scoreCardList = scoreCardRepo.findByUserIdOrderByScoreTimestampDesc(userId);
		List<BadgeCard> badgeCardList = badgeCardRepo.findByUserIdOrderByBadgeTimestampDesc(userId);
		
		//Distribute badges depending on score
		checkAndGiveBadgeBasedOnScore(badgeCardList, 
				Badge.BRONZE_MULTIPLICATOR, totalScore, 100, userId).ifPresent(badgeCards::add);
		checkAndGiveBadgeBasedOnScore(badgeCardList, 
				Badge.SILVER_MULTIPLICATOR, totalScore, 500, userId).ifPresent(badgeCards::add);
		checkAndGiveBadgeBasedOnScore(badgeCardList, 
				Badge.GOLD_MULTIPLICATOR, totalScore, 999, userId).ifPresent(badgeCards::add);
		
		//First won badge
		if(scoreCardList.size() == 1 && !containsBadge(badgeCardList, Badge.FIRST_WON)) {
			BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
			badgeCards.add(firstWonBadge);
		}
		
		//Lucky number badge - This is a call from Gamification mS to Multiplication mS.
		MultiplicationResultAttempt attempt = 
				mltpResAttemptClient.retrieveMultiplicationResultAttemptById(attemptId);
		if(!containsBadge(badgeCards, Badge.LUCKY_NUMBER) 
				&& (LUCKY_NUMBER == attempt.getMultiplicationFactorA() || 
				LUCKY_NUMBER == attempt.getMultiplicationFactorB())) {
		
			BadgeCard luckyNumberBadge = giveBadgeToUser(Badge.LUCKY_NUMBER, userId);
			badgeCards.add(luckyNumberBadge);
		}
		
		
		return badgeCards;
	}
	
	
	/**
	 * Convenience method to check the current score against the different thresholds to gain badges. 
	 * It also assigns badge to user if the conditions are met.
	 * */
	private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(List<BadgeCard> badgeCards, Badge badge, 
			int score, int scoreThreshold, Long userId){
		
		if(score >= scoreThreshold && !containsBadge(badgeCards, badge)) {
			return Optional.of(giveBadgeToUser(badge, userId));
		}
		return Optional.empty();
	}
	
	
	/**
	 * Checks if the passed list of badges includes the one being checked.
	 * */
	private boolean containsBadge(List<BadgeCard> badgeCards, Badge badge) {
		return badgeCards.stream().anyMatch(b -> b.getBadge().equals(badge));
	}
	
	
	/**
	 * Assigns a new badge to the given user
	 * */
	private BadgeCard giveBadgeToUser(Badge badge, Long userId) {
		BadgeCard badgeCard = new BadgeCard(userId, badge);
		badgeCardRepo.save(badgeCard);
		log.info("User with id {} won a new badge: {}", userId, badge);
		return badgeCard;
	}
}
