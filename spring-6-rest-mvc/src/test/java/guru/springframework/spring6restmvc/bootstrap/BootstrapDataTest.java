package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.services.BeerCsvService;
import guru.springframework.spring6restmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BeerCsvService beerCsvService;
    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        this.bootstrapData = new BootstrapData(this.beerRepository, this.customerRepository, this.beerCsvService);
    }

    @Test
    void testRun() throws Exception {
        this.bootstrapData.run();

        assertThat(this.beerRepository.count()).isEqualTo(2413);
        assertThat(this.customerRepository.count()).isEqualTo(3);
    }

}