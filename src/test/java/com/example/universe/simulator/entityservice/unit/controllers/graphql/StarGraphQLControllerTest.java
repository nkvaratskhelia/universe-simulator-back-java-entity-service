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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
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
        Star entity = TestUtils.buildStar();
        List<Star> entityList = List.of(entity);

        StarFilter filter = TestUtils.buildStarFilter();
        Pageable pageable = TestUtils.buildDefaultPageable();
        Page<Star> entityPage = new PageImpl<>(entityList, pageable, entityList.size());

        // language=GraphQL
        String document = """
                query getStars($name: String, $pageInput: PageInput) {
                  getStars(name: $name, pageInput: $pageInput) {
                    id, name, version, galaxyId
                  }
                }
            """;

        given(service.getList(any(), any())).willReturn(entityPage);

        // when
        // then
        graphQlTester.document(document)
            .variable("name", filter.getName())
            .execute()
            .path("getStars")
            .entityList(StarDto.class)
            .containsExactly(mapper.toDto(entity));
        then(specificationBuilder).should().build(filter);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testGetStar() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        Star entity = TestUtils.buildStar();
        StarDto dto = mapper.toDto(entity);

        // language=GraphQL
        String document = """
                query getStar($id:ID!) {
                  getStar(id:$id) {
                    id, name, version, galaxyId
                  }
                }
            """;

        given(service.get(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("id", id)
            .execute()
            .path("getStar")
            .entity(StarDto.class)
            .isEqualTo(dto);
        then(service).should().get(id);
    }

    @Test
    void testAddStar() throws Exception {
        // given
        AddStarInput input = TestUtils.buildAddStarInput();
        Star entity = TestUtils.buildStar();
        StarDto resultDto = mapper.toDto(entity);

        // language=GraphQL
        String document = """
                mutation addStar($input: AddStarInput!) {
                  addStar(input:$input) {
                    id, name, version, galaxyId
                  }
                }
            """;

        given(service.add(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("input", TestUtils.buildInputMapForStarAdd(input))
            .execute()
            .path("addStar")
            .entity(StarDto.class)
            .isEqualTo(resultDto);

        then(service).should().add(mapper.toEntity(input));
    }

    @Test
    void testUpdateStar() throws Exception {
        // given
        UpdateStarInput input = TestUtils.buildUpdateStarInput();
        Star entity = mapper.toEntity(input);
        StarDto resultDto = mapper.toDto(entity);

        // language=GraphQL
        String document = """
                mutation updateStar($input: UpdateStarInput!) {
                  updateStar(input:$input) {
                    id, name, version, galaxyId
                  }
                }
            """;

        given(service.update(any())).willReturn(entity);

        // when
        // then
        graphQlTester.document(document)
            .variable("input", TestUtils.buildInputMapForStarUpdate(input))
            .execute()
            .path("updateStar")
            .entity(StarDto.class)
            .isEqualTo(resultDto);

        then(service).should().update(entity);
    }

    @Test
    void testDeleteStar() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // language=GraphQL
        String document = """
                mutation deleteStar($id: ID!) {
                  deleteStar(id:$id)
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
