package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

// Common cases are tested using galaxies.
class CommonIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        // -----------------------------------should throw sort parameter error-----------------------------------

        // when
        MockHttpServletResponse response = performRequest(get("/galaxy/get-list")
            .param("sort", "invalid")
        );
        // then
        verifyErrorResponse(response, ErrorCodeType.INVALID_SORT_PARAMETER);

        // -----------------------------------should throw entity modified error-----------------------------------

        // ====================given====================

        // add entity
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        response = performRequest(post("/galaxy/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );
        GalaxyDto addedDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        // update entity once
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto.getId());
        dto.setName(addedDto.getName() + "Update");

        performRequest(put("/galaxy/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );

        // update entity second time without increasing version
        dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(addedDto.getId());

        // ====================when====================

        response = performRequest(put("/galaxy/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );

        // ====================then====================

        verifyErrorResponse(response, ErrorCodeType.ENTITY_MODIFIED);

        // -----------------------------------should throw not found error-----------------------------------

        // when
        performRequest(delete("/galaxy/delete/{id}", addedDto.getId()));
        response = performRequest(delete("/galaxy/delete/{id}", addedDto.getId()));
        // then
        verifyErrorResponse(response, ErrorCodeType.NOT_FOUND_ENTITY);
    }
}
