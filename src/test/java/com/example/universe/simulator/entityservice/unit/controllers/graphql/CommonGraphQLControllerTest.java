package com.example.universe.simulator.entityservice.unit.controllers.graphql;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractGraphQLTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.config.GraphQLConfig;
import com.example.universe.simulator.entityservice.controllers.graphql.GalaxyGraphQLController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapper;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapperImpl;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

// Common graphql controller cases are tested using GalaxyGraphQLController.
@GraphQlTest(GalaxyGraphQLController.class)
@Import({GraphQLConfig.class, GalaxyMapperImpl.class})
class CommonGraphQLControllerTest extends AbstractGraphQLTest {

    @MockBean
    private GalaxyService service;

    @MockBean
    private GalaxySpecificationBuilder specificationBuilder;

    @SpyBean
    private GalaxyMapper galaxyMapper;

    @Test
    void testGetGalaxies_customPageable_withoutSorting() {
        // given
        Galaxy firstEntity = TestUtils.buildGalaxyWithName("name1");
        Galaxy secondEntity = TestUtils.buildGalaxyWithName("name2");
        List<Galaxy> entityList = List.of(firstEntity, secondEntity);

        Pageable pageable = TestUtils.getSpaceEntityPageableWithoutSorting();
        Page<Galaxy> entityPage = new PageImpl<>(entityList, pageable, entityList.size());

        // language=GraphQL
        String document = """
                query getGalaxies($name: String, $pageInput: PageInput) {
                  getGalaxies(name: $name, pageInput: $pageInput) {
                    id, name, version
                  }
                }
            """;

        given(service.getList(any(), any())).willReturn(entityPage);
        // when
        // then
        graphQlTester.document(document)
            .variable("pageInput", TestUtils.buildInputMapForOnlyPaging(pageable))
            .execute()
            .path("getGalaxies")
            .entityList(GalaxyDto.class)
            .containsExactly(galaxyMapper.toDto(firstEntity), galaxyMapper.toDto(secondEntity));
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGetGalaxies_customPageable_withSorting() {
        // given
        Galaxy firstEntity = TestUtils.buildGalaxyWithName("name1");
        Galaxy secondEntity = TestUtils.buildGalaxyWithName("name2");
        List<Galaxy> entityList = List.of(firstEntity, secondEntity);

        Pageable pageable = TestUtils.getSpaceEntityPageable();
        Page<Galaxy> entityPage = new PageImpl<>(entityList, pageable, entityList.size());

        // language=GraphQL
        String document = """
                query getGalaxies($name: String, $pageInput: PageInput) {
                  getGalaxies(name: $name, pageInput: $pageInput) {
                    id, name, version
                  }
                }
            """;

        given(service.getList(any(), any())).willReturn(entityPage);
        // when
        // then
        graphQlTester.document(document)
            .variable("pageInput", TestUtils.buildInputMapForPagingAndSorting(pageable))
            .execute()
            .path("getGalaxies")
            .entityList(GalaxyDto.class)
            .containsExactly(galaxyMapper.toDto(firstEntity), galaxyMapper.toDto(secondEntity));
        then(service).should().getList(null, pageable);
    }
}
