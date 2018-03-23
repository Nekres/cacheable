/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nrs.cacheable;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class Service implements Iservice{
    
    
    @Override
    public String getValue(String val) {
        System.out.println("Simulating long process...");
       simulateLongExecution();
        System.out.println("Simulation ended");
       return val + " : success";
    }
    @Cacheable(key = "#getValue")
    public boolean simulateLongExecution(){
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
}
