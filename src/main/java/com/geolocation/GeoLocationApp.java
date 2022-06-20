package com.geolocation;

import com.geolocation.caching.GeolocationCaching;
import com.geolocation.config.DatasourceConfig;
import com.geolocation.domain.entity.Geolocation;
import com.geolocation.exception.CustomValidationExceptionMapper;
import com.geolocation.repository.GeolocationDAO;
import com.geolocation.resource.GeolocationResource;
import com.geolocation.service.GeolocationService;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:52 AM
 * @project GeoLocationApp
 */
public class GeoLocationApp extends Application<DatasourceConfig> {
    public static void main(String[] args) throws Exception {

        new GeoLocationApp().run(args);
    }
    @Override
    public String getName() {
        return "GeoLocationApp";
    }

    @Override
    public void initialize(Bootstrap<DatasourceConfig> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        bootstrap.addBundle(datasourceConfigHibernateBundle);

    }

    private final HibernateBundle<DatasourceConfig> datasourceConfigHibernateBundle = new HibernateBundle<>(Geolocation.class) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(DatasourceConfig datasourceConfig) {
            return datasourceConfig.getDataSourceFactory();
        }
    };

    @Override
    public void run(DatasourceConfig datasourceConfig, Environment environment) throws Exception {
        final GeolocationCaching cacheInstance = GeolocationCaching.getInstance();
        final GeolocationDAO geolocationDAO = new GeolocationDAO(datasourceConfigHibernateBundle.getSessionFactory());
        final Client client = new JerseyClientBuilder(environment).using(datasourceConfig.getJerseyClientConfiguration())
                .build(getName());
        final GeolocationService geolocationService = new GeolocationService(geolocationDAO,client, datasourceConfig.getApiURL());
        cacheInstance.initializeCache(geolocationService);

        final GeolocationResource resource = new GeolocationResource(geolocationService);
        environment.jersey().register(new CustomValidationExceptionMapper());
        environment.jersey().register(resource);
    }


}
