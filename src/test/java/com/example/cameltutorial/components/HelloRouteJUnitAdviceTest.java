package com.example.cameltutorial.components;

import com.example.cameltutorial.components.choice.HelloRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static org.apache.camel.builder.AdviceWith.adviceWith;

public class HelloRouteJUnitAdviceTest extends CamelTestSupport {

    //set to return true if you want to user advice inject
    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new HelloRoute();
    }

    @Test
    public void testMockEndpoints() throws Exception {
        RouteDefinition route = context.getRouteDefinition("greeting");
        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().to("mock:finishGreeting");
            }
        });

        context.start();

        MockEndpoint mockEndpoint = getMockEndpoint("mock:finishGreeting");
        mockEndpoint.expectedMessageCount(1);

        template.sendBody("direct:greeting","Me");

        mockEndpoint.assertIsSatisfied();
    }
}
