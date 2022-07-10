package com.example.universe.simulator.entityservice.unit.controllers.graphql;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.config.GraphQLConfig;
import com.example.universe.simulator.entityservice.controllers.graphql.GalaxyGraphQLController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.GalaxyDtoValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@GraphQlTest(GalaxyGraphQLController.class)
@Import({GraphQLConfig.class})
public class GalaxyGraphQLControllerTest extends AbstractWebMvcTest {

//    @Autowired
//    private GraphQlTester graphQlTester;

    @MockBean
    private GalaxyService service;

    @MockBean
    private GalaxyDtoValidator validator;

    @MockBean
    private GalaxySpecificationBuilder specificationBuilder;

//    @Test
//    void testGetList() throws Exception {
//        // given
//        List<Galaxy> entityList = List.of(
//                TestUtils.buildGalaxy()
//        );
//
//        GalaxyFilter filter = TestUtils.buildGalaxyFilter();
//        Pageable pageable = TestUtils.getDefaultPageable();
//        Page<Galaxy> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
//        Page<GalaxyDto> dtoPage = entityPage.map(item -> modelMapper.map(item, GalaxyDto.class));
//
//        given(service.getList(any(), any())).willReturn(entityPage);
//        // when
//        MockHttpServletResponse response = performRequest(get("/galaxies")
//                .param("name", filter.getName())
//        );
//        // then
//        verifySuccessfulResponse(response, dtoPage);
//        then(specificationBuilder).should().build(filter);
//        then(service).should().getList(null, pageable);
//    }

    @Test
    void testGetAllProductsQueryReturnsAllProducts() {
        // language=GraphQL
//
//        List<Galaxy> entityList = List.of(
//                TestUtils.buildGalaxy()
//        );
//
//        String document = """
//            query {
//              getGalaxies {
//                id
//                name
//                version
//              }
//            }
//        """;
//
//        GalaxyFilter filter = TestUtils.buildGalaxyFilter();
//        Pageable pageable = TestUtils.getDefaultPageable();
//        Page<Galaxy> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
//        Page<GalaxyDto> dtoPage = entityPage.map(item -> modelMapper.map(item, GalaxyDto.class));
//
//        when(service.getList(any(), any())).thenReturn(entityPage);
//
//        graphQlTester.document(document)
//                .execute()
//                .path("getGalaxies")
//                .entityList(GalaxyDto.class)
//                .hasSize(1);
    }

}
