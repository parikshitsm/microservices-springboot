package microservices.book.multiplication.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.multiplication.domain.lombok.Multiplication;
import microservices.book.multiplication.service.MultiplicationService;

/**
 * This class implements a REST API for our Multiplication application.
 * */

@RestController
@RequestMapping("/multiplications")
public class MultiplicationController {
	
	private static final Logger log = Logger.getLogger(MultiplicationController.class);
	
	private final MultiplicationService multiplicationService;
	
	@Autowired
	public MultiplicationController(final MultiplicationService mtpService) {
		this.multiplicationService = mtpService;
	}
	
	@GetMapping("/random")
	Multiplication getRandomMultiplication() {
		log.info("REST API /multiplications/random called..");
		
		return multiplicationService.createRandomMultiplicationForUser();
	}

}
