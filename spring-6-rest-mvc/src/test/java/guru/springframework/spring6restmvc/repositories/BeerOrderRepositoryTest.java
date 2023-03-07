package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BeerRepository beerRepository;
    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        this.testCustomer = this.customerRepository.findAll().get(0);
        this.testBeer = this.beerRepository.findAll().get(0);
    }

    @Test
    void testBeerOrders() {
        System.out.println(this.beerOrderRepository.count());
        System.out.println(this.customerRepository.count());
        System.out.println(this.beerRepository.count());
        System.out.println(this.testCustomer.getName());
        System.out.println(this.testBeer.getBeerName());
    }

}