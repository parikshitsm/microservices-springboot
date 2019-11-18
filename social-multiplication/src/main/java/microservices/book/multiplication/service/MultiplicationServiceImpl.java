package microservices.book.multiplication.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.lombok.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.lombok.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;

@Service
public class MultiplicationServiceImpl implements MultiplicationService{
	
	private static final Logger log = Logger.getLogger(MultiplicationServiceImpl.class);
	
	private RandomGeneratorService randomGeneratorService;
	
	private MultiplicationResultAttemptRepository attemptRepository;
	private UserRepository userRepository;
	
	private EventDispatcher eventDispatcher;

	@Autowired
	public MultiplicationServiceImpl(RandomGeneratorService rGenService, MultiplicationResultAttemptRepository resAttmptRepo, 
					UserRepository usrRepo, EventDispatcher eventDispatcher) {
		this.randomGeneratorService = rGenService;
		this.attemptRepository = resAttmptRepo;
		this.userRepository = usrRepo;
		this.eventDispatcher = eventDispatcher;
	}
	
	@Override
	public Multiplication createRandomMultiplication() {
		int factorA = randomGeneratorService.generateRandomFactor();
		int factorB = randomGeneratorService.generateRandomFactor();
		return new Multiplication(factorA, factorB);
	}
	
	
	@Transactional
	@Override
	public boolean checkAttempt(MultiplicationResultAttempt resultAttempt) {
		
		//check if the user already exists for that alias
		Optional<User> user = userRepository.findByAlias(resultAttempt.getUser().getAlias());
		
		//Avoids 'hack' attempts
		Assert.isTrue(!resultAttempt.isCorrect(), "You can't send an attempt marked as correct!!");
			
		//check if its correct
		boolean isCorrect = resultAttempt.getResultAttempt() == 
				resultAttempt.getMultiplication().getFactorA() * 
				resultAttempt.getMultiplication().getFactorB();
		
		MultiplicationResultAttempt checkedAttempt = 
				new MultiplicationResultAttempt(user.orElse(resultAttempt.getUser()), 
						resultAttempt.getMultiplication(), resultAttempt.getResultAttempt(), isCorrect);
		
		
		//Stores the attempt
		attemptRepository.save(checkedAttempt);
		
		//Communicates the result via event
		eventDispatcher.send(new MultiplicationSolvedEvent(checkedAttempt.getId(), checkedAttempt.getUser().getId(), checkedAttempt.isCorrect()));
		System.out.println("MultiplicationSolvedEvent sent successfully to RabbitMQ broker. Returning isCorrect = " + isCorrect);
		//Returns the result
		return isCorrect;
	}

	@Override
	public microservices.book.multiplication.domain.lombok.Multiplication createRandomMultiplicationForUser() {
		int factorA = randomGeneratorService.generateRandomFactor();
		int factorB = randomGeneratorService.generateRandomFactor();
		log.debug("Returning factorA = " + factorA + ", factorB = " + factorB);
		return new microservices.book.multiplication.domain.lombok.Multiplication(factorA, factorB);
	}
	
	
	@Override
	public List<MultiplicationResultAttempt> getStatsForUser(String userAlias){
		//Remember : This is Spring Data JPA feature, where we are using Spring's CrudRepository Interface method patterns
		//and we are not implementing any of the interface methods as such. This is Spring magic.
		return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
	}

	@Override
	public MultiplicationResultAttempt getResultById(final Long resultId) {
		 //BOOT2 : changed from findOne
		 return attemptRepository.findById(resultId).
				 orElseThrow(() -> new IllegalArgumentException("The requested resultId ["+resultId+"] does not exist."));
	}

	

}
