package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
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
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(galaxyDto))
        ).andReturn().getResponse();
        //then
        GalaxyDto addedGalaxy = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        //-----------------------------------should add star-----------------------------------

        //given
        StarDto starDto = TestUtils.buildStarDtoForAdd();
        starDto.getGalaxy().setId(addedGalaxy.getId());
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(starDto))
        ).andReturn().getResponse();
        //then
        StarDto addedStar = objectMapper.readValue(response.getContentAsString(), StarDto.class);

        //-----------------------------------should add planet-----------------------------------

        //given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.getStar().setId(addedStar.getId());
        //when
        response = mockMvc.perform(post("/planet/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto))
        ).andReturn().getResponse();
        //then
        PlanetDto addedPlanet = objectMapper.readValue(response.getContentAsString(), PlanetDto.class);

        //-----------------------------------should throw sort parameter error-----------------------------------

        //when
        response = mockMvc.perform(get("/moon/get-list")
                .param("sort", "invalid")
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.INVALID_SORT_PARAMETER);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = mockMvc.perform(get("/moon/get-list")).andReturn().getResponse();
        //then
        JsonPage<MoonDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        //-----------------------------------should add entity-----------------------------------

        //given
        MoonDto dto = TestUtils.buildMoonDtoForAdd();
        dto.setName("name1");
        dto.getPlanet().setId(addedPlanet.getId());
        //when
        response = mockMvc.perform(post("/moon/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        MoonDto addedDto1 = objectMapper.readValue(response.getContentAsString(), MoonDto.class);

        //-----------------------------------should add entity-----------------------------------

        //given
        dto = TestUtils.buildMoonDtoForAdd();
        dto.setName("name2");
        dto.getPlanet().setId(addedPlanet.getId());
        //when
        response = mockMvc.perform(post("/moon/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        MoonDto addedDto2 = objectMapper.readValue(response.getContentAsString(), MoonDto.class);

        //-----------------------------------should return list with 2 elements-----------------------------------

        //when
        response = mockMvc.perform(get("/moon/get-list")).andReturn().getResponse();
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(2);

        //-----------------------------------should return entity-----------------------------------

        //when
        response = mockMvc.perform(get("/moon/get/{id}", addedDto1.getId())).andReturn().getResponse();
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
        response = mockMvc.perform(put("/moon/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
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
        response = mockMvc.perform(put("/moon/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_MODIFIED);

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = mockMvc.perform(delete("/moon/delete/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = mockMvc.perform(delete("/moon/delete/{id}", addedDto2.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should throw not found error-----------------------------------

        //when
        response = mockMvc.perform(delete("/moon/delete/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.NOT_FOUND_ENTITY);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = mockMvc.perform(get("/moon/get-list")).andReturn().getResponse();
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        //-----------------------------------should delete planet-----------------------------------

        //when
        response = mockMvc.perform(delete("/planet/delete/{id}", addedPlanet.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should delete star-----------------------------------

        //when
        response = mockMvc.perform(delete("/star/delete/{id}", addedStar.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should delete galaxy-----------------------------------

        //when
        response = mockMvc.perform(delete("/galaxy/delete/{id}", addedGalaxy.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
    }
}
