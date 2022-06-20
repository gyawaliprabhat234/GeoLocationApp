package com.geolocation.repository;

import com.geolocation.domain.entity.Geolocation;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * @author Prabhat Gyawali
 * @created 20-Jun-2022 - 9:53 AM
 * @project GeoLocationApp
 */
public class GeolocationDAO extends AbstractDAO<Geolocation> {
    public GeolocationDAO(SessionFactory sessionFactory){
        super(sessionFactory);
    }
    public void delete(String ipAddress){
        currentSession().delete(findByIpAddress(ipAddress));
        currentSession().flush();
    }
    public Geolocation findByIpAddress(String ipAddress){
        return get(ipAddress);
    }

    public void save(Geolocation geolocation) {
        persist(geolocation);
    }
}
