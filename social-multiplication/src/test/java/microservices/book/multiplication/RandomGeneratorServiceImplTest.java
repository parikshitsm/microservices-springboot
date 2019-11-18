package microservices.book.multiplication;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import microservices.book.multiplication.service.RandomGeneratorServiceImpl;

public class RandomGeneratorServiceImplTest {

	private RandomGeneratorServiceImpl randomGeneratorServiceImpl;
	
	@Before
	public void setUp() {
		randomGeneratorServiceImpl = new RandomGeneratorServiceImpl();
	}
	
	/**
	 * We use a Java 8 Stream of the first 1000 numbers to mimic a for loop.
	   Then, we transform each number with map to a random int factor, we box
	   each one to an Integer object, and finally we collect them into a list. The
	   test checks that all of them are within the expected range that we define
	   using a similar approach.
	 * */
	
	@Test
	public void generateRandomFactorIsBetweenExpectedLimits() {
		
		//when a good sample of randomly generated factors is generated
		List<Integer> randomFactors = IntStream.range(0, 1000).map(i -> randomGeneratorServiceImpl.generateRandomFactor()).boxed().collect(Collectors.toList());
		
		//then all of them should be between 11 and 100 because we want a middle-complexity calculation
		assertThat(randomFactors).containsOnlyElementsOf(IntStream.range(11, 100).boxed().collect(Collectors.toList()));
	}
}
