/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nrs.cacheable;

/**
 *
 * @author root
 */
public class Service implements Iservice{
    
       
    @Override
    public String getValue(String val) {
       return "value";
    }
    
}
