/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nrs.cacheable;

import com.nrs.cacheable.exceptions.NonCacheableException;

/**
 *  
 * @author root
 */
public class Main {
    
    public static void main(String[] args) throws NonCacheableException {
        CacheManager<Iservice> manager = new CacheManager<>(new Service(), new LeastRecentlyUsed(20));
        Iservice t = manager.register();
        final String value = t.getValue("Some string value");
        System.out.println(value);
    }
    
}
