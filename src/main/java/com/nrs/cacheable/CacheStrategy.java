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
public interface CacheStrategy {
    
     public Object getValue(final String methodName, Object... args);
     public void putValue(final String key, final Object value);
    
}
