package com.geolocation.resource;

import com.codahale.metrics.annotation.Timed;
import com.geolocation.domain.dto.GeolocationDto;
import com.geolocation.domain.dto.ResponseDto;
import com.geolocation.service.GeolocationService;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:53 AM
 * @project GeoLocationApp
 */

@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
public class GeolocationResource {
    private final GeolocationService geolocationService;
    private static Logger log = LoggerFactory.getLogger(GeolocationService.class);
    public GeolocationResource(GeolocationService geolocationService)
    {
        this.geolocationService = geolocationService;
    }

    @GET
    @Timed
    @Path("/")
    @UnitOfWork
    public Response getGeolocation(@Pattern(regexp = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)(\\.(?!$)|$)){4}$",
            message = "ip address is not valid") @NotEmpty @QueryParam("ip") String ipAddress) {
        log.info("\nGET request by passing {} ip address ", ipAddress);
        long startTime = System.currentTimeMillis();
        log.info("============GETTING DATA==================");
        Optional<GeolocationDto> optionalGeolocationDto = geolocationService.getGeoLocation(ipAddress);
        ResponseDto response =  optionalGeolocationDto
                .filter((locationDto)-> locationDto.getStatus().equals("success"))
                .map(
                        (locationDto)->
                                ResponseDto.builder()
                                        .responseData(locationDto)
                                        .success(true).build()
                )
                .orElseGet(()-> ResponseDto.builder().message("Location is not found")
                        .success(false).build());

        log.info("TIME TAKEN : {} ms", System.currentTimeMillis() - startTime);
        log.info("============RESPONSE SENT==================\n");
        return Response.status(200).entity(response).build();
    }
}
