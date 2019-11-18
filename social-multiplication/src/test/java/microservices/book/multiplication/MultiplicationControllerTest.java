package microservices.book.multiplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import microservices.book.multiplication.controller.MultiplicationController;
import microservices.book.multiplication.domain.lombok.Multiplication;
import microservices.book.multiplication.service.MultiplicationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


/**
 * @WebMvcTest - This will initialize the Spring's web application context.
 * However, it will only load the configuration related to MVC layer (controllers), in contrast to 
 * @SpringBootTest, which loads entire spring configuration.
 * 
 * */

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationController.class)
public class MultiplicationControllerTest {
	
	/**
	 * We use @MockBean instead of @Mock annotation as we need to tell
	 * Spring not to inject the real bean (MultiplicationServiceImpl) but only a mock object,
	 * which we configure later with given() to return the expected Multiplication.
	 * We are isolating layers, we are only testing the controller, not the service.
	 * */
	@MockBean
	private MultiplicationService multiplicationService;
	
	@Autowired
	private MockMvc mvc;
	
	//This object will be magically initialized by the initFields method below
	/**
	 * The JacksonTester object will provide useful methods to check JSON contents.
	 * */
	private JacksonTester<Multiplication> json;
	
	@Before
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());	
	}
	
	@Test
	public void getRandomMultiplicationTest() throws Exception {
		//given
		given(multiplicationService.createRandomMultiplicationForUser()).willReturn(new Multiplication(70, 20));
		
		//when
		MockHttpServletResponse response = mvc
				.perform(get("/multiplications/random").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
		
		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
		assertThat(response.getContentAsString()).isEqualTo(json.write(new Multiplication(70, 20)).getJson());
	}
}
