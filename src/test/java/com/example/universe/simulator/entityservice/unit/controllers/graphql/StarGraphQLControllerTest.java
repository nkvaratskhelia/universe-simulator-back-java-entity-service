package com.example.universe.simulator.entityservice.unit.controllers.graphql;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractGraphQLTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.graphql.StarGraphQLController;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.entities.Star;
import com.example.universe.simulator.entityservice.filters.StarFilter;
import com.example.universe.simulator.entityservice.inputs.AddStarInput;
import com.example.universe.simulator.entityservice.inputs.UpdateStarInput;
import com.example.universe.simulator.entityservice.mappers.StarMapper;
import com.example.universe.simulator.entityservice.mappers.StarMapperImpl;
import com.example.universe.simulator.entityservice.services.StarService;
import com.example.universe.simulator.entityservice.specifications.StarSpecificationBuilder;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@GraphQlTest(StarGraphQLController.class)
@Import(StarMapperImpl.class)
class StarGraphQLControllerTest extends AbstractGraphQLTest {

    @MockBean
    private StarService service;

    @MockBean
    private StarSpecificationBuilder specificationBuilder;

    @SpyBean
    private StarMapper mapper;

    @Test
    void testGetStars() {
        // given
        // language=GraphQL
        var document = """
            query getStars($name: String) {
              getStars(name: $name) {
                id, version, name, galaxyId
              }
            }
            """;

        StarFilter filter = TestUtils.buildStarFilter();
        Pageable pageable = TestUtils.buildDefaultPageable();

        List<Star> entities = List.of(TestUtils.buildStar());
        List<StarDto> dtos = mapper.toDtoList(entities);

        Page<Star> page = new PageImpl<>(entities, pageable, entities.size());
        given(service.getList(any(), eq(pageable))).willReturn(page);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("name", filter.getName())
            .execute();
        // then
        response
            .path("getStars")
            .entityList(StarDto.class)
            .isEqualTo(dtos);

        then(specificationBuilder).should().build(filter);
    }

    @Test
    void testGetStar() throws Exception {
        // given
        // language=GraphQL
        var document = """
            query getStar($id: ID!) {
              getStar(id: $id) {
                id, version, name, galaxyId
              }
            }
            """;

        var id = UUID.randomUUID();

        Star entity = TestUtils.buildStar();
        StarDto dto = mapper.toDto(entity);

        given(service.get(any())).willReturn(entity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("id", id)
            .execute();
        // then
        response
            .path("getStar")
            .entity(StarDto.class)
            .isEqualTo(dto);

        then(service).should().get(id);
    }

    @Test
    void testAddStar() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation addStar($input: AddStarInput!) {
              addStar(input: $input) {
                id, version, name, galaxyId
              }
            }
            """;

        AddStarInput input = TestUtils.buildAddStarInput();
        Map<String, Object> inputMap = TestUtils.buildAddStarInputMap(input);
        Star inputEntity = mapper.toEntity(input);

        Star resultEntity = mapper.toEntity(input);
        resultEntity.setId(UUID.randomUUID());
        resultEntity.setVersion(0L);

        StarDto resultDto = mapper.toDto(resultEntity);

        given(service.add(any())).willReturn(resultEntity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("input", inputMap)
            .execute();
        // then
        response
            .path("addStar")
            .entity(StarDto.class)
            .isEqualTo(resultDto);

        then(service).should().add(inputEntity);
    }

    @Test
    void testUpdateStar() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation updateStar($input: UpdateStarInput!) {
              updateStar(input: $input) {
                id, version, name, galaxyId
              }
            }
            """;

        UpdateStarInput input = TestUtils.buildUpdateStarInput();
        Map<String, Object> inputMap = TestUtils.buildUpdateStarInputMap(input);
        Star inputEntity = mapper.toEntity(input);

        Star resultEntity = mapper.toEntity(input);
        resultEntity.setVersion(resultEntity.getVersion() + 1);
        StarDto resultDto = mapper.toDto(resultEntity);

        given(service.update(any())).willReturn(resultEntity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("input", inputMap)
            .execute();
        // then
        response
            .path("updateStar")
            .entity(StarDto.class)
            .isEqualTo(resultDto);

        then(service).should().update(inputEntity);
    }

    @Test
    void testDeleteStar() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation deleteStar($id: ID!) {
              deleteStar(id: $id)
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
            .path("deleteStar")
            .entity(UUID.class)
            .isEqualTo(id);

        then(service).should().delete(id);
    }
}
