package microservices.book.gamification.event;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


/**
 * Event that models the fact that a {@link Multiplication} has 
 * been solved in the system. Provides some context information about the 
 * multiplication.
 * */

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class MultiplicationSolvedEvent implements Serializable{

	private final Long multiplicationResultAttemptId;
	private final Long userId;
	private final boolean correct;
	
	//Json deserialization
	MultiplicationSolvedEvent() {
		multiplicationResultAttemptId = (long) -1;
		userId = (long) -1;
		correct = false;
	}
}
