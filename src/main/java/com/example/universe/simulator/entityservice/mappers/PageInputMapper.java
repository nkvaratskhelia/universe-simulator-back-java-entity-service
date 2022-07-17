package com.example.universe.simulator.entityservice.mappers;

import com.example.universe.simulator.entityservice.inputs.PageInput;
import org.mapstruct.Mapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Mapper(config = MappingConfig.class)
public interface PageInputMapper {
    default Pageable toPageable(PageInput pageInput) {
        Sort sort = Sort.by(pageInput.sort().stream()
            .map(s -> new Sort.Order(s.direction(), s.property()))
            .toList());
        return PageRequest.of(pageInput.page(), pageInput.size(), sort);
    }
}
