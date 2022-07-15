package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.inputs.UpdateGalaxyInput;
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
        MockHttpServletResponse response = performRequest(get("/galaxies")
            .param("sort", sortProperty)
        );

        // then
        verifyErrorResponse(response, ErrorCodeType.INVALID_SORT_PROPERTY);
    }

    @Test
    void testOptimisticLocking() throws Exception {
        // given

        // add entity
        MockHttpServletResponse response = performRequestWithBody(post("/galaxies"),
            TestUtils.buildAddGalaxyInput()
        );
        GalaxyDto dto = readResponse(response, GalaxyDto.class);

        // update entity once
        performRequestWithBody(put("/galaxies"),
            new UpdateGalaxyInput(dto.getId(), dto.getVersion(), dto.getName() + "Update1")
        );

        // when

        // update entity second time without increasing version
        response = performRequestWithBody(put("/galaxies"),
            new UpdateGalaxyInput(dto.getId(), dto.getVersion(), dto.getName() + "Update2")
        );

        // then
        verifyErrorResponse(response, ErrorCodeType.ENTITY_MODIFIED);

        // cleanup
        performRequest(delete("/galaxies/{id}", dto.getId()));
    }

    @Test
    void testDeleteNonexistentEntity() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // when
        MockHttpServletResponse response = performRequest(delete("/galaxies/{id}", id));

        // then
        verifyErrorResponse(response, ErrorCodeType.NOT_FOUND_ENTITY);
    }
}
