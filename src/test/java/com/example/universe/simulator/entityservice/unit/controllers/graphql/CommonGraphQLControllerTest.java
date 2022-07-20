package com.example.universe.simulator.entityservice.unit.controllers.graphql;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractGraphQLTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.graphql.GalaxyGraphQLController;
import com.example.universe.simulator.entityservice.inputs.PageInput;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapperImpl;
import com.example.universe.simulator.entityservice.mappers.PageInputMapper;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

// Common graphql controller cases are tested using GalaxyGraphQLController.
@GraphQlTest(GalaxyGraphQLController.class)
@Import(GalaxyMapperImpl.class)
class CommonGraphQLControllerTest extends AbstractGraphQLTest {

    @MockBean
    private GalaxyService galaxyService;

    @MockBean
    private GalaxySpecificationBuilder galaxySpecificationBuilder;

    @SpyBean
    private PageInputMapper pageInputMapper;

    @Test
    void testPaging_defaultPageInput() {
        // given
        // language=GraphQL
        var document = """
            {
              getGalaxies {
                id
              }
            }
            """;

        PageInput pageInput = TestUtils.buildDefaultPageInput();
        Pageable pageable = TestUtils.buildDefaultPageable();

        given(galaxyService.getList(any(), any())).willReturn(Page.empty());
        // when
        graphQlTester
            .document(document)
            .execute();
        // then
        then(pageInputMapper).should().toPageable(pageInput);
        then(galaxyService).should().getList(null, pageable);
    }

    @Test
    void testPaging_defaultSortDirection() {
        // given
        // language=GraphQL
        var document = """
            query getGalaxies($pageInput: PageInput) {
              getGalaxies(pageInput: $pageInput) {
                id
              }
            }
            """;

        PageInput pageInput = TestUtils.buildDefaultSortDirectionPageInput();
        Map<String, Object> pageInputMap = TestUtils.buildDefaultSortDirectionPageInputMap();

        Pageable pageable = TestUtils.buildDefaultSortDirectionPageable();

        given(galaxyService.getList(any(), any())).willReturn(Page.empty());
        // when
        graphQlTester
            .document(document)
            .variable("pageInput", pageInputMap)
            .execute();
        // then
        then(pageInputMapper).should().toPageable(pageInput);
        then(galaxyService).should().getList(null, pageable);
    }

    @Test
    void testPaging() {
        // given
        // language=GraphQL
        var document = """
            query getGalaxies($pageInput: PageInput) {
              getGalaxies(pageInput: $pageInput) {
                id
              }
            }
            """;

        PageInput pageInput = TestUtils.buildSpaceEntityPageInput();
        Map<String, Object> pageInputMap = TestUtils.buildSpaceEntityPageInputMap();

        Pageable pageable = TestUtils.buildSpaceEntityPageable();

        given(galaxyService.getList(any(), any())).willReturn(Page.empty());
        // when
        graphQlTester
            .document(document)
            .variable("pageInput", pageInputMap)
            .execute();
        // then
        then(pageInputMapper).should().toPageable(pageInput);
        then(galaxyService).should().getList(null, pageable);
    }
}
