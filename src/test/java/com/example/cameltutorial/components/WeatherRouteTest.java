package com.example.cameltutorial.components;

import com.example.cameltutorial.CamelTutorialApplication;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = CamelTutorialApplication.class, properties = "com.example.rabbitmq.enable=true")
@CamelSpringBootTest
public class WeatherRouteTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void sendAndReceiveMessage() {
        rabbitTemplate.send("amq.direct", "weather", message());
        Message response = rabbitTemplate.receive("weather-event", 1000);
        assertNotNull(response, "Response must be non-null");

        String body = new String(response.getBody());

        assertTrue(body.contains("id"), "Id must be defined");
        assertTrue(body.contains("receivedTime"), "receivedTime must be defined");
    }

    private Message message() {

        return MessageBuilder
                .withBody("{ \"city\": \"London\", \"temp\": \"20\", \"unit\": \"C\"}"
                        .getBytes(StandardCharsets.UTF_8))
                .build();
    }


}
