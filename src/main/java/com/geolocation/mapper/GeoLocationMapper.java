package com.geolocation.mapper;

import com.geolocation.domain.dto.GeolocationDto;
import com.geolocation.domain.entity.Geolocation;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:49 AM
 * @project GeoLocationApp
 */
public class GeoLocationMapper {
    public static GeolocationDto mapToDto(Geolocation geolocation) {
        return GeolocationDto.builder()
                .ipAddress(geolocation.getIpAddress())
                .query(geolocation.getQuery())
                .status(geolocation.getStatus())
                .countryCode(geolocation.getCountryCode())
                .country(geolocation.getCountry())
                .region(geolocation.getRegion())
                .regionName(geolocation.getRegionName())
                .city(geolocation.getCity())
                .timeZone(geolocation.getTimeZone())
                .zip(geolocation.getZip())
                .lat(geolocation.getLat())
                .lon(geolocation.getLon())
                .as(geolocation.getAs())
                .isp(geolocation.getIsp())
                .org(geolocation.getOrg())
                .build();
    }
}
