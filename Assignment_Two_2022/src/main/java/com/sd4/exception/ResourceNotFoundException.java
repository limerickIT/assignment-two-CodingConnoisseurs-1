/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.exception;

/**
 *
 * @author paw
 */
public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ResourceNotFoundException(String msg) {
    super(msg);
  }
}
