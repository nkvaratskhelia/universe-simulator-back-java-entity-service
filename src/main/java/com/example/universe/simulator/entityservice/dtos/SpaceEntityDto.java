package com.example.universe.simulator.entityservice.dtos;

import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.utils.Utils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode
@SuperBuilder
abstract class SpaceEntityDto {

    private UUID id;

    private String name;

    private Long version;

    public final void validate(boolean isUpdate) throws AppException {
        validateCommonFields(isUpdate);
        validateDtoFields();

        fixCommonDirtyFields(isUpdate);
        fixDtoDirtyFields();
    }

    private void validateCommonFields(boolean isUpdate) throws AppException {
        if (isUpdate && Objects.isNull(id)) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_ID);
        }
        if (Utils.isNullOrBlank(name)) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_NAME);
        }
        if (isUpdate && Objects.isNull(version)) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_VERSION);
        }
    }

    private void fixCommonDirtyFields(boolean isUpdate) {
        if (!isUpdate) {
            id = null;
            version = null;
        }

        name = name.strip();
    }

    abstract void validateDtoFields() throws AppException;

    abstract void fixDtoDirtyFields();
}
