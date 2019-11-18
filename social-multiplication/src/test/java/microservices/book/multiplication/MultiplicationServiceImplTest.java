package microservices.book.multiplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.lombok.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.lombok.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import microservices.book.multiplication.service.MultiplicationServiceImpl;
import microservices.book.multiplication.service.RandomGeneratorService;

public class MultiplicationServiceImplTest {

	private MultiplicationServiceImpl multiplicationServiceImpl;
	
	/**
	 * Note that we don’t inject a mock bean with @MockBean but just use
	   the plain @Mock annotation to create a mock service, which we then
	   programmatically use to construct the MultiplicationServiceImpl object.
	 * */
	@Mock
	private RandomGeneratorService randomGeneratorService;
	
	@Mock
	private MultiplicationResultAttemptRepository attemptRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private EventDispatcher eventDispatcher;
	
	@Before
	public void setUp() {
		//with this call to initMocks we tell Mockito to process the annotations
		MockitoAnnotations.initMocks(this);
		multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService, attemptRepository, userRepository, eventDispatcher);
	}
	
	@Test
	public void createRandomMultiplicationTest() {
		//given (our mocked Random Generator service will return first 50, then 30)
		
		given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);
		
		//when
		Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();
		
		//then
		assertThat(multiplication.getFactorA()).isEqualTo(50);
		assertThat(multiplication.getFactorB()).isEqualTo(30);
		assertThat(multiplication.getResult()).isEqualTo(1500);
	}
	
	
	//This test uses project lombok jar
	@Test
	public void checkCorrectAttemptTest() {
		//given
		microservices.book.multiplication.domain.lombok.Multiplication multiplication = new microservices.book.multiplication.domain.lombok.Multiplication(50, 60);
		User user = new User("john_doe");
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
		MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, true);
		MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(attempt.getId(), attempt.getUser().getId(), true);
		given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());
		
		//when
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		//assert
		assertThat(attemptResult).isTrue();
		verify(attemptRepository).save(verifiedAttempt);
		verify(eventDispatcher).send(event);
	}
	
	//This test uses project lombok jar
	@Test
	public void checkWrongAttemptTest() {
		//given
		microservices.book.multiplication.domain.lombok.Multiplication multiplication = new microservices.book.multiplication.domain.lombok.Multiplication(50, 60);
		User user = new User("john_doe");
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);
		MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(attempt.getId(), attempt.getUser().getId(), false);
		given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());
		
		//when
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		//assert
		assertThat(attemptResult).isFalse();
		verify(attemptRepository).save(attempt);
		verify(eventDispatcher).send(event);
	}
	
	
	@Test
	public void retrieveStatsTest() {
		//given
		microservices.book.multiplication.domain.lombok.Multiplication multiplication = 
				new microservices.book.multiplication.domain.lombok.Multiplication(50, 60);
		User user = new User("john_doe");
		MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
		
		MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 3051, false);
		
		List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);
		
		given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());
		
		given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("john_doe")).willReturn(latestAttempts);
		
		//when
		List<MultiplicationResultAttempt> latestAttemptsResults = multiplicationServiceImpl.getStatsForUser("john_doe");
		
		//then
		assertThat(latestAttemptsResults).isEqualTo(latestAttempts);
	}
	
}
