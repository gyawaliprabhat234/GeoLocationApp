package com.geolocation.caching;

import com.geolocation.domain.dto.GeolocationDto;
import com.geolocation.service.GeolocationService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:43 AM
 * @project GeoLocationApp
 */
public class GeolocationCaching {
    private static final Logger logger = LoggerFactory.getLogger(GeolocationCaching.class);
    private static GeolocationCaching geolocationCaching;

    public static GeolocationCaching getInstance() {
        geolocationCaching = Optional.ofNullable(geolocationCaching).orElseGet( GeolocationCaching::new);
        return geolocationCaching;
    }

    private static LoadingCache<String, GeolocationDto> locationCache;

    public void initializeCache(GeolocationService geolocationService) {
        if (locationCache == null) {
            locationCache = CacheBuilder.newBuilder()
                    .concurrencyLevel(20)
                    .maximumSize(2000)
                    .expireAfterAccess(10, TimeUnit.SECONDS)
                    .recordStats()
                    .build(new CacheLoader<String, GeolocationDto>() { // Build the CacheLoader
                        @Override
                        public GeolocationDto load(String ipAddress) throws Exception {
                            logger.info("Fetching Geolocation Data from DB/ Cache");
                            return geolocationService.getGeoDataFromAPI(ipAddress)
                                    .orElse(null);
                        }
                    });
        }
    }

    public Optional<GeolocationDto> getGeolocationDataFromCache(String key) {
        try {
            CacheStats cacheStats = locationCache.stats();
            logger.info("CacheStats = {} ", cacheStats);
            return Optional.ofNullable(locationCache.get(key));
        } catch (ExecutionException e) {
            logger.error("Error Retrieving from the GeoLocation Cache"
                    + e.getMessage());
        }
        return Optional.empty();
    }


}
