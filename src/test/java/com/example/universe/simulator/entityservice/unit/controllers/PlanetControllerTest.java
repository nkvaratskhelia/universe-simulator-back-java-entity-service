package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.controllers.PlanetController;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.modelmapper.TypeToken;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@WebMvcTest(PlanetController.class)
class PlanetControllerTest extends AbstractWebMvcTest {

    @MockBean
    private PlanetService service;

    @Test
    void testGetList() throws Exception {
        // given
        List<Planet> entityList = List.of(Planet.builder().name("name").build());
        List<PlanetDto> dtoList = modelMapper.map(entityList, new TypeToken<List<PlanetDto>>() {
        }.getType());
        given(service.getList()).willReturn(entityList);
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/planet/get-list")
                .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifySuccessfulResponse(response, dtoList);
        then(service).should().getList();
    }

    @Test
    void testGet() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        Planet entity = Planet.builder().name("name").build();
        PlanetDto dto = modelMapper.map(entity, PlanetDto.class);
        given(service.get(any())).willReturn(entity);
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/planet/get/{id}", id)
                .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifySuccessfulResponse(response, dto);
        then(service).should().get(id);
    }

    @Test
    void testAdd_dirtyFieldFixAndSuccessfulAdd() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        Planet planet = modelMapper.map(planetDto, Planet.class);

        //dirty input
        planetDto.setId(UUID.randomUUID());
        planetDto.setName(" name ");
        planetDto.setVersion(1L);

        given(service.add(any())).willReturn(planet);
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifySuccessfulResponse(response, modelMapper.map(planet, PlanetDto.class));
        then(service).should().add(planet);
    }

    @Test
    void testAdd_validateNullName() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.setName(null);
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validateEmptyName() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.setName("");
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validateBlankName() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.setName(" ");
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validateNullStar() throws Exception {
        //given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.setStar(null);
        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_STAR);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validateNullStarId() throws Exception {
        //given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.getStar().setId(null);
        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_STAR_ID);
        then(service).should(never()).add(any());
    }

    @Test
    void testUpdate_dirtyFieldFixAndSuccessfulUpdate() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        Planet planet = modelMapper.map(planetDto, Planet.class);

        //dirty input
        planetDto.setName(" name ");

        given(service.update(any())).willReturn(planet);
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifySuccessfulResponse(response, modelMapper.map(planet, PlanetDto.class));
        then(service).should().update(planet);
    }

    @Test
    void testUpdate_validateNullName() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setName(null);
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validateEmptyName() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setName("");
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validateBlankName() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setName("    ");
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validateNullId() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setId(null);
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_ID);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validateNullVersion() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setVersion(null);
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_VERSION);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validateNullStar() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setStar(null);
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_STAR);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validateNullStarId() throws Exception {
        // given
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.getStar().setId(null);
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_STAR_ID);
        then(service).should(never()).update(any());
    }

    @Test
    void testDelete() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/planet/delete/{id}", id)
                .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // then
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }

}
