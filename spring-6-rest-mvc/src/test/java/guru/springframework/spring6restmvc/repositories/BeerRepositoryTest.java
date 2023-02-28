package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeerTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            this.beerRepository.save(Beer.builder()
                    .beerName("My Beer01234567890123456789012345678901234567890123456789")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("23432343234")
                    .price(new BigDecimal("11.99"))
                    .build());
            this.beerRepository.flush();
        });
    }

    @Test
    void testSaveBeer() {
        Beer savedBeer = this.beerRepository.save(Beer.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("23432343234")
                .price(new BigDecimal("11.99"))
                .build());
        this.beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

}