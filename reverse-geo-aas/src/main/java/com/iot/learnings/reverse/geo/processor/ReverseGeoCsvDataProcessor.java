package com.iot.learnings.reverse.geo.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.iot.learnings.reverse.geo.model.ReverseGeoCsvData;

public class ReverseGeoCsvDataProcessor implements ItemProcessor<ReverseGeoCsvData, ReverseGeoCsvData> {

	private static final Logger LOG = LoggerFactory.getLogger(ReverseGeoCsvDataProcessor.class);

	@Override
	public ReverseGeoCsvData process(ReverseGeoCsvData item) throws Exception {
		if (item.getPopulation() == null) {
			LOG.info("Skipped => ReverseGeoCsvData: {}", item);
			return null;
		}
		return item;
	}
}
