package com.geolocation.service;

import com.geolocation.domain.dto.GeolocationDto;
import com.geolocation.domain.entity.Geolocation;
import com.geolocation.exception.RecordNotFoundException;
import com.geolocation.mapper.GeoLocationMapper;
import com.geolocation.repository.GeolocationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:46 AM
 * @project GeoLocationApp
 */
public class GeolocationService{


    private final GeolocationDAO geolocationDAO;
    private Client restClient;
    private static Logger log = LoggerFactory.getLogger(GeolocationService.class);
    private final String getURL;
    public GeolocationService(GeolocationDAO geolocationDAO, Client restClient, String geoApiUrl){
        this.geolocationDAO = geolocationDAO;
        this.restClient = restClient;
        this.getURL = geoApiUrl;
    }

    public Optional<GeolocationDto> getGeoDataFromAPI(String ipAddress){
        Geolocation geoLocation;
        try{
            log.info("Ip address Searching in Database: "+ipAddress);
            geoLocation = getGeoDataFromDB(ipAddress);
            if(LocalDateTime.now().minusMinutes(5).isAfter(geoLocation.getLastUpdatedTime())){
                log.info("reloading the data for {}", ipAddress);
                geoLocation =  getGeoDataFromApiCall(ipAddress);
                //Removing the previous record from the database.
                geolocationDAO.delete(ipAddress);
                //saving the new record
                saveGeoData(geoLocation);
            }
        }
        catch (RecordNotFoundException c){
            log.info("making api call to search {}: ", ipAddress);
            geoLocation = getGeoDataFromApiCall(ipAddress);
            if(geoLocation!=null && geoLocation.getStatus().equalsIgnoreCase("success")){
                saveGeoData(geoLocation);
            }
        }
        return Optional.ofNullable(geoLocation).map(location -> GeoLocationMapper.mapToDto(location));
    }

    private void saveGeoData(Geolocation geolocation){
        log.info("save geolocation data in database with ip address: "+geolocation.getIpAddress());
        geolocationDAO.save(geolocation);
    }

    private Geolocation getGeoDataFromApiCall(String ipAddress){
        Geolocation  geolocation =  restClient.target(getURL+ipAddress)
                .request().get()
                .readEntity(Geolocation.class);
        geolocation.setIpAddress(ipAddress);
        return geolocation;
    }

    private Geolocation getGeoDataFromDB (String ipAddress){
        log.info("Searching for {} in database: "+ipAddress);
        Geolocation geolocationOpt = geolocationDAO.findByIpAddress(ipAddress);
        return Optional.ofNullable(geolocationOpt).orElseThrow(()-> {
            log.info("Record of {} is not found in database", ipAddress);
            return  new RecordNotFoundException("Ip Address is not found");
        });
    }
}
