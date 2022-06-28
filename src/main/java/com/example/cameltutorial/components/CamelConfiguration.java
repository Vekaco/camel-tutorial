package com.example.cameltutorial.components;

import com.rabbitmq.client.ConnectionFactory;
import io.micrometer.core.instrument.Clock;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.apache.camel.CamelContext;
import org.apache.camel.component.micrometer.MicrometerConstants;
import org.apache.camel.component.micrometer.messagehistory.MicrometerMessageHistoryFactory;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyFactory;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfiguration {
    public static final String RABBIT_URI = "rabbitmq:amq.direct?queue=%s&routingKey=%s&autoDelete=false";

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        return factory;
    }

    @Bean
    public CamelContextConfiguration camelContextConfiguration() {
       return new CamelContextConfiguration() {
           @Override
           public void beforeApplicationStart(CamelContext camelContext) {
               camelContext.addRoutePolicyFactory(new MicrometerRoutePolicyFactory());
               camelContext.setMessageHistoryFactory(new MicrometerMessageHistoryFactory());
           }

           @Override
           public void afterApplicationStart(CamelContext camelContext) {

           }
       };
    }

    @Bean(name = { MicrometerConstants.METRICS_REGISTRY_NAME, "prometheusMeterRegistry" })
    public PrometheusMeterRegistry prometheusMeterRegistry(
            PrometheusConfig prometheusConfig, CollectorRegistry collectorRegistry, Clock clock) {
        return new PrometheusMeterRegistry(prometheusConfig, collectorRegistry, clock);
    }
}
