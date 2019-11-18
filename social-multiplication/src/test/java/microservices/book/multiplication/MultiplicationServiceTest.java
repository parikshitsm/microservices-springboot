package microservices.book.multiplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.service.MultiplicationService;
import microservices.book.multiplication.service.RandomGeneratorService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiplicationServiceTest {

	@MockBean
	private RandomGeneratorService randomGeneratorService;
	
	@Autowired
	private MultiplicationService multiplicationService;
	
	@Test
	public void createRandomMultiplicationTest() {
		//given (our mocked RandomGenerator Service will return first 50, then 30)
		given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);
		
		//when
		Multiplication mutiplication = multiplicationService.createRandomMultiplication();
		
		//then
		assertThat(mutiplication.getFactorA()).isEqualTo(50);
		assertThat(mutiplication.getFactorB()).isEqualTo(30);
		assertThat(mutiplication.getResult()).isEqualTo(1500);
	}
}
