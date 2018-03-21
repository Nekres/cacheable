/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nrs.cacheable;

import com.nrs.cacheable.exceptions.NonCacheableClassException;
import com.nrs.cacheable.exceptions.NonCacheableException;
import com.nrs.cacheable.exceptions.NonCacheableMethodException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Using proxy to intercept method calls.
 * @author root
 */
public class CacheManager<T> implements InvocationHandler{
    
    private final T target;
    private final HashMap<String,Method> cacheable = new HashMap<>();

    public CacheManager(T target) {
        this.target = target;
    }
    
    
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String key = null;
        Cacheable annotation = method.getAnnotation(Cacheable.class);
        if(annotation != null){
            key = annotation.key();
        }
        if(cacheable.keySet().contains(key)){
            System.out.println("contains");
            return method.invoke(target,args);
        }else{
            System.out.println(cacheable.get(0).getName());
            System.out.println("not contains");
        }
        return null;
    }
    /**
     * Method allows to register your object as cacheable.
     * @return proxy-object
     * @throws NonCacheableException 
     */
    public T register() throws NonCacheableException{
        Class clazz = target.getClass();
        List<Method> common = this.getCommonMethods(clazz);
        
        for(Method method : common){
         
            if(method.getAnnotation(Cacheable.class) != null){
                if(method.getReturnType().equals(Void.TYPE)){
                    throw new NonCacheableMethodException("Method return type must be non-void");
                }
                
                cacheable.put(method.getAnnotation(Cacheable.class).key(), method);
                System.out.println(method.getAnnotation(Cacheable.class).key());
            }
        }
        
        if(cacheable.isEmpty()){
            throw new NonCacheableClassException("At least one class method must be annotated as Cacheable");
        }
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }
    /**
     * Used to compare
     * @return 
     */
    private final List<Method> getCommonMethods(Class clazz) {
        List<Method> common = new ArrayList<>();
        for (Class c : clazz.getInterfaces()) {
            for (Method a : c.getMethods()) {
                for (Method m : clazz.getMethods()) {
                    if (m.getParameterCount() == a.getParameterCount() && m.getName().equals(a.getName())) {
                        common.add(a);
                    }
                }
            }

        }
        return common;
    }

}
