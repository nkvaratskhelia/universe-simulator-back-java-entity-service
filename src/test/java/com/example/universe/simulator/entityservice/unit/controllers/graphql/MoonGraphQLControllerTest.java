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

@GraphQlTest(MoonGraphQLController.class)
@Import(MoonMapperImpl.class)
class MoonGraphQLControllerTest extends AbstractGraphQLTest {

    @MockBean
    private MoonService service;

    @MockBean
    private MoonSpecificationBuilder specificationBuilder;

    @SpyBean
    private MoonMapper moonMapper;

    @Test
    void testGetMoons() {
        // given
        Moon entity = TestUtils.buildMoon();
        List<Moon> entityList = List.of(entity);

        MoonFilter filter = TestUtils.buildMoonFilter();
        Pageable pageable = TestUtils.buildDefaultPageable();
        Page<Moon> entityPage = new PageImpl<>(entityList, pageable, entityList.size());

        // language=GraphQL
        String document = """
                query getMoons($name: String, $pageInput: PageInput) {
                  getMoons(name: $name, pageInput: $pageInput) {
                    id, name, version, planetId
                  }
                }
            """;

        given(service.getList(any(), any())).willReturn(entityPage);

        // when
        // then
        graphQlTester.document(document)
            .variable("name", filter.getName())
            .execute()
            .path("getMoons")
            .entityList(MoonDto.class)
            .containsExactly(moonMapper.toDto(entity));
        then(specificationBuilder).should().build(filter);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGetMoon() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        Moon entity = TestUtils.buildMoon();
        MoonDto dto = moonMapper.toDto(entity);

        // language=GraphQL
        String document = """
                query getMoon($id:ID!) {
                  getMoon(id:$id) {
                    id, name, version, planetId
                  }
                }
            """;

        given(service.get(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("id", id)
            .execute()
            .path("getMoon")
            .entity(MoonDto.class)
            .isEqualTo(dto);
        then(service).should().get(id);
    }

    @Test
    void testAddMoon() throws Exception {
        // given
        AddMoonInput input = TestUtils.buildAddMoonInput();
        Moon entity = TestUtils.buildMoon();
        MoonDto resultDto = moonMapper.toDto(entity);

        // language=GraphQL
        String document = """
                mutation addMoon($input: AddMoonInput!) {
                  addMoon(input:$input) {
                    id, name, version, planetId
                  }
                }
            """;

        given(service.add(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("input", TestUtils.buildInputMapForMoonAdd(input))
            .execute()
            .path("addMoon")
            .entity(MoonDto.class)
            .isEqualTo(resultDto);

        then(service).should().add(moonMapper.toEntity(input));
    }

    @Test
    void testUpdateMoon() throws Exception {
        // given
        UpdateMoonInput input = TestUtils.buildUpdateMoonInput();
        Moon entity = moonMapper.toEntity(input);
        MoonDto resultDto = moonMapper.toDto(entity);

        // language=GraphQL
        String document = """
                mutation updateMoon($input: UpdateMoonInput!) {
                  updateMoon(input:$input) {
                    id, name, version, planetId
                  }
                }
            """;

        given(service.update(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("input", TestUtils.buildInputMapForMoonUpdate(input))
            .execute()
            .path("updateMoon")
            .entity(MoonDto.class)
            .isEqualTo(resultDto);

        then(service).should().update(entity);
    }

    @Test
    void testDeleteMoon() {
        // given
        UUID id = UUID.randomUUID();

        // language=GraphQL
        String document = """
                mutation deleteMoon($id: ID!) {
                  deleteMoon(id:$id)
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