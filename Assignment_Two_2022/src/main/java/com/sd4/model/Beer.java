/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Beer  extends RepresentationModel<Beer> implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long brewery_id;
    
    @NotBlank(message="Beer Name cannot be empty")
    private String name;
    private Integer cat_id;
    private Integer style_id;
    private Double abv;
    private Double ibu;
    private Double srm;
    
    @Lob 
    private String description;
    private Integer add_user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date last_mod;

    private String image;
    
    @NotNull(message = "Buy Price cannot be empty")
    private Double buy_price;
    
    @NotNull(message = "Sell Price cannot be empty")
    private Double sell_price;

}
