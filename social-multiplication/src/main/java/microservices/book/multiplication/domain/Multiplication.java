package microservices.book.multiplication.domain;

/**
 * This class represents multiplication in our application.
 * */
public class Multiplication {

	//Both factors
	private int factorA;
	private int factorB;
	
	//The result of operation A*B
	private int result;
	
	public Multiplication(int factrA, int factrB) {
		this.factorA = factrA;
		this.factorB = factrB;
		this.result = factrA * factrB;
	}

	public int getFactorA() {
		return factorA;
	}

	public int getFactorB() {
		return factorB;
	}

	public int getResult() {
		return result;
	}
	
	@Override
	public String toString() {
		return "Multiplcation{"
				+ "factorA= " + factorA +
				", factorB= " + factorB +
				", result(A*B)=" + result + 
				"}";
	}
}
