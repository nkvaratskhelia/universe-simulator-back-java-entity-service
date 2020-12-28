package com.example.universe.simulator.entityservice.integration;

import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.utils.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class StarIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() throws Exception {
        //-----------------------------------should return empty list-----------------------------------

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/star/get-list")).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        List<StarDto> resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).isEmpty();

        //-----------------------------------should throw not found error-----------------------------------

        //given
        UUID nonexistentId = UUID.randomUUID();
        //when
        response = mockMvc.perform(get("/star/get/{id}", nonexistentId)).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----------------------------------should add galaxy-----------------------------------

        //given
        GalaxyDto galaxyDto = TestUtils.buildGalaxyDtoForAdd();
        //when
        response = mockMvc.perform(post("/galaxy/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(galaxyDto))
        ).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        GalaxyDto addedGalaxy = objectMapper.readValue(response.getContentAsString(), GalaxyDto.class);

        //-----------------------------------should throw missing name error when null name-----------------------------------

        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.setName(null);
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing name error when empty name-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForAdd();
        dto.setName("");
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing name error when blank name-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForAdd();
        dto.setName(" ");
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing galaxy error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForAdd();
        dto.setGalaxy(null);
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_GALAXY);

        //-----------------------------------should throw missing galaxy id error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForAdd();
        dto.getGalaxy().setId(null);
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_GALAXY_ID);

        //-----------------------------------should throw galaxy not found error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForAdd();
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.GALAXY_NOT_FOUND);

        //-----------------------------------should fix dirty fields and add entity-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForAdd();
        dto.getGalaxy().setId(addedGalaxy.getId());

        //dirty input
        UUID dirtyId = UUID.randomUUID();
        dto.setId(dirtyId);
        dto.setName(" name1 ");
        dto.setVersion(1L);

        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        StarDto addedDto1 = objectMapper.readValue(response.getContentAsString(), StarDto.class);
        assertThat(addedDto1.getId()).isNotEqualTo(dirtyId);
        assertThat(addedDto1.getName()).isEqualTo("name1");
        assertThat(addedDto1.getVersion()).isEqualTo(0);
        assertThat(addedDto1.getGalaxy().getId()).isEqualTo(addedGalaxy.getId());

        //-----------------------------------should return list with 1 element-----------------------------------

        //when
        response = mockMvc.perform(get("/star/get-list")).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).hasSize(1);

        //-----------------------------------should return entity-----------------------------------

        //when
        response = mockMvc.perform(get("/star/get/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        StarDto resultDto = objectMapper.readValue(response.getContentAsString(), StarDto.class);
        assertThat(resultDto).isEqualTo(addedDto1);
        assertThat(resultDto.getGalaxy().getId()).isEqualTo(addedGalaxy.getId());

        //-----------------------------------should throw name exists error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForAdd();
        dto.setName(addedDto1.getName());
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.EXISTS_NAME);

        //-----------------------------------should add entity-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForAdd();
        dto.setName("name2");
        dto.getGalaxy().setId(addedGalaxy.getId());
        //when
        response = mockMvc.perform(post("/star/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        StarDto addedDto2 = objectMapper.readValue(response.getContentAsString(), StarDto.class);
        assertThat(addedDto2.getName()).isEqualTo("name2");

        //-----------------------------------should return list with 2 elements-----------------------------------

        //when
        response = mockMvc.perform(get("/star/get-list")).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).hasSize(2);

        //-----------------------------------should throw missing id error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(null);
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_ID);

        //-----------------------------------should throw missing name error when null name-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setName(null);
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing name error when empty name-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setName("");
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing name error when blank name-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setName(" ");
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_NAME);

        //-----------------------------------should throw missing version error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setVersion(null);
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_VERSION);

        //-----------------------------------should throw missing galaxy error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setGalaxy(null);
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_GALAXY);

        //-----------------------------------should throw missing galaxy id error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.getGalaxy().setId(null);
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.MISSING_PARAMETER_GALAXY_ID);

        //-----------------------------------should throw not found error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----------------------------------should throw name exists error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.setName(addedDto2.getName());
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.EXISTS_NAME);

        //-----------------------------------should throw galaxy not found error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(addedDto1.getId());
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.GALAXY_NOT_FOUND);

        //-----------------------------------should fix dirty fields and update entity-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.getGalaxy().setId(addedGalaxy.getId());

        //dirty input
        dto.setName(" name1Update ");

        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        StarDto updatedDto = objectMapper.readValue(response.getContentAsString(), StarDto.class);
        assertThat(updatedDto.getId()).isEqualTo(addedDto1.getId());
        assertThat(updatedDto.getName()).isEqualTo("name1Update");
        assertThat(updatedDto.getVersion()).isEqualTo(1);
        assertThat(updatedDto.getGalaxy().getId()).isEqualTo(addedGalaxy.getId());

        //-----------------------------------should throw entity modified error-----------------------------------

        //given
        dto = TestUtils.buildStarDtoForUpdate();
        dto.setId(addedDto1.getId());
        dto.getGalaxy().setId(addedGalaxy.getId());
        //when
        response = mockMvc.perform(put("/star/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_MODIFIED);

        //-----------------------------------should throw not found error-----------------------------------

        //when
        response = mockMvc.perform(delete("/star/delete/{id}", nonexistentId)).andReturn().getResponse();
        //then
        verifyErrorResponse(response.getContentAsString(), ErrorCodeType.ENTITY_NOT_FOUND);

        //-----------------------------------should delete entity-----------------------------------

        //when
        response = mockMvc.perform(delete("/star/delete/{id}", addedDto1.getId())).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());

        //-----------------------------------should return list with 1 element-----------------------------------

        //when
        response = mockMvc.perform(get("/star/get-list")).andReturn().getResponse();
        //then
        verifyOkStatus(response.getStatus());
        resultList = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertThat(resultList).hasSize(1);
    }
}
