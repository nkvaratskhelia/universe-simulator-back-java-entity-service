package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.controllers.PlanetController;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.entities.Planet;
import com.example.universe.simulator.entityservice.services.PlanetService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.TypeToken;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@WebMvcTest(PlanetController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlanetControllerTest extends AbstractWebMvcTest {

    @MockBean
    private PlanetService service;

    private UUID id;
    private Planet planet;
    private List<Planet> planets;
    private PlanetDto planetDto;
    private List<PlanetDto> planetDtos;
    private Planet planetForAdd;
    private PlanetDto planetDtoForAdd;

    @BeforeAll
    void setUpContext() {
        id = UUID.randomUUID();
        planet = Planet.builder().id(id).name("name").version(0L).build();
        planets = List.of(planet);
        planetDto = modelMapper.map(planet, PlanetDto.class);
        planetDtos = modelMapper.map(
                planets,
                new TypeToken<List<PlanetDto>>() {
                }.getType()
        );
        planetForAdd = Planet.builder().name("name").build();
        planetDtoForAdd = modelMapper.map(planetForAdd, PlanetDto.class);
    }

    @Test
    void testGet() throws Exception {
        // arrange
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
        given(service.add(planetForAdd)).willReturn(planetForAdd);
        // act
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/planet/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planetDtoForAdd));
        MockHttpServletResponse response = mockMvc
                .perform(requestBuilder)
                .andReturn()
                .getResponse();
        // assert
        verifySuccessfulResponse(response, planetDtoForAdd);
        then(service).should().add(planetForAdd);
    }

    @Test
    void testUpdate() throws Exception {
        // arrange
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
    void testDelete() throws Exception {
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
