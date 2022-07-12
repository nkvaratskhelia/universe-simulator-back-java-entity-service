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
import com.example.universe.simulator.entityservice.validators.GalaxyDtoValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@GraphQlTest(GalaxyGraphQLController.class)
@Import({GraphQLConfig.class, GalaxyMapperImpl.class})
class CommonGraphQLControllerTest extends AbstractGraphQLTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private GalaxyService service;

    @MockBean
    private GalaxyDtoValidator validator;

    @MockBean
    private GalaxySpecificationBuilder specificationBuilder;

    @SpyBean
    private GalaxyMapper mapper;

    @Test
    void testGetGalaxies_customPageable() throws Exception {
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
            .variable("pageInput", TestUtils.buildInputMapForPaging(pageable))
            .execute()
            .path("getGalaxies")
            .entityList(GalaxyDto.class)
            .containsExactly(mapper.toDto(firstEntity), mapper.toDto(secondEntity));
        then(service).should().getList(null, pageable);
    }
}
