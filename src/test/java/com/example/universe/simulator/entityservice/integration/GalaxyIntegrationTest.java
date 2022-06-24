package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.common.dtos.EventDto;
import com.example.universe.simulator.entityservice.common.utils.JsonPage;
import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
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

class GalaxyIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        // -----------------------------------should return empty list-----------------------------------

        // when
        MockHttpServletResponse response = performRequest(get("/galaxy/get-list"));
        // then
        JsonPage<GalaxyDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        // -----------------------------------add entity-----------------------------------

        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName("name1");

        response = performRequest(post("/galaxy/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );

        GalaxyDto addedDto1 = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        // -----------------------------------add another entity-----------------------------------

        dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName("name2");

        response = performRequest(post("/galaxy/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );

        GalaxyDto addedDto2 = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        // -----------------------------------should return list with 2 elements-----------------------------------

        // when
        response = performRequest(get("/galaxy/get-list"));
        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(2);

        // -----------------------------------should return entity and store it in cache-----------------------------------

        // when
        response = performRequest(get("/galaxy/get/{id}", addedDto1.getId()));
        // then
        GalaxyDto resultDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(resultDto).isEqualTo(addedDto1);

        GalaxyDto fromCache = modelMapper.map(cacheManager.getCache("Galaxy").get(addedDto1.getId()).get(), GalaxyDto.class);
        assertThat(fromCache).isEqualTo(addedDto1);

        // -----------------------------------should update entity-----------------------------------

        // given
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName("name1Update");

        // when
        performRequest(put("/galaxy/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );

        //check if resultDto got updated in db
        response = performRequest(get("/galaxy/get/{id}", addedDto1.getId()));

        // then
        resultDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);
        assertThat(resultDto.getName()).isEqualTo(dto.getName());
        assertThat(resultDto.getVersion()).isEqualTo(dto.getVersion() + 1);

        // -----------------------------------should return list with 1 element-----------------------------------

        // when
        response = performRequest(get("/galaxy/get-list")
            .param("name", "1uP")
        );
        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(1);

        // -----------------------------------should delete entity from database and from cache-----------------------------------

        // when
        response = performRequest(delete("/galaxy/delete/{id}", addedDto1.getId()));
        // then
        verifyOkStatus(response.getStatus());
        assertThat(cacheManager.getCache("Galaxy").get(addedDto1.getId())).isNull();

        // -----------------------------------should delete entity-----------------------------------

        // when
        response = performRequest(delete("/galaxy/delete/{id}", addedDto2.getId()));
        // then
        verifyOkStatus(response.getStatus());

        // -----------------------------------should return empty list-----------------------------------

        // when
        response = performRequest(get("/galaxy/get-list"));
        // then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        // -----------------------------------should have fired application events-----------------------------------

        // given
        Map<String, Long> eventsByType = applicationEvents.stream(EventDto.class)
            .collect(Collectors.groupingBy(EventDto::type, Collectors.counting()));

        // then
        assertThat(eventsByType).isEqualTo(Map.ofEntries(
            Map.entry(EventType.GALAXY_ADD.toString(), 2L),
            Map.entry(EventType.GALAXY_UPDATE.toString(), 1L),
            Map.entry(EventType.GALAXY_DELETE.toString(), 2L)
        ));
    }
}
