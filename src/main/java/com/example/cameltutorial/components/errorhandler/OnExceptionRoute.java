package com.example.cameltutorial.components.errorhandler;

import com.example.cameltutorial.components.errorhandler.HelloBean;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.cameltutorial.components.errorhandler.CommonErrorHandlerRoute.COUNTER;

@Component
public class OnExceptionRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        onException(Exception.class)
                .log(LoggingLevel.ERROR, ">> ${exception}")
                .handled(true)//the exception is handled and will not transfer to the next route
                .to("direct:exceptionHandler");

//        onException(MyOwnException.class)
//                .log(LoggingLevel.ERROR, ">> ${exception}")
//                .handled(true)
//                .maximumRedeliveries(3)
//                .to("direct:exceptionHandler");

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
