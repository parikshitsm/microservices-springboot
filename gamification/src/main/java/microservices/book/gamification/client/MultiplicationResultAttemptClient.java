package microservices.book.gamification.client;

import microservices.book.gamification.client.dto.MultiplicationResultAttempt;

/**
 * This interface allows us to connect to the Multiplication
 * microservice. 
 * */
public interface MultiplicationResultAttemptClient {

	MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(final long multiplicationId);
}
