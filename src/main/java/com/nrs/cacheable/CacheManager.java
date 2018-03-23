/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nrs.cacheable;

import com.nrs.cacheable.exceptions.NonCacheableClassException;
import com.nrs.cacheable.exceptions.NonCacheableException;
import com.nrs.cacheable.exceptions.NonCacheableMethodException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Using proxy to intercept method calls.
 * 
 * @author root
 */
public class CacheManager<T> implements InvocationHandler {
    private final T target;
    private final HashMap<String, Method> cacheable = new HashMap<>();
    private final CacheStrategy strategy;

    public CacheManager(T target, CacheStrategy strategy) {
        this.target = target;
        this.strategy = strategy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (compareMethods(cacheable.get(method.getName()), method)) {
                String name = method.toString();
                result =  strategy.getValue(name, args);
                if(result == null){
                    result = method.invoke(target, args);
                    strategy.putValue(method.toString(), result);
                }
                return result;
        } else {
            
            result = method.invoke(target, args);
            strategy.putValue(method.toString(), result);
            return result;
        }
    }

    /**
     * Method allows to register your object as cacheable.
     *
     * @return proxy-object
     * @throws NonCacheableException
     */
    public T register() throws NonCacheableException {
        Class clazz = target.getClass();
        List<Method> common = this.getCommonMethods(clazz);
        //common ancestors methods with real type
        for (Method method : common) {
            if (checkOnAnnotation(method)) {
                cacheable.put(method.getName(), method);
            }
        }
        
        for (Method m : clazz.getMethods()) {
            System.out.println(m.getName());
            if (checkOnAnnotation(m) && m.getDeclaringClass().equals(clazz)) { //check if this method declared in current class, not in ancestor's class
                if(!cacheable.containsKey(m.getName())){
                    cacheable.put(m.getName(), m); 
                }
            }
        }
        
        if (cacheable.isEmpty()) {
            throw new NonCacheableClassException("At least one method must be annotated as Cacheable");
        }
    
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    /**
     * Used to compare
     *
     * @return
     */
    private final List<Method> getCommonMethods(Class clazz) {
        List<Method> common = new ArrayList<>();
        for (Class c : clazz.getInterfaces()) {
            for (Method a : c.getMethods()) {
                for (Method m : clazz.getMethods()) {
                    if (compareMethods(m, a)) {
                        common.add(a);
                    }
                }
            }

        }
        return common;
    }
    /**
     * Check if annotation present at given Method. 
     * @param method - method to check
     * @return true if annotation present
     * @throws NonCacheableMethodException if method has return type = Void
     */
    private boolean checkOnAnnotation(final Method method) throws NonCacheableMethodException {
        if (method.getAnnotation(Cacheable.class) != null) {
            if (method.getReturnType().equals(Void.TYPE)) {
                throw new NonCacheableMethodException("Method return type must be non-void");
            }
            return true;
        }else
        return false;
    }
    /**
     * Compares methods by their names, parameters count and types
     * @param first
     * @param second
     * @return true if equal
     */
    private boolean compareMethods(final Method first, final Method second) {
        return first.getParameterCount() == second.getParameterCount() && first.getName().equals(second.getName())
                && Arrays.equals(first.getParameterTypes(), second.getParameterTypes());
    }
}
