package it.unisalento.db.crud.DbMongo.Iservices;

import it.unisalento.db.crud.DbMongo.domain.GeoJson;

public interface ITestService {
    public GeoJson getData(double minLon, double minLat, double maxLon, double maxLat, String collectionName);

    public GeoJson getRealTimeData( double minLon, double minLat, double maxLon, double maxLat, int zoom) throws InterruptedException;

}
