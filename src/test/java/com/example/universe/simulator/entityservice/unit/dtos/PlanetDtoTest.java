package com.example.universe.simulator.entityservice.unit.dtos;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class PlanetDtoTest {

    @Test
    void testValidateDtoFields_nullStar() {
        //given
        PlanetDto dto = TestUtils.buildPlanetDtoForAdd();
        dto.setStar(null);
        //when
        AppException exception = catchThrowableOfType(() -> dto.validate(false), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_STAR);
    }

    @Test
    void testValidateDtoFields_nullStarId() {
        //given
        PlanetDto dto = TestUtils.buildPlanetDtoForAdd();
        dto.getStar().setId(null);
        //when
        AppException exception = catchThrowableOfType(() -> dto.validate(false), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_STAR_ID);
    }
}
