package com.example.cameltutorial.components.errorhandler;


import static com.example.cameltutorial.components.errorhandler.CommonErrorHandlerRoute.COUNTER;

public class HelloBean {

    public HelloBean() {
        System.out.println("HelloBean Constructor");
    }
    public void callGood() {
        System.out.println("Good call for " + COUNTER.get());
    }

    public void callBad() {
        System.out.println("Bad Call for " + COUNTER.get());
        throw new RuntimeException("Exception for " + COUNTER.get());
    }
}
