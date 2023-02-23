package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    private final BeerService beerService;

    @PatchMapping("/{beerId}")
    public ResponseEntity<Beer> updateBeerPatchById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        this.beerService.patchBeerById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<Beer> deleteById(@PathVariable("beerId") UUID beerId) {
        this.beerService.deleteById(beerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<Beer> updateById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        this.beerService.updateBeerById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping
    public ResponseEntity<Beer> handlePost(@RequestBody Beer beer) {
        Beer savedBeer = this.beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers() {
        return this.beerService.listBeers();
    }

    @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get Beer by Id - in controller. Id: " + beerId.toString());

        return this.beerService.getBeerById(beerId);
    }

}
