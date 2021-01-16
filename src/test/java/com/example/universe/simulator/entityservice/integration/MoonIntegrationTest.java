package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.filters.MoonFilter;
import com.example.universe.simulator.entityservice.utils.JsonPage;
import com.example.universe.simulator.entityservice.utils.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class MoonIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        //-----------------------------------should add galaxy-----------------------------------

        //given
        GalaxyDto galaxyDto = TestUtils.buildGalaxyDtoForAdd();
        //when
        MockHttpServletResponse response = performRequest(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(galaxyDto))
        );
        //then
        GalaxyDto addedGalaxy = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        //-----------------------------------should add star-----------------------------------

        //given
        StarDto starDto = TestUtils.buildStarDtoForAdd();
        starDto.getGalaxy().setId(addedGalaxy.getId());
        //when
        response = performRequest(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(starDto))
        );
        //then
        StarDto addedStar = objectMapper.readValue(response.getContentAsString(), StarDto.class);

        //-----------------------------------should add planet-----------------------------------

        //given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.getStar().setId(addedStar.getId());
        //when
        response = performRequest(post("/planet/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto))
        );
        //then
        PlanetDto addedPlanet = objectMapper.readValue(response.getContentAsString(), PlanetDto.class);

        //-----------------------------------should throw sort parameter error-----------------------------------

        //when
        response = performRequest(post("/moon/get-list")
                .param("sort", "invalid")
        );
        //then
        verifyErrorResponse(response, ErrorCodeType.INVALID_SORT_PARAMETER);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = performRequest(post("/moon/get-list"));
        //then
        JsonPage<MoonDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        //-----------------------------------should add entity-----------------------------------

        //given
        MoonDto dto = TestUtils.buildMoonDtoForAdd();
        dto.setName("name1");
        dto.getPlanet().setId(addedPlanet.getId());
        //when
        response = performRequest(post("/moon/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        MoonDto addedDto1 = objectMapper.readValue(response.getContentAsString(), MoonDto.class);

        //-----------------------------------should add entity-----------------------------------

        //given
        dto = TestUtils.buildMoonDtoForAdd();
        dto.setName("name2");
        dto.getPlanet().setId(addedPlanet.getId());
        //when
        response = performRequest(post("/moon/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        MoonDto addedDto2 = objectMapper.readValue(response.getContentAsString(), MoonDto.class);

        //-----------------------------------should return list with 2 elements-----------------------------------

        //when
        response = performRequest(post("/moon/get-list"));
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(2);

        //-----------------------------------should return entity-----------------------------------

        //when
        response = performRequest(get("/moon/get/{id}", addedDto1.getId()));
        //then
        MoonDto resultDto = objectMapper.readValue(response.getContentAsString(), MoonDto.class);
        assertThat(resultDto).isEqualTo(addedDto1);
        assertThat(resultDto.getPlanet().getId()).isEqualTo(addedPlanet.getId());

        //-----------------------------------should update entity-----------------------------------

        //given
        dto = TestUtils.buildMoonDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName("name1Update");
        dto.getPlanet().setId(addedPlanet.getId());

        //when
        response = performRequest(put("/moon/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        MoonDto updatedDto = objectMapper.readValue(response.getContentAsString(), MoonDto.class);
        assertThat(updatedDto.getName()).isEqualTo(dto.getName());
        assertThat(updatedDto.getVersion()).isEqualTo(dto.getVersion() + 1);

        //-----------------------------------should throw entity modified error-----------------------------------

        //given
        dto = TestUtils.buildMoonDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.getPlanet().setId(addedPlanet.getId());
        //when
        response = performRequest(put("/moon/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        verifyErrorResponse(response, ErrorCodeType.ENTITY_MODIFIED);

        //-----------------------------------should return list with 1 element-----------------------------------

        //given
        MoonFilter filter = MoonFilter.builder().name("1uP").build();
        //when
        response = performRequest(post("/moon/get-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter))
        );
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(1);

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = performRequest(delete("/moon/delete/{id}", addedDto1.getId()));
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = performRequest(delete("/moon/delete/{id}", addedDto2.getId()));
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should throw not found error-----------------------------------

        //when
        response = performRequest(delete("/moon/delete/{id}", addedDto1.getId()));
        //then
        verifyErrorResponse(response, ErrorCodeType.NOT_FOUND_ENTITY);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = performRequest(post("/moon/get-list"));
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        //-----------------------------------should delete planet-----------------------------------

        //when
        response = performRequest(delete("/planet/delete/{id}", addedPlanet.getId()));
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should delete star-----------------------------------

        //when
        response = performRequest(delete("/star/delete/{id}", addedStar.getId()));
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should delete galaxy-----------------------------------

        //when
        response = performRequest(delete("/galaxy/delete/{id}", addedGalaxy.getId()));
        //then
        verifyOkStatus(response.getStatus());
    }
}
