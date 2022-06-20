package com.geolocation.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:45 AM
 * @project GeoLocationApp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeolocationDto {

    private String ipAddress;
    private String query;
    private String status;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private String timeZone;
    private double lat;
    private double lon;
    private String isp;
    private String org;
    private String as;
}

