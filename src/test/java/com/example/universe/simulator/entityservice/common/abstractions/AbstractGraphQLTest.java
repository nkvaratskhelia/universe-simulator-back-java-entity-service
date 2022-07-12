package com.example.universe.simulator.entityservice.common.abstractions;

import com.example.universe.simulator.common.test.AbstractTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.GraphQlTester;

@AbstractTest
public abstract class AbstractGraphQLTest {

    @Autowired
    protected GraphQlTester graphQlTester;

}
