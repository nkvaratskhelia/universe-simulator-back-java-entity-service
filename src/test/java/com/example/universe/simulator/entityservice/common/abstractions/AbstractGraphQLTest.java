package com.example.universe.simulator.entityservice.common.abstractions;

import com.example.universe.simulator.common.test.AbstractTest;
import com.example.universe.simulator.entityservice.config.GraphQLConfig;
import com.example.universe.simulator.entityservice.mappers.PageInputMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

@AbstractTest
@Import({GraphQLConfig.class, PageInputMapperImpl.class})
public abstract class AbstractGraphQLTest {

    @Autowired
    protected GraphQlTester graphQlTester;
}
