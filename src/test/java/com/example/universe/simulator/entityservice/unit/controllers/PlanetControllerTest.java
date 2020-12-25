package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.controllers.PlanetController;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.exception.AppException;
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
    void testGet() throws Exception {
        // arrange
        UUID id = UUID.randomUUID();
        Planet planet = Planet.builder().name("name").build();
        PlanetDto planetDto = modelMapper.map(planet, PlanetDto.class);
        given(service.get(id)).willReturn(planet);
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/planet/get/{id}", id)
                .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifySuccessfulResponse(response, planetDto);
        then(service).should().get(id);
    }

    //@Test
    //void testGet_entityNotFound() throws Exception {
    //    // arrange
    //    UUID id = UUID.randomUUID();
    //    given(service.get(id)).willThrow(new AppException(ErrorCodeType.ENTITY_NOT_FOUND));
    //    // act
    //    RequestBuilder requestBuilder = MockMvcRequestBuilders
    //            .get("/planet/get/{id}", id)
    //            .accept(MediaType.APPLICATION_JSON);
    //    MockHttpServletResponse response = mockMvc
    //            .perform(requestBuilder)
    //            .andReturn()
    //            .getResponse();
    //    // assert
    //    verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);
    //    then(service).should().get(id);
    //}

    @Test
    void testGetList() throws Exception {
        // arrange
        List<Planet> planets = List.of(Planet.builder().name("name").build());
        List<PlanetDto> planetDtos = modelMapper.map(planets, new TypeToken<List<PlanetDto>>() {
        }.getType());
        given(service.getList()).willReturn(planets);
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/planet/get-list")
                .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifySuccessfulResponse(response, planetDtos);
        then(service).should().getList();
    }

    @Test
    void testAdd() throws Exception {
        // arrange
        Planet planet = Planet.builder().name("name").build();
        PlanetDto planetDto = modelMapper.map(planet, PlanetDto.class);
        given(service.add(planet)).willReturn(planet);
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifySuccessfulResponse(response, planetDto);
        then(service).should().add(planet);
    }

    @Test
    void testAdd_validateNullName() throws Exception {
        // arrange
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.setName(null);
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validateEmptyName() throws Exception {
        // arrange
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.setName("");
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testAdd_validateBlankName() throws Exception {
        // arrange
        PlanetDto planetDto = TestUtils.buildPlanetDtoForAdd();
        planetDto.setName(" ");
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).add(any());
    }

    @Test
    void testUpdate() throws Exception {
        // arrange
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        Planet planet = modelMapper.map(planetDto, Planet.class);
        given(service.update(planet)).willReturn(planet);
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifySuccessfulResponse(response, planetDto);
        then(service).should().update(planet);
    }

    @Test
    void testUpdate_validateNullName() throws Exception {
        // arrange
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setName(null);
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validateEmptyName() throws Exception {
        // arrange
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setName("");
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validateBlankName() throws Exception {
        // arrange
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setName("    ");
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validateNullId() throws Exception {
        // arrange
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setId(null);
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_ID);
        then(service).should(never()).update(any());
    }

    @Test
    void testUpdate_validateNullVersion() throws Exception {
        // arrange
        PlanetDto planetDto = TestUtils.buildPlanetDtoForUpdate();
        planetDto.setVersion(null);
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/planet/update")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDto));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_VERSION);
        then(service).should(never()).update(any());
    }

    @Test
    void testDelete() throws Exception {
        // arrange
        UUID id = UUID.randomUUID();
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/planet/delete/{id}", id)
                .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifyOkStatus(response.getStatus());
        then(service).should().delete(id);
    }

}
