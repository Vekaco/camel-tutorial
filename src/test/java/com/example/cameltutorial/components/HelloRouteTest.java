package com.example.cameltutorial.components;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HelloRouteTest {
    @Autowired
    private ProducerTemplate producerTemplate;

    @Test
    public void testMocksAreValid() {
        System.out.println("Sending 1");
        producerTemplate.sendBody("direct:greeting", "Team");

        System.out.println("Sending 2");
        producerTemplate.sendBody("direct:greeting", "Me");
    }
}
