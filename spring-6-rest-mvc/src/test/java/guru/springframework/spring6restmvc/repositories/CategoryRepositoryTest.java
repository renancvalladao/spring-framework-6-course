package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BeerRepository beerRepository;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        this.testBeer = this.beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testAddCategory() {
        Category savedCategory = this.categoryRepository.save(Category.builder()
                .description("Ales")
                .build());

        this.testBeer.addCategory(savedCategory);
        Beer savedBeer = this.beerRepository.save(this.testBeer);
    }

}