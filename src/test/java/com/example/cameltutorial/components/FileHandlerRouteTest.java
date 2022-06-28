package com.example.cameltutorial.components;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class FileHandlerRouteTest {
    @Autowired
    ProducerTemplate producerTemplate;

    @Test
    public void testCamelFileRoute() {
        System.out.println("Sending request to append to existing file...");
        //producerTemplate.sendBody("direct:appendToFile", new Date() + "\n");
        producerTemplate.sendBody("direct:appendToFile", new Date());
        System.out.println("Sent request to append to existing file...");
    }
}
