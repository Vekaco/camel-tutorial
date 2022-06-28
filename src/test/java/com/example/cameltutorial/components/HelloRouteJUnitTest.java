package com.example.cameltutorial.components;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;

@MockEndpoints()
public class HelloRouteJUnitTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        //return new HelloRoute();

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:greeting")//.log(LoggingLevel.ERROR, "${body}")
                        .to("mock:greetingResult");
            }
        };
    }


    @Test
    public void testMockAreValid() throws InterruptedException {
//        MockEndpoint mockEndpoint = getMockEndpoint("mock:greetingResult");
//        mockEndpoint.expectedMessageCount(2);
        System.out.println("Sending 1");
        template.sendBody("direct:greeting", "Team");

        System.out.println("Sending 2");
        template.sendBody("direct:greeting", "Me");
        //mockEndpoint.assertIsSatisfied();
    }
}
