package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIntegrationTest {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    BeerMapper beerMapper;
    @Autowired
    WebApplicationContext wac;
    @Autowired
    ObjectMapper objectMapper;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void testListBeersByStyle() throws Exception {
        this.mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(548)));
    }

    @Test
    void testListBeersByName() throws Exception {
        this.mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(336)));
    }

    @Test
    void testPatchBeerBadName() throws Exception {
        Beer beer = this.beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name 01234567890123456789012345678901234567890123456789");

        this.mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testPatchNotFound() {
        assertThrows(NotFoundException.class, () -> this.beerController.updateBeerPatchById(UUID.randomUUID(), BeerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void patchExistingBeer() {
        Beer beer = this.beerRepository.findAll().get(0);
        final String beerName = "PATCHED";
        BeerDTO beerDTO = BeerDTO.builder().beerName(beerName).build();

        ResponseEntity<BeerDTO> responseEntity = this.beerController.updateBeerPatchById(beer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = this.beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> this.beerController.deleteById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        Beer beer = this.beerRepository.findAll().get(0);

        ResponseEntity<BeerDTO> responseEntity = this.beerController.deleteById(beer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(this.beerRepository.findById(beer.getId())).isEmpty();
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> this.beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingBeer() {
        Beer beer = this.beerRepository.findAll().get(0);
        BeerDTO beerDTO = this.beerMapper.beerToBeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity<BeerDTO> responseEntity = this.beerController.updateById(beer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = this.beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeerTest() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity<BeerDTO> responseEntity = this.beerController.handlePost(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(location[4]);

        Beer beer = this.beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }

    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> this.beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    void testGetById() {
        Beer beer = this.beerRepository.findAll().get(0);

        BeerDTO dto = this.beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testListBeers() {
        List<BeerDTO> dtos = this.beerController.listBeers(null, null);

        assertThat(dtos.size()).isEqualTo(2413);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        this.beerRepository.deleteAll();
        List<BeerDTO> dtos = this.beerController.listBeers(null, null);

        assertThat(dtos.size()).isEqualTo(0);
    }

}