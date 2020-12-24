package com.example.universe.simulator.entityservice.unit;

import com.example.universe.simulator.entityservice.AbstractMockMvcTest;
import com.example.universe.simulator.entityservice.config.BeanConfig;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(BeanConfig.class)
public abstract class AbstractWebMvcTest extends AbstractMockMvcTest {

    @Autowired
    protected ModelMapper modelMapper;
}
