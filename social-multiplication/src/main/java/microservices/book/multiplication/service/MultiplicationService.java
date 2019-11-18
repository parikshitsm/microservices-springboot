package microservices.book.multiplication.service;

import java.util.List;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.lombok.MultiplicationResultAttempt;

public interface MultiplicationService {
	
	/**
	 * Generates a random {@link Multiplication} object.
	 * @return a multiplication of randomly generated numbers
	 * */
	Multiplication createRandomMultiplication();
	
	
	/**
	 * Generates a random {@link microservices.book.multiplication.domain.lombok.Multiplication} object.
	 * @return a multiplication of randomly generated numbers
	 * */
	microservices.book.multiplication.domain.lombok.Multiplication createRandomMultiplicationForUser();
	
	
	/**
	 * @return true if the attempt matches the result of the multiplication
	 * */
	boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);
	
	/**
	 * retrieve user attempts
	 * */
	List<MultiplicationResultAttempt> getStatsForUser(String userAlias);
	
	
	/**
	 * Gets an attempt by its id
	 * */
	MultiplicationResultAttempt getResultById(final Long resultId);
}
