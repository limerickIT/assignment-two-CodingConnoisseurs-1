/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.sd4.model.Beer;
import com.sd4.model.Brewery;
import com.sd4.service.BeerService;
import com.sd4.service.BreweryService;
import java.awt.PageAttributes.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
//import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
//import static org.sfw.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.ResponseBody;

//pgf imports
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

//DOES NOT EXIST
//import com.itextpdf.io.image.ImageData; 
//import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sd4.model.Category;
import com.sd4.model.Style;
import com.sd4.service.CategoryService;
import com.sd4.service.StyleService;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author paw
 */
@RestController
@RequestMapping("/beers")
public class BeerController {

    @Autowired
    private BeerService beerService;

    @Autowired
    private BreweryService breweryService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StyleService styleService;

    @GetMapping(value = "/pdf/{id}", produces = org.springframework.http.MediaType.APPLICATION_PDF_VALUE)
    public void getPdf(@PathVariable long id) throws FileNotFoundException, DocumentException {
        Optional<Beer> o = beerService.findOne(id);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Beer Poster.pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

        if (o.isPresent()) {
            long breweryId = o.get().getBrewery_id();
            Optional<Brewery> brewery = breweryService.findOne(breweryId);

            long catId = o.get().getCat_id();
            Optional<Category> cat = categoryService.findOne(catId);

            long styleId = o.get().getStyle_id();
            Optional<Style> style = styleService.findOne(Long.valueOf(styleId));

            String beerName = o.get().getName();
            double abv = o.get().getAbv();
            String desc = o.get().getDescription();
            double sellPrice = o.get().getSell_price();
            String breweryName = brewery.get().getName();
            String website = brewery.get().getWebsite();
            String categoryName = cat.get().getCat_name();
            String styleName = style.get().getStyle_name();

            Paragraph chunk1 = new Paragraph("Beer Name: " + beerName, font);
            Paragraph chunk2 = new Paragraph("ABV: " + abv, font);
            Paragraph chunk3 = new Paragraph("Description: " + desc, font);
            Paragraph chunk4 = new Paragraph("Sell Price: " + sellPrice, font);
            Paragraph chunk5 = new Paragraph("Brewery Name: " + breweryName, font);
            Paragraph chunk6 = new Paragraph("Brewery Website: " + website, font);
            Paragraph chunk7 = new Paragraph("Category: " + categoryName, font);
            Paragraph chunk8 = new Paragraph("Style: " + styleName, font);

            document.add(chunk1);
            document.add(chunk2);
            document.add(chunk3);
            document.add(chunk4);
            document.add(chunk5);
            document.add(chunk6);
            document.add(chunk7);
            document.add(chunk8);

            //ADD IMAGE
        } else {
            Chunk chunk = new Chunk("No beer with that id", font);
            document.add(chunk);
        }
        document.close();

        //document.addTitle("Beer poster");
//        String beerName1 = o.get().getName();
//        Paragraph chunk11 = new Paragraph("Beer Name: " + beerName1, font);
//        document.add(chunk11);
//
//        long breweryId1 = o.get().getBrewery_id();
//        Optional<Brewery> brewery1 = breweryService.findOne(breweryId1);
//        String breweryName1 = brewery1.get().getName();
//        Paragraph chunk14 = new Paragraph("Brewery Name: " + breweryName1, font);
//        document.add(chunk14);
//
//        long catId = o.get().getCat_id();
//        Optional<Category> cat = categoryService.findOne(catId);
//
//        String categoryName = cat.get().getCat_name();
//        Paragraph chunk7 = new Paragraph("Category: " + categoryName, font);
//        document.add(chunk7);
//
//        long styleId = o.get().getStyle_id();
//
//        if (!styleService.findOne(styleId).isPresent()) {
////            Optional<Style> style = styleService.findOne(styleId);
////            String styleName = style.get().getStyle_name();
////            Paragraph chunk8 = new Paragraph("Style: " + styleName, font);
////            document.add(chunk8);
//        } else {
//            Paragraph chunk16 = new Paragraph(" Style: not present", font);
//            document.add(chunk16);
//        }
//
////        Creating an ImageData object       
////        String imFile = "C:/itextExamples/logo.jpg";
////        ImageData data = ImageDataFactory.create(imFile);
////
////        // Creating an Image object        
////        Image image = new Image(data);
////
////        // Adding image to the document       
////        document.add(image);
//        if (!o.isPresent()) {
//            Chunk chunk16 = new Chunk("o is present", font);
//            document.add(chunk16);
//        } else {
//            Chunk chunk16 = new Chunk("o is NOT present", font);
//            document.add(chunk16);
//        }
    }

    @GetMapping(value = "/image", produces = org.springframework.http.MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage() throws IOException {

        String path = new ClassPathResource("/src/main/resources/static.assets.images.thumbs/1.jpg").toString();
        InputStream in = getClass()
                .getResourceAsStream("/src/main/resources/static.assets.images.thumbs/1.jpg");
        return IOUtils.toByteArray(in);

    }

//    @GetMapping(value = "/image", produces = MediaTypes.HAL_JSON_VALUE)
//    public ResponseEntity<Beer> getImage() throws IOException {
//        long id = 1;
//        Optional<Beer> o = beerService.findOne(id);
////        String path = "src/main/resources/static.assets.images.large/";
////        if(type == "thumb"){
////            path = "src/main/resources/static.assets.images.thumbs/";
////        }
//String path = new ClassPathResource("/src/main/resources/static.assets.images.thumbs/1.jpg").toString();
//        String resource = new ClassPathResource("1.jpg").toString();
//        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//        //String path = Thread.currentThread().getContextClassLoader().toString();
//         System.out.println(path);
//        InputStream in = classloader.getResourceAsStream("1.jpg");
//    return ResponseEntity.ok(o.get());
//
//
//    }
    //@GetMapping(value = "/hateoas/{id}", produces = { "application/hal+json" })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Beer> getOneWithHATEOAS(@PathVariable long id) {
        Optional<Beer> o = beerService.findOne(id);

        if (!o.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            Link selfLink = linkTo(methodOn(BeerController.class)
                    .getAll()).withRel("allBeers");
            o.get().add(selfLink);
            return ResponseEntity.ok(o.get());
        }
    }

    @GetMapping(value = "/all", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<Beer> getAll() {
        //ADD RESPONSE ENTITY? WHEN LIST IS EMPTY
        List<Beer> beerList = beerService.findAll();
        for (final Beer b : beerList) {
            long id = b.getId();
            Link selfLink = linkTo(BeerController.class).slash(id).withSelfRel();
            b.add(selfLink);
            Link beerDetails = linkTo(methodOn(BeerController.class)
                    .getBeerDetails(id)).withRel("beerDetails");
            b.add(beerDetails);
        }

        Link link = linkTo(BeerController.class).withSelfRel();
        CollectionModel<Beer> result = CollectionModel.of(beerList, link);
        return result;
    }

    //get beer name, desc and brewery name
    @GetMapping(value = "/details/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> getBeerDetails(@PathVariable long id) {
        Optional<Beer> o = beerService.findOne(id);
        long breweryId = o.get().getBrewery_id();
        Optional<Brewery> brewery = breweryService.findOne(breweryId);
        JSONObject resp = new JSONObject();
        String name = o.get().getName();
        String description = o.get().getDescription();
        String breweryName = brewery.get().getName();

        try {
            resp.put("beer name", name);
            resp.put("description", description);
            resp.put("brewery name", breweryName);
        } catch (JSONException ex) {
            Logger.getLogger(BeerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //String details = "Name: " + o.get().getName() + " Description: " + o.get().getDescription();
        return ResponseEntity.ok(resp.toString());
    }

//    @GetMapping("")
//    public List<Beer> getAll() {
//        return beerService.findAll();
//    }
//    @GetMapping("{id}")
//    public ResponseEntity<Beer> getOne(@PathVariable long id) {
//        Optional<Beer> o = beerService.findOne(id);
//
//        if (!o.isPresent()) {
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        } else {
//            return ResponseEntity.ok(o.get());
//        }
//    }
    @GetMapping("count")
    public long getCount() {
        return beerService.count();
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {
        beerService.deleteByID(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity add(@RequestBody Beer a) {
        beerService.saveBeer(a);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity edit(@RequestBody Beer a) { //the edit method should check if the Author object is already in the DB before attempting to save it.
        beerService.saveBeer(a);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/brewery")
    public List<Brewery> getAllBreweries() {
        return breweryService.findAll();
    }
}
