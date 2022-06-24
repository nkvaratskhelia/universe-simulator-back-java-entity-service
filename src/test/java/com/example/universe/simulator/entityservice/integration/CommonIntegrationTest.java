package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

// Common cases are tested using galaxies.
class CommonIntegrationTest extends AbstractIntegrationTest {

    @Test
    void testInvalidSortParameter() throws Exception {
        // when
        MockHttpServletResponse response = performRequest(get("/galaxy/get-list")
            .param("sort", "invalid")
        );
        // then
        verifyErrorResponse(response, ErrorCodeType.INVALID_SORT_PARAMETER);
    }

    @Test
    void testOptimisticLocking() throws Exception {
        // -----------------------------------given-----------------------------------

        // add entity
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        MockHttpServletResponse response = performRequest(post("/galaxy/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );
        GalaxyDto addedDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        // update entity once
        addedDto.setName(addedDto.getName() + "Update1");
        performRequest(put("/galaxy/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(addedDto))
        );

        // -----------------------------------when-----------------------------------

        // update entity second time without increasing version
        addedDto.setName(addedDto.getName() + "Update2");
        response = performRequest(put("/galaxy/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(addedDto))
        );

        // -----------------------------------then-----------------------------------

        verifyErrorResponse(response, ErrorCodeType.ENTITY_MODIFIED);

        // cleanup
        performRequest(delete("/galaxy/delete/{id}", addedDto.getId()));
    }

    @Test
    void testDeleteNonexistentEntity() throws Exception {
        // when
        MockHttpServletResponse response = performRequest(delete("/galaxy/delete/{id}", UUID.randomUUID()));
        // then
        verifyErrorResponse(response, ErrorCodeType.NOT_FOUND_ENTITY);
    }
}
