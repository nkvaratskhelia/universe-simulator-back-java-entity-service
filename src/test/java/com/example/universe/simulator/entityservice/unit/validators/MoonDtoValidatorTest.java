package com.example.universe.simulator.entityservice.unit.validators;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.MoonDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import com.example.universe.simulator.entityservice.validators.MoonDtoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@ExtendWith(MockitoExtension.class)
class MoonDtoValidatorTest {

    @InjectMocks
    private MoonDtoValidator validator;

    @Test
    void testValidateDtoFields_nullPlanetId() {
        // given
        MoonDto dto = TestUtils.buildMoonDtoForAdd();
        dto.setPlanetId(null);
        // when
        AppException exception = catchThrowableOfType(() -> validator.validate(dto, false), AppException.class);
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_PLANET_ID);
    }
}
