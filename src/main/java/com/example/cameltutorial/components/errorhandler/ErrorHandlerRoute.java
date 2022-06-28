package com.example.cameltutorial.components.errorhandler;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.cameltutorial.components.errorhandler.CommonErrorHandlerRoute.COUNTER;

@Component
public class ErrorHandlerRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        //defaultErrorHandler, deadLetterChannel, transactionErrorHandler,
        // NoErrorHandler, LoggingErrorHandler
        errorHandler(
                deadLetterChannel("direct:exceptionHandler")
                        .maximumRedeliveries(2));

        from("timer:time?period=1000").process(
                exchange -> exchange.getIn().setBody(new Date())
        ).choice()
                .when(predicate -> COUNTER.getAndIncrement() % 2 == 0)
                .bean(HelloBean.class, "callBad")
                .otherwise()
                .bean(HelloBean.class, "callGood")
                .end()
                .log(LoggingLevel.INFO, ">> ${header.firedTime} >> ${body}")
                .to("log:reply");
    }
}
