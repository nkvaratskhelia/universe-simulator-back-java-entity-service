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

import java.util.List;
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
    private PlanetMapper planetMapper;

    @Test
    void testGetPlanets() {
        // given
        Planet entity = TestUtils.buildPlanet();
        List<Planet> entityList = List.of(entity);

        PlanetFilter filter = TestUtils.buildPlanetFilter();
        Pageable pageable = TestUtils.buildDefaultPageable();
        Page<Planet> entityPage = new PageImpl<>(entityList, pageable, entityList.size());

        // language=GraphQL
        String document = """
                query getPlanets($name: String, $pageInput: PageInput) {
                  getPlanets(name: $name, pageInput: $pageInput) {
                    id, name, version, starId
                  }
                }
            """;

        given(service.getList(any(), any())).willReturn(entityPage);

        // when
        // then
        graphQlTester.document(document)
            .variable("name", filter.getName())
            .execute()
            .path("getPlanets")
            .entityList(PlanetDto.class)
            .containsExactly(planetMapper.toDto(entity));
        then(specificationBuilder).should().build(filter);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGetPlanet() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        Planet entity = TestUtils.buildPlanet();
        PlanetDto dto = planetMapper.toDto(entity);

        // language=GraphQL
        String document = """
                query getPlanet($id:ID!) {
                  getPlanet(id:$id) {
                    id, name, version, starId
                  }
                }
            """;

        given(service.get(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("id", id)
            .execute()
            .path("getPlanet")
            .entity(PlanetDto.class)
            .isEqualTo(dto);
        then(service).should().get(id);
    }

    @Test
    void testAddPlanet() throws Exception {
        // given
        AddPlanetInput input = TestUtils.buildAddPlanetInput();
        Planet entity = TestUtils.buildPlanet();
        PlanetDto resultDto = planetMapper.toDto(entity);

        // language=GraphQL
        String document = """
                mutation addPlanet($input: AddPlanetInput!) {
                  addPlanet(input:$input) {
                    id, name, version, starId
                  }
                }
            """;

        given(service.add(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("input", TestUtils.buildInputMapForPlanetAdd(input))
            .execute()
            .path("addPlanet")
            .entity(PlanetDto.class)
            .isEqualTo(resultDto);

        then(service).should().add(planetMapper.toEntity(input));
    }

    @Test
    void testUpdatePlanet() throws Exception {
        // given
        UpdatePlanetInput input = TestUtils.buildUpdatePlanetInput();
        Planet entity = planetMapper.toEntity(input);
        PlanetDto resultDto = planetMapper.toDto(entity);

        // language=GraphQL
        String document = """
                mutation updatePlanet($input: UpdatePlanetInput!) {
                  updatePlanet(input:$input) {
                    id, name, version, starId
                  }
                }
            """;

        given(service.update(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("input", TestUtils.buildInputMapForPlanetUpdate(input))
            .execute()
            .path("updatePlanet")
            .entity(PlanetDto.class)
            .isEqualTo(resultDto);

        then(service).should().update(entity);
    }

    @Test
    void testDeletePlanet() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // language=GraphQL
        String document = """
                mutation deletePlanet($id: ID!) {
                  deletePlanet(id:$id)
                }
            """;

        // when
        // then
        graphQlTester.document(document)
            .variable("id", id)
            .executeAndVerify();
        then(service).should().delete(id);
    }
}
