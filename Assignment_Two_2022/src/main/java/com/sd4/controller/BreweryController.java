/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.controller;

import com.sd4.model.Brewery;
import com.sd4.service.BeerService;
import com.sd4.service.BreweryService;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 *
 * @author paw
 */
//CHANGE TO SOMETHING DIFFERENT FROM BEER CONTROLLER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
@RestController
@RequestMapping("/brewery")
public class BreweryController {

    @Autowired
    private BeerService beerService;
    
    @Autowired
    private BreweryService breweryService;

    //@GetMapping(value = "/hateoas/{id}", produces = { "application/hal+json" })
    @GetMapping(value = "/hateoas/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Brewery> getOneWithHATEOAS(@PathVariable long id) {
        Optional<Brewery> o = breweryService.findOne(id);

        if (!o.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            Link selfLink = linkTo(methodOn(BreweryController.class)
                    .getAll()).withRel("allBreweries");
            o.get().add(selfLink);
            return ResponseEntity.ok(o.get());
        }
    }

    @GetMapping(value = "/hateoas/", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<Brewery> getAllWithHATEOAS() {
        //ADD RESPONSE ENTITY? WHEN LIST IS EMPTY
        List<Brewery> beerList = breweryService.findAll();
        for (final Brewery b : beerList) {
            long id = b.getId();
            Link selfLink = linkTo(BreweryController.class).slash(id).withSelfRel();
            b.add(selfLink);
            Link beerDetails = linkTo(methodOn(BreweryController.class)
                    .getBreweryDetails(id)).withRel("breweryDetails");
            b.add(beerDetails);
        }

        Link link = linkTo(BeerController.class).withSelfRel();
        CollectionModel<Brewery> result = CollectionModel.of(beerList, link);
        return result;
    }

    @GetMapping(value = "/details/{id}")
    public ResponseEntity<String> getBreweryDetails(@PathVariable long id) {
        Optional<Brewery> o = breweryService.findOne(id);
        //long breweryId = o.get().getBrewery_id();
        //NO METHOD FROM BREWERY WORK HERE!!!!!!!!!!!!!!!!!!
        //Optional<Brewery> brewery = breweryService.findOne(breweryId);        
        JSONObject resp = new JSONObject();
        String name = o.get().getName();
        String description = o.get().getDescription();
        try {
            resp.put("name", name);
            resp.put("description", description);
        } catch (JSONException ex) {
            Logger.getLogger(BeerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String details = "Name: " + o.get().getName() + " Description: " + o.get().getDescription();
        return ResponseEntity.ok(resp.toString());
    }
    @GetMapping("")
    public List<Brewery> getAll() {
        return breweryService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Brewery> getOne(@PathVariable long id) {
        Optional<Brewery> o = breweryService.findOne(id);

        if (!o.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(o.get());
        }
    }

    @GetMapping("count")
    public long getCount() {
        return breweryService.count();
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {
        breweryService.deleteByID(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity add(@RequestBody Brewery a) {
        breweryService.saveBrewery(a);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity edit(@RequestBody Brewery a) { //the edit method should check if the Author object is already in the DB before attempting to save it.
        breweryService.saveBrewery(a);
        return new ResponseEntity(HttpStatus.OK);
    }
}
