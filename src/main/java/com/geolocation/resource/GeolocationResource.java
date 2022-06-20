package com.geolocation.resource;

import com.codahale.metrics.annotation.Timed;
import com.geolocation.caching.GeolocationCaching;
import com.geolocation.domain.dto.GeolocationDto;
import com.geolocation.domain.dto.ResponseDto;
import com.geolocation.service.GeolocationService;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;

import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:53 AM
 * @project GeoLocationApp
 */

@Path("/geolocation")
@Produces(MediaType.APPLICATION_JSON)
public class GeolocationResource {
    private final GeolocationService geolocationService;
    public GeolocationResource(GeolocationService geolocationService)
    {
        this.geolocationService = geolocationService;
    }

    @GET
    @Timed
    @Path("/{ipAddress}")
    @UnitOfWork
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.MINUTES)
    public Response getGeolocation(@Pattern(regexp = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)(\\.(?!$)|$)){4}$",
            message = "ip address is not valid") @PathParam("ipAddress") String ipAddress) {
        Optional<GeolocationDto> optionalGeolocationDto = GeolocationCaching.getInstance().getGeolocationDataFromCache(ipAddress);;
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
        return Response.status(200).entity(response).build();
    }
}
