package com.iot.learnings.socket.converter;

public interface Converter<I, O> {

	O convert(I input);
}
