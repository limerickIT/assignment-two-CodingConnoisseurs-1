/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.controller;

import com.sd4.model.Beer;
import com.sd4.model.Breweries_Geocode;
import com.sd4.model.Brewery;
import com.sd4.service.BeerService;
import com.sd4.service.Breweries_GeocodeService;
import com.sd4.service.BreweryService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
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

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import org.springframework.web.servlet.view.RedirectView;

//QR 
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @Autowired
    private Breweries_GeocodeService breweries_GeocodeService;

    @GetMapping(path = "/generateQR/{breweryId}", produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage generateQR(@PathVariable long breweryId) throws WriterException {
        Optional<Brewery> o = breweryService.findOne(breweryId);
        String address = "";
        String name = "";
        String phoneNo = "";
        String email = "";
        String website = "";
        
        if (!o.isPresent()) {
            //return error
        } else {
            name = o.get().getName();
            address = o.get().getName() + " " + o.get().getAddress1() + " " + o.get().getAddress2() + " " + o.get().getCity() + " " + o.get().getState() + " " + o.get().getCode() + " " + o.get().getCountry();
            email = o.get().getEmail();
            website = o.get().getWebsite();
            phoneNo = o.get().getPhone();
        }
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("BEGIN:VCARD");
        buffer.append("\nFN:").append(name);
        buffer.append("\nADR;TYPE=pref:").append(address);
        buffer.append("\nLABEL;TYPE=pref:").append("No");
        buffer.append("\nTEL;TYPE=work:").append(phoneNo);
        buffer.append("\nURL:").append(website);
        buffer.append("\nEMAIL;TYPE=pref:").append(email);
        buffer.append("\nEND:VCARD");
        
        String barcodeText = buffer.toString();
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 250, 250);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);

    }

    @RequestMapping(value = "/iFrameMap/{breweryId}", produces = MediaType.TEXT_HTML_VALUE)
    public String mapiFrameView(@PathVariable long breweryId) {
        Optional<Brewery> o = breweryService.findOne(breweryId);
        String breweryAddress = "";
        String webPage = "";
        Double lattitude;
        String longitude = "";
        if (!o.isPresent()) {
            breweryAddress = "No brewery with this address exists.";
        } else {

            Optional<Breweries_Geocode> breweriesGeocode = breweries_GeocodeService.findOne(Long.valueOf(breweryId));
            breweryAddress = o.get().getName() + " " + o.get().getAddress1() + " " + o.get().getAddress2() + " " + o.get().getCity() + " " + o.get().getState() + " " + o.get().getCode() + " " + o.get().getCountry();
            if (!breweriesGeocode.isPresent()) {
                webPage = " <p>" + breweryAddress + "</p><p>There is no map to be displayed due to lack of coordinates</p>";
            } else {
                lattitude = breweriesGeocode.get().getLatitude();
                longitude = breweriesGeocode.get().getLongitude().toString();
                webPage = " <p>" + breweryAddress + "</p><iframe width=\"100%\" height=\"100%\" src=\"https://www.google.com/maps/place/" + lattitude + "," + longitude + "\" title=\"W3Schools Free Online Web Tutorials\"></iframe> ";
            }

        }

        return webPage;

    }

    @RequestMapping(value = "/redirectMap")
    public RedirectView mapRedirectView() {

        RedirectView redirectView = new RedirectView("https://www.google.com/maps/place/52.6515619,-8.6651593");

        return redirectView;

    }

    @GetMapping(value = "/{breweryId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Brewery> getOneBrewery(@PathVariable long breweryId) {
        Optional<Brewery> o = breweryService.findOne(breweryId);

        if (!o.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            Link selfLink = linkTo(methodOn(BreweryController.class)
                    .getAllBreweries()).withRel("allBreweries");
            o.get().add(selfLink);
            return ResponseEntity.ok(o.get());
        }
    }

    @GetMapping(value = "/allBreweries", produces = MediaType.APPLICATION_XML_VALUE)
    public CollectionModel<Brewery> getAllBreweries() {
        //ADD RESPONSE ENTITY? WHEN LIST IS EMPTY
        List<Brewery> beerList = breweryService.findAll();
        for (final Brewery b : beerList) {
            long id = b.getId();
            Link selfLink = linkTo(BreweryController.class).slash(id).withSelfRel();
            b.add(selfLink);
            Link link = linkTo(methodOn(BreweryController.class)
                    .getBreweryDetails(id)).withRel("breweryDetails");
            b.add(link);
        }

        Link link = linkTo(BeerController.class).withSelfRel();
        CollectionModel<Brewery> result = CollectionModel.of(beerList, link);
        return result;
    }

    //get all beers by the brewery
    @GetMapping(value = "/breweryBeers/{breweryId}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<Beer> getBeersByBrewery(@PathVariable long breweryId) {
        Optional<Brewery> o = breweryService.findOne(breweryId);
        List<Beer> beerListBrewery = new ArrayList();

        List<Beer> beerList = beerService.findAll();
        for (final Beer b : beerList) {
            if (b.getBrewery_id() == breweryId) {
                beerListBrewery.add(b);
            }
        }
        Link link = linkTo(methodOn(BreweryController.class)
                .getAllBreweries()).withRel("allBreweries");
        o.get().add(link);
        CollectionModel<Beer> result = CollectionModel.of(beerListBrewery, link);
        return result;

    }

    @GetMapping(value = "/breweryDetails/{breweryId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> getBreweryDetails(@PathVariable long breweryId) {
        Optional<Brewery> o = breweryService.findOne(breweryId);       
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
    public ResponseEntity add(@Valid @RequestBody Brewery a) {
        breweryService.saveBrewery(a);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity edit(@Valid @RequestBody Brewery a) { //the edit method should check if the Author object is already in the DB before attempting to save it.
        breweryService.saveBrewery(a);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
