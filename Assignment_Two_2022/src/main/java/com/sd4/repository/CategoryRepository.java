/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sd4.repository;

import com.sd4.model.Category;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author paw
 */
public interface CategoryRepository extends CrudRepository<Category, Long>{
    
}
