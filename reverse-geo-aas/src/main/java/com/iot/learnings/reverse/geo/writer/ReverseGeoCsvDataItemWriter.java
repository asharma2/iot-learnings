package com.iot.learnings.reverse.geo.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.iot.learnings.reverse.geo.mapper.ReverseGeoMapper;
import com.iot.learnings.reverse.geo.model.ReverseGeo;
import com.iot.learnings.reverse.geo.model.ReverseGeoCsvData;
import com.iot.learnings.reverse.geo.service.ReverseGeoService;

public class ReverseGeoCsvDataItemWriter implements ItemWriter<ReverseGeoCsvData> {

	private static final Logger LOG = LoggerFactory.getLogger(ReverseGeoCsvDataItemWriter.class);

	@Autowired
	ReverseGeoMapper reverseGeoMapper;
	@Autowired
	ReverseGeoService reverseGeoService;

	@Override
	public void write(List<? extends ReverseGeoCsvData> items) throws Exception {
		long s = System.currentTimeMillis();
		for (ReverseGeoCsvData csvData : items) {
			ReverseGeo revGeo = reverseGeoMapper.mapCsvRecordToDomain(csvData);
			reverseGeoService.save(revGeo);
		}
		LOG.info("Writer => Records: {}, Time: {}", items.size(), (System.currentTimeMillis() - s));
	}
}
