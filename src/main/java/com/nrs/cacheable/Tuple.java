/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nrs.cacheable;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author root
 */
public class Tuple {
    
    private final String methodName;
    private final List<Object> params;
    private final Object result;
    private final int paramsCount;

    public Tuple(String methodName, List<Object> params, Object result, int paramsCount) {
        this.methodName = methodName;
        this.params = params;
        this.result = result;
        this.paramsCount = paramsCount;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.methodName);
        hash = 47 * hash + Objects.hashCode(this.params);
        hash = 47 * hash + Objects.hashCode(this.result);
        hash = 47 * hash + this.paramsCount;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tuple other = (Tuple) obj;
        if (this.paramsCount != other.paramsCount) {
            return false;
        }
        if (!Objects.equals(this.methodName, other.methodName)) {
            return false;
        }
        if (!Objects.equals(this.params, other.params)) {
            return false;
        }
        if (!Objects.equals(this.result, other.result)) {
            return false;
        }
        return true;
    }

    
    
}
