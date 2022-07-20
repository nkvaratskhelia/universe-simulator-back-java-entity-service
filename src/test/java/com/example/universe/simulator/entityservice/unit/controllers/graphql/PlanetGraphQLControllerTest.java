package com.example.universe.simulator.entityservice.unit.controllers.graphql;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractGraphQLTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.graphql.PlanetGraphQLController;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.filters.PlanetFilter;
import com.example.universe.simulator.entityservice.inputs.AddPlanetInput;
import com.example.universe.simulator.entityservice.inputs.UpdatePlanetInput;
import com.example.universe.simulator.entityservice.mappers.PlanetMapper;
import com.example.universe.simulator.entityservice.mappers.PlanetMapperImpl;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.specifications.PlanetSpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@GraphQlTest(PlanetGraphQLController.class)
@Import(PlanetMapperImpl.class)
class PlanetGraphQLControllerTest extends AbstractGraphQLTest {

    @MockBean
    private PlanetService service;

    @MockBean
    private PlanetSpecificationBuilder specificationBuilder;

    @SpyBean
    private PlanetMapper mapper;

    @Test
    void testGetPlanets() {
        // given
        // language=GraphQL
        var document = """
            query getPlanets($name: String) {
              getPlanets(name: $name) {
                id, version, name, starId
              }
            }
            """;

        PlanetFilter filter = TestUtils.buildPlanetFilter();
        Pageable pageable = TestUtils.buildDefaultPageable();

        List<Planet> entities = List.of(TestUtils.buildPlanet());
        List<PlanetDto> dtos = mapper.toDtoList(entities);

        Page<Planet> page = new PageImpl<>(entities, pageable, entities.size());
        given(service.getList(any(), any())).willReturn(page);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("name", filter.getName())
            .execute();
        // then
        response
            .path("getPlanets")
            .entityList(PlanetDto.class)
            .isEqualTo(dtos);

        then(specificationBuilder).should().build(filter);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGetPlanet() throws Exception {
        // given
        // language=GraphQL
        var document = """
            query getPlanet($id: ID!) {
              getPlanet(id: $id) {
                id, version, name, starId
              }
            }
            """;

        var id = UUID.randomUUID();

        Planet entity = TestUtils.buildPlanet();
        PlanetDto dto = mapper.toDto(entity);

        given(service.get(any())).willReturn(entity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("id", id)
            .execute();
        // then
        response
            .path("getPlanet")
            .entity(PlanetDto.class)
            .isEqualTo(dto);

        then(service).should().get(id);
    }

    @Test
    void testAddPlanet() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation addPlanet($input: AddPlanetInput!) {
              addPlanet(input: $input) {
                id, version, name, starId
              }
            }
            """;

        AddPlanetInput input = TestUtils.buildAddPlanetInput();
        Map<String, Object> inputMap = TestUtils.buildAddPlanetInputMap(input);
        Planet inputEntity = mapper.toEntity(input);

        Planet resultEntity = mapper.toEntity(input);
        resultEntity.setId(UUID.randomUUID());
        resultEntity.setVersion(0L);

        PlanetDto resultDto = mapper.toDto(resultEntity);

        given(service.add(any())).willReturn(resultEntity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("input", inputMap)
            .execute();
        // then
        response
            .path("addPlanet")
            .entity(PlanetDto.class)
            .isEqualTo(resultDto);

        then(service).should().add(inputEntity);
    }

    @Test
    void testUpdatePlanet() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation updatePlanet($input: UpdatePlanetInput!) {
              updatePlanet(input: $input) {
                id, version, name, starId
              }
            }
            """;

        UpdatePlanetInput input = TestUtils.buildUpdatePlanetInput();
        Map<String, Object> inputMap = TestUtils.buildUpdatePlanetInputMap(input);
        Planet inputEntity = mapper.toEntity(input);

        Planet resultEntity = mapper.toEntity(input);
        resultEntity.setVersion(resultEntity.getVersion() + 1);
        PlanetDto resultDto = mapper.toDto(resultEntity);

        given(service.update(any())).willReturn(resultEntity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("input", inputMap)
            .execute();
        // then
        response
            .path("updatePlanet")
            .entity(PlanetDto.class)
            .isEqualTo(resultDto);

        then(service).should().update(inputEntity);
    }

    @Test
    void testDeletePlanet() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation deletePlanet($id: ID!) {
              deletePlanet(id: $id)
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
            .path("deletePlanet")
            .entity(UUID.class)
            .isEqualTo(id);

        then(service).should().delete(id);
    }
}
