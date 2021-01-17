package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.filters.StarFilter;
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

class StarIntegrationTest extends AbstractIntegrationTest {

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

        //-----------------------------------should throw sort parameter error-----------------------------------

        //when
        response = performRequest(post("/star/get-list")
                .param("sort", "invalid")
        );
        //then
        verifyErrorResponse(response, ErrorCodeType.INVALID_SORT_PARAMETER);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = performRequest(post("/star/get-list"));
        //then
        JsonPage<StarDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        //-----------------------------------should add entity-----------------------------------

        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.setName("name1");
        dto.getGalaxy().setId(addedGalaxy.getId());
        //when
        response = performRequest(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        StarDto addedDto1 = objectMapper.readValue(response.getContentAsString(), StarDto.class);

        //-----------------------------------should add entity-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForAdd();
        dto.setName("name2");
        dto.getGalaxy().setId(addedGalaxy.getId());
        //when
        response = performRequest(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        StarDto addedDto2 = objectMapper.readValue(response.getContentAsString(), StarDto.class);

        //-----------------------------------should return list with 2 elements-----------------------------------

        //when
        response = performRequest(post("/star/get-list"));
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(2);

        //-----------------------------------should return entity-----------------------------------

        //when
        response = performRequest(get("/star/get/{id}", addedDto1.getId()));
        //then
        StarDto resultDto = objectMapper.readValue(response.getContentAsString(), StarDto.class);
        assertThat(resultDto).isEqualTo(addedDto1);
        assertThat(resultDto.getGalaxy().getId()).isEqualTo(addedGalaxy.getId());

        //-----------------------------------should update entity-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName("name1Update");
        dto.getGalaxy().setId(addedGalaxy.getId());

        //when
        response = performRequest(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        StarDto updatedDto = objectMapper.readValue(response.getContentAsString(), StarDto.class);
        assertThat(updatedDto.getName()).isEqualTo(dto.getName());
        assertThat(updatedDto.getVersion()).isEqualTo(dto.getVersion() + 1);

        //-----------------------------------should throw entity modified error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.getGalaxy().setId(addedGalaxy.getId());
        //when
        response = performRequest(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        );
        //then
        verifyErrorResponse(response, ErrorCodeType.ENTITY_MODIFIED);

        //-----------------------------------should return list with 1 element-----------------------------------

        //given
        StarFilter filter = StarFilter.builder().name("1uP").build();
        //when
        response = performRequest(post("/star/get-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter))
        );
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).hasSize(1);

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = performRequest(delete("/star/delete/{id}", addedDto1.getId()));
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = performRequest(delete("/star/delete/{id}", addedDto2.getId()));
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should throw not found error-----------------------------------

        //when
        response = performRequest(delete("/star/delete/{id}", addedDto1.getId()));
        //then
        verifyErrorResponse(response, ErrorCodeType.NOT_FOUND_ENTITY);

        //-----------------------------------should return empty list-----------------------------------

        //when
        response = performRequest(post("/star/get-list"));
        //then
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList.getContent()).isEmpty();

        //-----------------------------------should delete galaxy-----------------------------------

        //when
        response = performRequest(delete("/galaxy/delete/{id}", addedGalaxy.getId()));
        //then
        verifyOkStatus(response.getStatus());
    }
}
