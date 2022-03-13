/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.service;

import com.sd4.model.Breweries_Geocode;
import com.sd4.repository.Breweries_GeocodeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author paw
 */
@Service
public class Breweries_GeocodeService {
    @Autowired
    private Breweries_GeocodeRepository breweries_GeocodeRepo;

    public Optional<Breweries_Geocode> findOne(Long id) {
        return breweries_GeocodeRepo.findById(id);
    }

    public List<Breweries_Geocode> findAll() {
        return (List<Breweries_Geocode>) breweries_GeocodeRepo.findAll();
    }

    public long count() {
        return breweries_GeocodeRepo.count();
    }

    public void deleteByID(long breweries_GeocodeID) {
        breweries_GeocodeRepo.deleteById(breweries_GeocodeID);
    }

    public void saveBreweries_Geocode(Breweries_Geocode a) {
        breweries_GeocodeRepo.save(a);
    }
}

