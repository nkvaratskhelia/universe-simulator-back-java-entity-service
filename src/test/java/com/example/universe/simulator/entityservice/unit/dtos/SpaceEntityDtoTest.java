package com.example.universe.simulator.entityservice.unit.dtos;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.GalaxyDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

/**
 * Common space entity validation is tested using GalaxyDto.
 */
class SpaceEntityDtoTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void testValidateCommonFields_add_invalidName(String name) {
        //given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setName(name);
        //when
        AppException exception = catchThrowableOfType(() -> dto.validate(false), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_NAME);
    }

    @Test
    void testValidateCommonFields_update_nullId() {
        //given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setId(null);
        //when
        AppException exception = catchThrowableOfType(() -> dto.validate(true), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_ID);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void testValidateCommonFields_update_invalidName(String name) {
        //given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setName(name);
        //when
        AppException exception = catchThrowableOfType(() -> dto.validate(true), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_NAME);
    }

    @Test
    void testValidateCommonFields_update_nullVersion() {
        //given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setVersion(null);
        //when
        AppException exception = catchThrowableOfType(() -> dto.validate(true), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_VERSION);
    }

    @Test
    void testFixCommonDirtyFields_add() throws AppException {
        //given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForAdd();
        dto.setId(UUID.randomUUID());
        dto.setName(" name ");
        dto.setVersion(1L);
        //when
        dto.validate(false);
        //then
        assertThat(dto.getId()).isNull();
        assertThat(dto.getName()).isEqualTo("name");
        assertThat(dto.getVersion()).isNull();
    }

    @Test
    void testFixCommonDirtyFields_update() throws AppException {
        //given
        GalaxyDto dto = TestUtils.buildGalaxyDtoForUpdate();
        dto.setName(" name ");
        //when
        dto.validate(true);
        //then
        assertThat(dto.getName()).isEqualTo("name");
    }
}
