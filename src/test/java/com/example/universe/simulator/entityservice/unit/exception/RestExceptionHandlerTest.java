package com.example.universe.simulator.entityservice.unit.exception;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.controllers.GalaxyController;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.entities.Galaxy;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.services.GalaxyService;
import com.example.universe.simulator.entityservice.specifications.GalaxySpecificationBuilder;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import com.example.universe.simulator.entityservice.unit.AbstractWebMvcTest;
import com.example.universe.simulator.entityservice.validators.GalaxyDtoValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Common exception handlers are tested using GalaxyController.
 */
@WebMvcTest(GalaxyController.class)
class RestExceptionHandlerTest extends AbstractWebMvcTest {

    @MockBean
    private GalaxyService service;

    @MockBean
    private GalaxyDtoValidator validator;

    @MockBean
    private GalaxySpecificationBuilder specificationBuilder;

    @Test
    void testAppException() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        AppException exception = new AppException(ErrorCodeType.IN_USE);
        willThrow(exception).given(service).delete(any());
        // when
        MockHttpServletResponse response = performRequest(delete("/galaxy/delete/{id}", id));
        // then
        verifyErrorResponse(response, exception.getErrorCode());
        then(service).should().delete(id);
    }

    @Test
    void testEmptyResultDataAccessException() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        willThrow(EmptyResultDataAccessException.class).given(service).delete(any());
        // when
        MockHttpServletResponse response = performRequest(delete("/galaxy/delete/{id}", id));
        // then
        verifyErrorResponse(response, ErrorCodeType.NOT_FOUND_ENTITY);
        then(service).should().delete(id);
    }

    @Test
    void testHttpMediaTypeNotSupportedException_missingContentType() throws Exception {
        // given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        // when
        MockHttpServletResponse response = performRequest(post("/galaxy/add")
            .content(objectMapper.writeValueAsString(dto))
        );
        // then
        verifyErrorResponse(response, ErrorCodeType.INVALID_CONTENT_TYPE);
        then(service).shouldHaveNoInteractions();
    }

    @Test
    void testHttpMediaTypeNotSupportedException_invalidContentType() throws Exception {
        // given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        // when
        MockHttpServletResponse response = performRequest(post("/galaxy/add")
            .contentType(MediaType.APPLICATION_XML)
            .content(objectMapper.writeValueAsString(dto))
        );
        // then
        verifyErrorResponse(response, ErrorCodeType.INVALID_CONTENT_TYPE);
        then(service).shouldHaveNoInteractions();
    }

    @Test
    void testHttpMessageNotReadableException() throws Exception {
        // when
        MockHttpServletResponse response = performRequest(post("/galaxy/add"));
        // then
        verifyErrorResponse(response, ErrorCodeType.INVALID_REQUEST_BODY);
        then(service).shouldHaveNoInteractions();
    }

    @Test
    void testHttpRequestMethodNotSupportedException() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        // when
        MockHttpServletResponse response = performRequest(get("/galaxy/delete/{id}", id));
        // then
        verifyErrorResponse(response, ErrorCodeType.INVALID_HTTP_METHOD);
        then(service).shouldHaveNoInteractions();
    }

    @Test
    void testMethodArgumentTypeMismatchException() throws Exception {
        // given
        String id = "id";
        // when
        MockHttpServletResponse response = performRequest(delete("/galaxy/delete/{id}", id));
        // then
        verifyErrorResponse(response, ErrorCodeType.INVALID_REQUEST_PARAMETER);
        then(service).shouldHaveNoInteractions();
    }

    @Test
    void testObjectOptimisticLockingFailureException() throws Exception {
        // given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForUpdate();
        Galaxy entity = modelMapper.map(dto, Galaxy.class);
        given(service.update(any())).willThrow(ObjectOptimisticLockingFailureException.class);
        // when
        MockHttpServletResponse response = performRequest(put("/galaxy/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        );
        // then
        verifyErrorResponse(response, ErrorCodeType.ENTITY_MODIFIED);
        then(service).should().update(entity);
    }

    @Test
    void testPropertyReferenceException() throws Exception {
        // given
        String property = "invalid";
        Sort sort = Sort.by(Sort.Order.asc(property));
        Pageable defaultPageable = TestUtils.getDefaultPageable();
        Pageable pageable = PageRequest.of(defaultPageable.getPageNumber(), defaultPageable.getPageSize(), sort);

        // JUnit cannot construct a proper instance of PropertyReferenceException, so we need to create it ourselves
        PropertyReferenceException exception = new PropertyReferenceException(property, ClassTypeInformation.from(Galaxy.class), List.of());
        given(service.getList(any(), any())).willThrow(exception);
        // when
        MockHttpServletResponse response = performRequest(post("/galaxy/get-list")
            .param("sort", property)
        );
        // then
        verifyErrorResponse(response, ErrorCodeType.INVALID_SORT_PARAMETER);
        then(service).should().getList(null, pageable);
    }

    @Test
    void testUnknownException() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        willThrow(RuntimeException.class).given(service).delete(any());
        // when
        MockHttpServletResponse response = performRequest(delete("/galaxy/delete/{id}", id));
        // then
        verifyErrorResponse(response, ErrorCodeType.SERVER_ERROR);
        then(service).should().delete(id);
    }
}
