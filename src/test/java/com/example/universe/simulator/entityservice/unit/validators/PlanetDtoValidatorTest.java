package com.example.universe.simulator.entityservice.unit.validators;

import com.example.universe.simulator.entityservice.common.utils.TestUtils;
import com.example.universe.simulator.entityservice.dtos.PlanetDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;
import com.example.universe.simulator.entityservice.validators.PlanetDtoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@ExtendWith(MockitoExtension.class)
class PlanetDtoValidatorTest {

    @InjectMocks
    private PlanetDtoValidator validator;

    @Test
    void testValidateDtoFields_nullStarId() {
        // given
        PlanetDto dto = TestUtils.buildPlanetDtoForAdd();
        dto.setStarId(null);
        // when
        AppException exception = catchThrowableOfType(() -> validator.validate(dto, false), AppException.class);
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodeType.MISSING_PARAMETER_STAR_ID);
    }
}
