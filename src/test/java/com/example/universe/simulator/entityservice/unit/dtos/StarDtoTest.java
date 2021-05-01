package com.example.universe.simulator.entityservice.unit.dtos;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class StarDtoTest {

    @Test
    void testValidateDtoFields_nullGalaxy() {
        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.setGalaxy(null);
        //when
        AppException exception = catchThrowableOfType(() -> dto.validate(false), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_GALAXY);
    }

    @Test
    void testValidateDtoFields_nullGalaxyId() {
        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.getGalaxy().setId(null);
        //when
        AppException exception = catchThrowableOfType(() -> dto.validate(false), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_GALAXY_ID);
    }
}
