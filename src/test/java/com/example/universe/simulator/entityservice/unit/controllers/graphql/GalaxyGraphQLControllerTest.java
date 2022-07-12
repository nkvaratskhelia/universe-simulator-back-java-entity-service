package com.example.universe.simulator.entityservice.unit.controllers.graphql;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractGraphQLTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.config.GraphQLConfig;
import com.example.universe.simulator.entityservice.controllers.graphql.GalaxyGraphQLController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapper;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapperImpl;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import com.example.universe.simulator.entityservice.validators.GalaxyDtoValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@GraphQlTest(GalaxyGraphQLController.class)
@Import({GraphQLConfig.class, GalaxyMapperImpl.class})
class GalaxyGraphQLControllerTest extends AbstractGraphQLTest {

    @MockBean
    private GalaxyService service;

    @MockBean
    private GalaxyDtoValidator validator;

    @MockBean
    private GalaxySpecificationBuilder specificationBuilder;

    @SpyBean
    private GalaxyMapper mapper;

    @Test
    void testGetGalaxies() {
        //given
        Galaxy entity = TestUtils.buildGalaxy();
        List<Galaxy> entityList = List.of(entity);

        GalaxyFilter filter = TestUtils.buildGalaxyFilter();
        Pageable pageable = TestUtils.getDefaultPageable();
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

        //when
        //then
        graphQlTester.document(document)
            .variable("name", filter.getName())
            .execute()
            .path("getGalaxies")
            .entityList(GalaxyDto.class)
            .containsExactly(mapper.toDto(entity));
        then(specificationBuilder).should().build(filter);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGetGalaxy() throws Exception {
        //given
        UUID id = UUID.randomUUID();
        Galaxy entity = TestUtils.buildGalaxy();
        GalaxyDto dto = mapper.toDto(entity);

        // language=GraphQL
        String document = """
                query getGalaxy($id:ID!) {
                  getGalaxy(id:$id) {
                    id, name, version
                  }
                }
            """;

        given(service.get(any())).willReturn(entity);

        //when
        //then
        graphQlTester.document(document)
            .variable("id", id)
            .execute()
            .path("getGalaxy")
            .entity(GalaxyDto.class)
            .isEqualTo(dto);
        then(service).should().get(id);
    }

    @Test
    void testAddGalaxy() throws Exception {
        // given
        GalaxyDto inputDto = TestUtils.buildGalaxyDtoForAdd();
        Galaxy entity = TestUtils.buildGalaxy();
        GalaxyDto resultDto = mapper.toDto(entity);

        // language=GraphQL
        String document = """
                mutation addGalaxy($input: AddGalaxyInput!) {
                  addGalaxy(input:$input) {
                    id, name, version
                  }
                }
            """;

        given(service.add(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("input", TestUtils.buildInputMapForGalaxyAdd(inputDto))
            .execute()
            .path("addGalaxy")
            .entity(GalaxyDto.class)
            .isEqualTo(resultDto);

        then(validator).should().validate(inputDto, false);
        then(service).should().add(mapper.toEntity(inputDto));
    }

    @Test
    void testUpdateGalaxy() throws Exception {
        // given
        GalaxyDto inputDto = TestUtils.buildGalaxyDtoForUpdate();
        Galaxy entity = mapper.toEntity(inputDto);
        GalaxyDto resultDto = mapper.toDto(entity);

        // language=GraphQL
        String document = """
                mutation updateGalaxy($input: UpdateGalaxyInput!) {
                  updateGalaxy(input:$input) {
                    id, name, version
                  }
                }
            """;

        given(service.update(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("input", TestUtils.buildInputMapForGalaxyUpdate(inputDto))
            .execute()
            .path("updateGalaxy")
            .entity(GalaxyDto.class)
            .isEqualTo(resultDto);

        then(validator).should().validate(inputDto, true);
        then(service).should().update(entity);
    }

    @Test
    void testDeleteGalaxy() throws Exception {
        //given
        UUID id = UUID.randomUUID();

        // language=GraphQL
        String document = """
                mutation deleteGalaxy($id: ID!) {
                  deleteGalaxy(id:$id)
                }
            """;

        //when
        //then
        graphQlTester.document(document)
            .variable("id", id)
            .executeAndVerify();
        then(service).should().delete(id);
    }

}
