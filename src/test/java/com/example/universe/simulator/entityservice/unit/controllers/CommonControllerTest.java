package com.example.universe.simulator.entityservice.unit.controllers;

import com.example.universe.simulator.entityservice.controllers.GalaxyController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Common controller cases are tested using GalaxyController.
 */
@WebMvcTest(GalaxyController.class)
class CommonControllerTest extends AbstractWebMvcTest {

    @MockBean
    private GalaxyService service;

    @Test
    void testGetList_defaultPageable() throws Exception {
        //given
        List<Galaxy> entityList = List.of(
                Galaxy.builder().name("name").build()
        );

        Pageable pageable = TestUtils.getDefaultPageable();
        Page<Galaxy> entityPage = new PageImpl<>(entityList, pageable, entityList.size());
        Page<GalaxyDto> dtoPage = entityPage.map(item -> modelMapper.map(item, GalaxyDto.class));

        given(service.getList(any(), any())).willReturn(entityPage);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/galaxy/get-list")).andReturn().getResponse();
        //then
        verifySuccessfulResponse(response, dtoPage);
        then(service).should().getList(null, pageable);
    }
}
