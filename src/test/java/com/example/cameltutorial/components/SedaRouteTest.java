package com.example.cameltutorial.components;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootTest
public class SedaRouteTest {

    @Autowired
    private ProducerTemplate template;


    @Test
    public void testMocksAreValid() {
        template.sendBody("direct:ticker", "Hello");

        try {
            SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
