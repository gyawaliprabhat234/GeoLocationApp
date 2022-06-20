package com.geolocation.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:49 AM
 * @project GeoLocationApp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Geolocation {
    @Id
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
    @Column(name= "_as")
    private String as;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedTime;
}
