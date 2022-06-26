package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

// Common cases are tested using galaxies.
class CommonIntegrationTest extends AbstractIntegrationTest {

    @Test
    void testInvalidSortProperty() throws Exception {
        // given
        var sortProperty = "invalid";

        // when
        MockHttpServletResponse response = performRequest(get("/galaxy/get-list")
            .param("sort", sortProperty)
        );

        // then
        verifyErrorResponse(response, ErrorCodeType.INVALID_SORT_PROPERTY);
    }

    @Test
    void testOptimisticLocking() throws Exception {
        // ----------------------------------------given----------------------------------------

        // add entity
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        MockHttpServletResponse response = performRequestWithBody(post("/galaxy/add"), dto);
        GalaxyDto addedDto = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        // update entity once
        addedDto.setName(addedDto.getName() + "Update1");
        performRequestWithBody(put("/galaxy/update"), addedDto);

        addedDto.setName(addedDto.getName() + "Update2");

        // ----------------------------------------when----------------------------------------

        // update entity second time without increasing version
        response = performRequestWithBody(put("/galaxy/update"), addedDto);

        // ----------------------------------------then----------------------------------------

        verifyErrorResponse(response, ErrorCodeType.ENTITY_MODIFIED);

        // ----------------------------------------cleanup----------------------------------------

        performRequest(delete("/galaxy/delete/{id}", addedDto.getId()));
    }

    @Test
    void testDeleteNonexistentEntity() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // when
        MockHttpServletResponse response = performRequest(delete("/galaxy/delete/{id}", id));

        // then
        verifyErrorResponse(response, ErrorCodeType.NOT_FOUND_ENTITY);
    }
}
