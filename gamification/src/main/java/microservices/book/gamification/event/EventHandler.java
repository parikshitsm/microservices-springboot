package microservices.book.gamification.event;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.service.GameService;


@Slf4j
@Component
public class EventHandler {
	
	private GameService gameService;
	
	public EventHandler(final GameService service) {
		this.gameService = service;
	}
	
	@RabbitListener(queues="${multiplication.queue}")
	public void handleMultiplicationSolved(final MultiplicationSolvedEvent event) {
		System.out.println("handleMultiplicationSolved | Multiplication Solved Event received : " + event.getMultiplicationResultAttemptId());
		log.info("Multiplication Solved Event received : {}", event.getMultiplicationResultAttemptId());
		try {
			gameService.newAttemptForUser(event.getUserId(), event.getMultiplicationResultAttemptId(), event.isCorrect());
		} catch (final Exception e) {
			log.error("Error when trying to process MultiplicationSolvedEvent", e);
			//Avoids the event to be re-queued and reprocessed
			throw new AmqpRejectAndDontRequeueException(e);
		}
	}

}
