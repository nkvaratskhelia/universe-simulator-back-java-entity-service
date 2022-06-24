package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.common.dtos.EventDto;
import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.types.EventType;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class StarIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        // -----------------------------------add galaxy-----------------------------------

        GalaxyDto galaxyDto = TestUtils.buildGalaxyDtoForAdd();
        MockHttpServletResponse response = performRequest(post("/galaxy/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(galaxyDto))
        );
        GalaxyDto addedGalaxy = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        // -----------------------------------should return empty list-----------------------------------

        // when
        response = performRequest(get("/star/get-list"));
        // then
        JsonPage<StarDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        // -----------------------------------add entity-----------------------------------

        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.setName("name1");
        dto.getGalaxy().setId(addedGalaxy.getId());

        response = performRequest(post("/star/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );

        StarDto addedDto1 = objectMapper.readValue(response.getContentAsString(), StarDto.class);

        // -----------------------------------add another entity-----------------------------------

        dto = TestUtils.buildStarDtoForAdd();
        dto.setName("name2");
        dto.getGalaxy().setId(addedGalaxy.getId());

        response = performRequest(post("/star/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );

        StarDto addedDto2 = objectMapper.readValue(response.getContentAsString(), StarDto.class);

        // -----------------------------------should return list with 2 elements-----------------------------------

        // when
        response = performRequest(get("/star/get-list"));
        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(2);

        // -----------------------------------should return entity-----------------------------------

        // when
        response = performRequest(get("/star/get/{id}", addedDto1.getId()));
        // then
        StarDto resultDto = objectMapper.readValue(response.getContentAsString(), StarDto.class);
        assertThat(resultDto).isEqualTo(addedDto1);
        assertThat(resultDto.getGalaxy().getId()).isEqualTo(addedGalaxy.getId());

        // -----------------------------------should update entity-----------------------------------

        // given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName("name1Update");
        dto.getGalaxy().setId(addedGalaxy.getId());

        // when
        performRequest(put("/star/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );
        response = performRequest(get("/star/get/{id}", addedDto1.getId()));

        // then
        resultDto = objectMapper.readValue(response.getContentAsString(), StarDto.class);
        assertThat(resultDto.getName()).isEqualTo(dto.getName());
        assertThat(resultDto.getVersion()).isEqualTo(dto.getVersion() + 1);

        // -----------------------------------should return list with 1 element-----------------------------------

        // when
        response = performRequest(get("/star/get-list")
            .param("name", "1uP")
        );
        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(1);

        // -----------------------------------should delete entity-----------------------------------

        // when
        response = performRequest(delete("/star/delete/{id}", addedDto1.getId()));
        // then
        verifyOkStatus(response.getStatus());

        // -----------------------------------should delete entity-----------------------------------

        // when
        response = performRequest(delete("/star/delete/{id}", addedDto2.getId()));
        // then
        verifyOkStatus(response.getStatus());

        // -----------------------------------should return empty list-----------------------------------

        // when
        response = performRequest(get("/star/get-list"));
        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        // -----------------------------------should have fired application events-----------------------------------

        // given
        Map<String, Long> eventsByType = applicationEvents.stream(EventDto.class)
            .collect(Collectors.groupingBy(EventDto::type, Collectors.counting()));

        // then
        assertThat(eventsByType).isEqualTo(Map.ofEntries(
            Map.entry(EventType.GALAXY_ADD.toString(), 1L),
            Map.entry(EventType.STAR_ADD.toString(), 2L),
            Map.entry(EventType.STAR_UPDATE.toString(), 1L),
            Map.entry(EventType.STAR_DELETE.toString(), 2L)
        ));

        // cleanup
        performRequest(delete("/galaxy/delete/{id}", addedGalaxy.getId()));
    }
}
