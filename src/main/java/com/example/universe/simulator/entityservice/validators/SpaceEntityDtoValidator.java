package com.example.universe.simulator.entityservice.validators;

import com.example.universe.simulator.entityservice.dtos.SpaceEntityDto;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.exception.ErrorCodeType;
import com.example.universe.simulator.entityservice.utils.Utils;

import java.util.Objects;

abstract class SpaceEntityDtoValidator<T extends SpaceEntityDto> {

    public final void validate(T dto, boolean isUpdate) throws AppException {
        validateCommonFields(dto, isUpdate);
        validateDtoFields(dto);

        fixCommonDirtyFields(dto, isUpdate);
        fixDtoDirtyFields(dto);
    }

    private void validateCommonFields(T dto, boolean isUpdate) throws AppException {
        if (isUpdate && Objects.isNull(dto.getId())) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_ID);
        }
        if (Utils.isNullOrBlank(dto.getName())) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_NAME);
        }
        if (isUpdate && Objects.isNull(dto.getVersion())) {
            throw new AppException(ErrorCodeType.MISSING_PARAMETER_VERSION);
        }
    }

    private void fixCommonDirtyFields(T dto, boolean isUpdate) {
        if (!isUpdate) {
            dto.setId(null);
            dto.setVersion(null);
        }

        dto.setName(dto.getName().strip());
    }

    abstract void validateDtoFields(T dto) throws AppException;

    abstract void fixDtoDirtyFields(T dto);
}
