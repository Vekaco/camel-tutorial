package com.example.cameltutorial.components;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

@Configuration
public class JMSConfiguration {
    @Bean
    public ConnectionFactory connectionFactory() {
        return  new ActiveMQConnectionFactory("tcp://localhost:61616");
    }
}
