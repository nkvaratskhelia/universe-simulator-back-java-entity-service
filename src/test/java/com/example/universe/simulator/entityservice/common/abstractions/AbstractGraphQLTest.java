package com.example.universe.simulator.entityservice.common.abstractions;

import com.example.universe.simulator.common.test.AbstractTest;
import com.example.universe.simulator.entityservice.mappers.PageInputMapper;
import com.example.universe.simulator.entityservice.mappers.PageInputMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

@AbstractTest
@Import(PageInputMapperImpl.class)
public abstract class AbstractGraphQLTest {

    @Autowired
    protected GraphQlTester graphQlTester;

    @SpyBean
    protected PageInputMapper pageInputMapper;
}
