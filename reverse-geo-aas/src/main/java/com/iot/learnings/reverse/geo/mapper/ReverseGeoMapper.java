package com.iot.learnings.reverse.geo.mapper;

import org.mapstruct.Mapper;

import com.iot.learnings.dto.ReverseGeoRequestDTO;
import com.iot.learnings.dto.ReverseGeoResponseDTO;
import com.iot.learnings.reverse.geo.model.ReverseGeo;
import com.iot.learnings.reverse.geo.model.ReverseGeoCsvData;

@Mapper(componentModel = "spring")
public interface ReverseGeoMapper {

	ReverseGeo mapDTOToDomain(ReverseGeoRequestDTO requestDTO);

	ReverseGeo mapCsvRecordToDomain(ReverseGeoCsvData reverseGeoCsvData);

	ReverseGeoResponseDTO mapDomainToDTO(ReverseGeo reverseGeo);
}
