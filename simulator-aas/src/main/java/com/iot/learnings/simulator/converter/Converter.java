package com.iot.learnings.simulator.converter;

@FunctionalInterface
public interface Converter<I, O> {

	O convert(I input);
}
