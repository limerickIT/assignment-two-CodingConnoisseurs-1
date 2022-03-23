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
//import java.awt.PageAttributes.MediaType;
import org.springframework.http.MediaType;
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

import com.itextpdf.text.Image;
//import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sd4.model.Category;
import com.sd4.model.Style;
import com.sd4.service.CategoryService;
import com.sd4.service.StyleService;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.view.RedirectView;

//Pagination
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

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

    @Autowired
    private PagedResourcesAssembler<Beer> pagedResourcesAssembler;

    @GetMapping(value = "/beerImage/{beerId}/{imageSize}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<BufferedImage> getBeerImage(@PathVariable long beerId, @PathVariable String imageSize) throws IOException {
        Optional<Beer> o = beerService.findOne(beerId);
        String path = "src/main/resources/static/assets/images/" + imageSize + "/" + beerId + ".jpg";
        BufferedImage image = ImageIO.read(new File(path));
        return ResponseEntity.ok(image);

    }

    //REMOVE SIZE ATTRIBUTE?
    @GetMapping(value = "/pdf/{beerId}/{imageSize}", produces = MediaType.APPLICATION_PDF_VALUE)
    public void getPdf(@PathVariable long beerId, @PathVariable String imageSize) throws FileNotFoundException, DocumentException {
        Optional<Beer> o = beerService.findOne(beerId);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/Beer Poster.pdf"));
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
            String image = o.get().getImage();

            Paragraph chunk1 = new Paragraph("Beer Name: " + beerName, font);
            Paragraph chunk2 = new Paragraph("ABV: " + abv, font);
            Paragraph chunk3 = new Paragraph("Description: " + desc, font);
            Paragraph chunk4 = new Paragraph("Sell Price: " + sellPrice, font);
            Paragraph chunk5 = new Paragraph("Brewery Name: " + breweryName, font);
            Paragraph chunk6 = new Paragraph("Brewery Website: " + website, font);
            Paragraph chunk7 = new Paragraph("Category: " + categoryName, font);
            Paragraph chunk8 = new Paragraph("Style: " + styleName, font);

            String imFile = "src/main/resources/static/assets/images/" + imageSize + "/" + beerId + ".jpg";

            document.add(chunk1);
            document.add(chunk2);
            document.add(chunk3);
            document.add(chunk4);
            document.add(chunk5);
            document.add(chunk6);
            document.add(chunk7);
            document.add(chunk8);
            Image img;
            try {
                img = Image.getInstance(imFile);
                document.add(img);
            } catch (BadElementException ex) {
                Logger.getLogger(BeerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BeerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Chunk chunk = new Chunk("No beer with that id", font);
            document.add(chunk);
        }
        document.close();
    }

    @RequestMapping(value = "/zip", produces = "application/zip")
    public void zipFiles() throws FileNotFoundException, IOException {
        String sourceFile = "src/main/resources/static/assets/images";
        FileOutputStream fos = new FileOutputStream("src/main/resources/beerImages.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    @GetMapping(value = "/{beerId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Beer> getOneBeer(@PathVariable long beerId) {
        Optional<Beer> o = beerService.findOne(beerId);

        if (!o.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            Link selfLink = linkTo(methodOn(BeerController.class)
                    .getAllBeers()).withRel("allBeers");
            o.get().add(selfLink);
            return ResponseEntity.ok(o.get());
        }
    }

    //with Pagination DOES NOT WORK!!!!
    @GetMapping(value = "/allBeersPagination", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<Beer> getAllBeersPagitantion(Pageable pageable) {
        Page<Beer> beerList = (Page<Beer>) beerService.findAll();
        for (final Beer b : beerList) {
            long id = b.getId();
            Link selfLink = linkTo(BeerController.class).slash(id).withSelfRel();
            b.add(selfLink);
            Link beerDetails = linkTo(methodOn(BeerController.class)
                    .getBeerDetails(id)).withRel("beerDetails");
            b.add(beerDetails);
        }

        Link link = linkTo(BeerController.class).withSelfRel();
        Page result1 = (Page) CollectionModel.of(beerList, link);
        CollectionModel<Beer> result = CollectionModel.of(beerList, link);
        return result;
    }

    @GetMapping(value = "/allBeers", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<Beer> getAllBeers() {
        //add check if empty
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
    @GetMapping(value = "/beerDetails/{beerId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> getBeerDetails(@PathVariable long beerId) {
        Optional<Beer> o = beerService.findOne(beerId);
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



}
