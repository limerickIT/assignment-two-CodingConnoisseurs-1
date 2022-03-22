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
//import javax.swing.JEditorPane;  
//import javax.swing.JFrame; 
//import javax.swing.SwingUtilities;
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

    @GetMapping(path = "/generateQR", produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage generateQR() throws WriterException {
        String barcodeText = "Something";
//        QRCodeWriter barcodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix
//                = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
//
//        return MatrixToImageWriter.toBufferedImage(bitMatrix);

        //QRcode generator logic
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 250, 250);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);

    }

    @RequestMapping(value = "/iFrameMap/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String mapiFrameView(@PathVariable long id) {
        Optional<Brewery> o = breweryService.findOne(id);
        String breweryAddress = "";
        String webPage = "";
        Double lattitude;
        String longitude = "";
        if (!o.isPresent()) {
            breweryAddress = "No brewery with this address exists.";
        } else {

            Optional<Breweries_Geocode> breweriesGeocode = breweries_GeocodeService.findOne(Long.valueOf(id));
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

    @GetMapping(value = "/loc/{id}")
    public void getLoc(@PathVariable long id) throws IOException {
        JFrame myFrame = new JFrame();
//        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
//        myFrame.setSize(400, 200);  
//        JEditorPane myPane = new JEditorPane();  
//        myPane.setContentType("text/plain");  
//        myPane.setText("Sleeping is necessary for a healthy body."  
//                + " But sleeping in unnecessary times may spoil our health, wealth and studies."  
//                + " Doctors advise that the sleeping at improper timings may lead for obesity during the students days.");  
//        myFrame.setContentPane(myPane);  
//        myFrame.setVisible(true);        

//        Optional<Brewery> o = breweryService.findOne(id);
//        JEditorPane website = new JEditorPane("http://smo-gram.tumblr.com/");
//        website.setEditable(false);
//        JFrame frame = new JFrame("Google");
//        frame.add(new JScrollPane(website));
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 600);
//        frame.setVisible(true);
//         Browser browser = BrowserFactory.create();
//    JFrame frame = new JFrame("Google Map");
//    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//    frame.add(browser.getView().getComponent(), BorderLayout.CENTER);
//    frame.setSize(600, 400);
//    frame.setLocationRelativeTo(null);
//    frame.setVisible(true);
//    browser.loadURL("http://maps.google.com");
    }

    private ResponseEntity<Brewery> getBreweryLoc(long id) {
        Optional<Brewery> o = breweryService.findOne(id);
        Link link = Link.of("https://www.google.com/maps/place/52.6515619,-8.6651593");
        o.get().add(link);
        return ResponseEntity.ok(o.get());
    }

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

    //get all beers by the brewery
    @GetMapping(value = "/beers/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<Beer> getBeersByBrewery(@PathVariable long id) {
        Optional<Brewery> o = breweryService.findOne(id);
        List<Beer> beerListBrewery = new ArrayList();

        List<Beer> beerList = beerService.findAll();
        for (final Beer b : beerList) {
            if (b.getBrewery_id() == id) {
                beerListBrewery.add(b);
            }
        }
        Link selfLink = linkTo(methodOn(BreweryController.class)
                .getAll()).withRel("allBreweries");
        o.get().add(selfLink);
        CollectionModel<Beer> result = CollectionModel.of(beerListBrewery);
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

    //    @GetMapping(value = "/map/{id}", produces = MediaTypes.HAL_JSON_VALUE)
//    public ResponseEntity<Brewery> getLocation(@PathVariable long id) {
//        Optional<Brewery> o = breweryService.findOne(id);
//
//        String map = "map";
//        Link link = Link.of("https://www.google.com/maps/place/52.6515619,-8.6651593");
//        o.get().add(link);
//        return ResponseEntity.ok(o.get());
//
//    }
//    @GetMapping(value = "/loc/{id}")
//    public void getLoc(@PathVariable long id) {
//        //Optional<Brewery> o = breweryService.findOne(id);
//
//        JFrame jFrame = new JFrame("Hello World Swing Example");
//        jFrame.setLayout(new FlowLayout());
//        jFrame.setSize(500, 360);
//        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//
//        JLabel label = new JLabel("Hello World Swing");
//        Border border = BorderFactory.createLineBorder(Color.BLACK);
//        label.setBorder(border);
//        label.setPreferredSize(new Dimension(150, 100));
//
//        label.setText("Hello World Swing");
//        label.setHorizontalAlignment(JLabel.CENTER);
//        label.setVerticalAlignment(JLabel.CENTER);
//
//        jFrame.add(label);
//        jFrame.setVisible(true);
//
//    }
//    @GetMapping(value = "/loc/{id}")
//    public void getLoc(@PathVariable long id) {
//        //Optional<Brewery> o = breweryService.findOne(id);
//
//                JEditorPane editor = new JEditorPane();
//        editor.setEditable(false);   
//        try {
//            editor.setPage("http://www.java2s.com/Code/Java/Swing-JFC/CreateasimplebrowserinSwing.htm");
//        }catch (IOException e) {
//            editor.setContentType("https://www.google.com/");
//            editor.setText("Page could not load");
//        }
// 
//        JScrollPane scrollPane = new JScrollPane(editor);     
//        JFrame f = new JFrame("Display example.com web page");
//        f.getContentPane().add(scrollPane);
//        f.setSize(700,400);
//        f.setVisible(true);
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//    }
}
