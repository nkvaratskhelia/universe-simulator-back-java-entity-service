package com.example.universe.simulator.entityservice.unit.dtos;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class MoonDtoTest {

    @Test
    void testValidateDtoFields_nullPlanet() {
        //given
        MoonDto dto = TestUtils.buildMoonDtoForAdd();
        dto.setPlanet(null);
        //when
        AppException exception = catchThrowableOfType(() -> dto.validate(false), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_PLANET);
    }

    @Test
    void testValidateDtoFields_nullPlanetId() {
        //given
        MoonDto dto = TestUtils.buildMoonDtoForAdd();
        dto.getPlanet().setId(null);
        //when
        AppException exception = catchThrowableOfType(() -> dto.validate(false), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_PLANET_ID);
    }
}
