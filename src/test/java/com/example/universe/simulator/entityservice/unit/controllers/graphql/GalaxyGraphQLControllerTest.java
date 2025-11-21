package com.example.universe.simulator.entityservice.unit.controllers.graphql;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractGraphQLTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.graphql.GalaxyGraphQLController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.filters.GalaxyFilter;
import com.example.universe.simulator.entityservice.inputs.AddGalaxyInput;
import com.example.universe.simulator.entityservice.inputs.UpdateGalaxyInput;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapper;
import com.example.universe.simulator.entityservice.mappers.GalaxyMapperImpl;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@GraphQlTest(GalaxyGraphQLController.class)
@Import(GalaxyMapperImpl.class)
class GalaxyGraphQLControllerTest extends AbstractGraphQLTest {

    @MockitoBean
    private GalaxyService service;

    @MockitoBean
    private GalaxySpecificationBuilder specificationBuilder;

    @MockitoSpyBean
    private GalaxyMapper mapper;

    @Test
    void testGetGalaxies() {
        // given
        // language=GraphQL
        var document = """
            query getGalaxies($name: String) {
              getGalaxies(name: $name) {
                id, version, name
              }
            }
            """;

        GalaxyFilter filter = TestUtils.buildGalaxyFilter();
        Pageable pageable = TestUtils.buildDefaultPageable();

        List<Galaxy> entities = List.of(TestUtils.buildGalaxy());
        List<GalaxyDto> dtos = mapper.toDtoList(entities);

        Page<@NonNull Galaxy> page = new PageImpl<>(entities, pageable, entities.size());
        given(service.getList(any(), eq(pageable))).willReturn(page);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("name", filter.getName())
            .execute();
        // then
        response
            .path("getGalaxies")
            .entityList(GalaxyDto.class)
            .isEqualTo(dtos);

        then(specificationBuilder).should().build(filter);
    }

    @Test
    void testGetGalaxy() throws Exception {
        // given
        // language=GraphQL
        var document = """
            query getGalaxy($id: ID!) {
              getGalaxy(id: $id) {
                id, version, name
              }
            }
            """;

        var id = UUID.randomUUID();

        Galaxy entity = TestUtils.buildGalaxy();
        GalaxyDto dto = mapper.toDto(entity);

        given(service.get(any())).willReturn(entity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("id", id)
            .execute();
        // then
        response
            .path("getGalaxy")
            .entity(GalaxyDto.class)
            .isEqualTo(dto);

        then(service).should().get(id);
    }

    @Test
    void testAddGalaxy() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation addGalaxy($input: AddGalaxyInput!) {
              addGalaxy(input: $input) {
                id, version, name
              }
            }
            """;

        AddGalaxyInput input = TestUtils.buildAddGalaxyInput();
        Map<String, Object> inputMap = TestUtils.buildAddGalaxyInputMap(input);
        Galaxy inputEntity = mapper.toEntity(input);

        Galaxy resultEntity = TestUtils.buildGalaxy();
        GalaxyDto resultDto = mapper.toDto(resultEntity);

        given(service.add(any())).willReturn(resultEntity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("input", inputMap)
            .execute();
        // then
        response
            .path("addGalaxy")
            .entity(GalaxyDto.class)
            .isEqualTo(resultDto);

        then(service).should().add(inputEntity);
    }

    @Test
    void testUpdateGalaxy() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation updateGalaxy($input: UpdateGalaxyInput!) {
              updateGalaxy(input: $input) {
                id, version, name
              }
            }
            """;

        UpdateGalaxyInput input = TestUtils.buildUpdateGalaxyInput();
        Map<String, Object> inputMap = TestUtils.buildUpdateGalaxyInputMap(input);
        Galaxy inputEntity = mapper.toEntity(input);

        Galaxy resultEntity = mapper.toEntity(input);
        resultEntity.setVersion(resultEntity.getVersion() + 1);
        GalaxyDto resultDto = mapper.toDto(resultEntity);

        given(service.update(any())).willReturn(resultEntity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("input", inputMap)
            .execute();
        // then
        response
            .path("updateGalaxy")
            .entity(GalaxyDto.class)
            .isEqualTo(resultDto);

        then(service).should().update(inputEntity);
    }

    @Test
    void testDeleteGalaxy() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation deleteGalaxy($id: ID!) {
              deleteGalaxy(id: $id)
            }
            """;

        UUID id = UUID.randomUUID();
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("id", id)
            .execute();
        // then
        response
            .path("deleteGalaxy")
            .entity(UUID.class)
            .isEqualTo(id);

        then(service).should().delete(id);
    }
}
