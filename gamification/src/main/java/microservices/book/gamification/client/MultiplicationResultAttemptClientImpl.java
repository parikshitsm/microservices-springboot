package microservices.book.gamification.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import microservices.book.gamification.client.dto.MultiplicationResultAttempt;

@Component
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient{
	
	private final RestTemplate restTemplate;
	private final String multiplicationHost;

	
	@Autowired
	public MultiplicationResultAttemptClientImpl(final RestTemplate template, @Value("${multiplicationHost}") final String mHost) {
		this.restTemplate = template;
		this.multiplicationHost = mHost;
	}
	
	@Override
	public MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(long multiplicationResultAttemptId) {
		return restTemplate.getForObject(multiplicationHost + "/results/" + multiplicationResultAttemptId, MultiplicationResultAttempt.class);
	}

}
