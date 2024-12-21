package com.example.universe.simulator.entityservice.unit.controllers.graphql;

import com.example.universe.simulator.entityservice.common.abstractions.AbstractGraphQLTest;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.graphql.MoonGraphQLController;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.entities.Moon;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.inputs.AddMoonInput;
import com.example.universe.simulator.entityservice.inputs.UpdateMoonInput;
import com.example.universe.simulator.entityservice.mappers.MoonMapper;
import com.example.universe.simulator.entityservice.mappers.MoonMapperImpl;
import com.example.universe.simulator.entityservice.services.MoonService;
import com.example.universe.simulator.entityservice.specifications.MoonSpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@GraphQlTest(MoonGraphQLController.class)
@Import(MoonMapperImpl.class)
class MoonGraphQLControllerTest extends AbstractGraphQLTest {

    @MockitoBean
    private MoonService service;

    @MockitoBean
    private MoonSpecificationBuilder specificationBuilder;

    @MockitoSpyBean
    private MoonMapper mapper;

    @Test
    void testGetMoons() {
        // given
        // language=GraphQL
        var document = """
            query getMoons($name: String) {
              getMoons(name: $name) {
                id, version, name, planetId
              }
            }
            """;

        MoonFilter filter = TestUtils.buildMoonFilter();
        Pageable pageable = TestUtils.buildDefaultPageable();

        List<Moon> entities = List.of(TestUtils.buildMoon());
        List<MoonDto> dtos = mapper.toDtoList(entities);

        Page<Moon> page = new PageImpl<>(entities, pageable, entities.size());
        given(service.getList(any(), eq(pageable))).willReturn(page);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("name", filter.getName())
            .execute();
        // then
        response
            .path("getMoons")
            .entityList(MoonDto.class)
            .isEqualTo(dtos);

        then(specificationBuilder).should().build(filter);
    }

    @Test
    void testGetMoon() throws Exception {
        // given
        // language=GraphQL
        var document = """
            query getMoon($id: ID!) {
              getMoon(id: $id) {
                id, version, name, planetId
              }
            }
            """;

        var id = UUID.randomUUID();

        Moon entity = TestUtils.buildMoon();
        MoonDto dto = mapper.toDto(entity);

        given(service.get(any())).willReturn(entity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("id", id)
            .execute();
        // then
        response
            .path("getMoon")
            .entity(MoonDto.class)
            .isEqualTo(dto);

        then(service).should().get(id);
    }

    @Test
    void testAddMoon() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation addMoon($input: AddMoonInput!) {
              addMoon(input: $input) {
                id, version, name, planetId
              }
            }
            """;

        AddMoonInput input = TestUtils.buildAddMoonInput();
        Map<String, Object> inputMap = TestUtils.buildAddMoonInputMap(input);
        Moon inputEntity = mapper.toEntity(input);

        Moon resultEntity = mapper.toEntity(input);
        resultEntity.setId(UUID.randomUUID());
        resultEntity.setVersion(0L);

        MoonDto resultDto = mapper.toDto(resultEntity);

        given(service.add(any())).willReturn(resultEntity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("input", inputMap)
            .execute();
        // then
        response
            .path("addMoon")
            .entity(MoonDto.class)
            .isEqualTo(resultDto);

        then(service).should().add(inputEntity);
    }

    @Test
    void testUpdateMoon() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation updateMoon($input: UpdateMoonInput!) {
              updateMoon(input: $input) {
                id, version, name, planetId
              }
            }
            """;

        UpdateMoonInput input = TestUtils.buildUpdateMoonInput();
        Map<String, Object> inputMap = TestUtils.buildUpdateMoonInputMap(input);
        Moon inputEntity = mapper.toEntity(input);

        Moon resultEntity = mapper.toEntity(input);
        resultEntity.setVersion(resultEntity.getVersion() + 1);
        MoonDto resultDto = mapper.toDto(resultEntity);

        given(service.update(any())).willReturn(resultEntity);
        // when
        GraphQlTester.Response response = graphQlTester
            .document(document)
            .variable("input", inputMap)
            .execute();
        // then
        response
            .path("updateMoon")
            .entity(MoonDto.class)
            .isEqualTo(resultDto);

        then(service).should().update(inputEntity);
    }

    @Test
    void testDeleteMoon() throws Exception {
        // given
        // language=GraphQL
        var document = """
            mutation deleteMoon($id: ID!) {
              deleteMoon(id: $id)
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
            .path("deleteMoon")
            .entity(UUID.class)
            .isEqualTo(id);

        then(service).should().delete(id);
    }
}
