package com.example.cameltutorial.components.rabbitmq;

import com.example.cameltutorial.components.CamelConfiguration;
import com.example.cameltutorial.dto.WeatherDto;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.DefaultMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@ConditionalOnProperty(name = "com.example.rabbitmq.enabled", havingValue = "true")
public class WeatherRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        //rabbitmq:{exchange_name}?{options}
        //rabbitmq:{exchange_name}?queue=xxx&routingKey=xxx&autoDelete=false&hostName=xxx&portName=xxx&userName=xxx&password=xxx
        //rabbitmq:{exchange_name}?queue=xxx&routingKey=xxx&autoDelete=false&connectionFactory=xxx
        //rabbitmq:{exchange_name}?queue=xxx&routingKey=xxx&autoDelete=false (default connection will find rabbitConnectionFactory)

        fromF(CamelConfiguration.RABBIT_URI, "weather", "weather")
                .log(LoggingLevel.ERROR, "Before enrichment: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, WeatherDto.class)
                .process(this::enrichWeatherDto)
                .log(LoggingLevel.ERROR, "After enrichment: ${body}")
                .marshal().json(JsonLibrary.Jackson, WeatherDto.class)
                .toF(CamelConfiguration.RABBIT_URI, "weather-events", "weather-event")
                .to("file://temp?fileName=weather-event.jsonl&fileExist=Append");
    }

    private void enrichWeatherDto(Exchange exchange) {
        WeatherDto dto = exchange.getMessage().getBody(WeatherDto.class);
        dto.setReceivedTime(new Date().toString());

        //sent message to another queue
        Message message = new DefaultMessage(exchange);
        message.setBody(dto);
        exchange.setMessage(message);
    }
}
