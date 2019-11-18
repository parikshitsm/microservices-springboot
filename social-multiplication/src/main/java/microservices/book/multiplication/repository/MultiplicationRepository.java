package microservices.book.multiplication.repository;

import org.springframework.data.repository.CrudRepository;

import microservices.book.multiplication.domain.lombok.Multiplication;

/**
 * This interface allows us to save and retrieve Multiplications. 
 * */
public interface MultiplicationRepository extends CrudRepository<Multiplication, Long>{

}
