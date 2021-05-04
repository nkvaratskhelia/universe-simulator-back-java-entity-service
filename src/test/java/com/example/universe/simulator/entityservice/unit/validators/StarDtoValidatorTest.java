package com.example.universe.simulator.entityservice.unit.validators;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.StarDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.validators.StarDtoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@ExtendWith(MockitoExtension.class)
class StarDtoValidatorTest {

    @InjectMocks
    private StarDtoValidator validator;

    @Test
    void testValidateDtoFields_nullGalaxy() {
        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.setGalaxy(null);
        //when
        AppException exception = catchThrowableOfType(() -> validator.validate(dto, false), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_GALAXY);
    }

    @Test
    void testValidateDtoFields_nullGalaxyId() {
        //given
        StarDto dto = TestUtils.buildStarDtoForAdd();
        dto.getGalaxy().setId(null);
        //when
        AppException exception = catchThrowableOfType(() -> validator.validate(dto, false), AppException.class);
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_GALAXY_ID);
    }
}
