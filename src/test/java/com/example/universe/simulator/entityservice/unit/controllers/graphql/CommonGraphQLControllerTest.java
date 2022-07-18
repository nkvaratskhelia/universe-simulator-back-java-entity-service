package com.example.universe.simulator.entityservice.unit.controllers.graphql;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractGraphQLTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.graphql.GalaxyGraphQLController;
import com.example.universe.simulator.entityservice.inputs.PageInput;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapperImpl;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

// Common graphql controller cases are tested using GalaxyGraphQLController.
@GraphQlTest(GalaxyGraphQLController.class)
@Import(GalaxyMapperImpl.class)
class CommonGraphQLControllerTest extends AbstractGraphQLTest {

    @MockBean
    private GalaxyService service;

    @MockBean
    private GalaxySpecificationBuilder specificationBuilder;

    @Test
    void testGetGalaxies_defaultPageInput() {
        // given
        // language=GraphQL
        String document = """
            {
              getGalaxies {
                id
              }
            }
            """;

        PageInput pageInput = TestUtils.buildDefaultPageInput();
        Pageable pageable = TestUtils.buildDefaultPageable();

        given(service.getList(any(), any())).willReturn(Page.empty());
        // when
        graphQlTester
            .document(document)
            .execute();
        // then
        then(pageInputMapper).should().toPageable(pageInput);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGetGalaxies_customPageable_withSorting() {
        // given
        // language=GraphQL
        String document = """
            query getGalaxies($name: String, $pageInput: PageInput) {
              getGalaxies(name: $name, pageInput: $pageInput) {
                id
              }
            }
            """;

        Pageable pageable = TestUtils.buildSpaceEntityPageable();

        given(service.getList(any(), any())).willReturn(Page.empty());
        // when
        graphQlTester
            .document(document)
            .variable("pageInput", TestUtils.buildInputMapForPagingAndSorting(pageable))
            .execute();
        // then
        then(service).should().getList(null, pageable);
    }
}
