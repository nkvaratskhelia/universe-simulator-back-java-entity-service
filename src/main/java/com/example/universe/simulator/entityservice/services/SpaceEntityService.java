package com.example.universe.simulator.entityservice.services;

import com.example.universe.simulator.entityservice.entities.SpaceEntity;
import com.example.universe.simulator.entityservice.exception.AppException;
import com.example.universe.simulator.entityservice.repositories.SpaceEntityRepository;
import com.example.universe.simulator.entityservice.types.ErrorCodeType;

abstract class SpaceEntityService<T extends SpaceEntity> {

    void validate(T entity, boolean isUpdate, SpaceEntityRepository<T> repository) throws AppException {
        validateCommonRules(entity, isUpdate, repository);
        validateEntity(entity, isUpdate);
    }

    private void validateCommonRules(T entity, boolean isUpdate, SpaceEntityRepository<T> repository) throws AppException {
        if (isUpdate && !repository.existsById(entity.getId())) {
            throw new AppException(ErrorCodeType.NOT_FOUND_ENTITY);
        }

        boolean existsByName = isUpdate
            ? repository.existsByNameAndIdNot(entity.getName(), entity.getId())
            : repository.existsByName(entity.getName());
        if (existsByName) {
            throw new AppException(ErrorCodeType.EXISTS_NAME);
        }
    }

    abstract void validateEntity(T entity, boolean isUpdate) throws AppException;
}
