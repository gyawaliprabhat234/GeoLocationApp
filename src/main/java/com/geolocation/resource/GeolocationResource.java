package com.geolocation.resource;

import com.codahale.metrics.annotation.Timed;
import com.geolocation.caching.GeolocationCaching;
import com.geolocation.domain.dto.GeolocationDto;
import com.geolocation.domain.dto.ResponseDto;
import com.geolocation.service.GeolocationService;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:53 AM
 * @project GeoLocationApp
 */

@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
public class GeolocationResource {
    private final GeolocationService geolocationService;
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
