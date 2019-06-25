package it.unisalento.db.crud.DbMongo.models;

import it.unisalento.db.crud.DbMongo.domain.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class GeoJsonConverter {


    // restituisce un oggetto GeoJson partendo da una lista di Test
    public static GeoJson getGeoJson(List<Test> tests) {
        GeoJson geoJson = new GeoJson();
        List<Feature> features = new ArrayList<>();
        Feature feature;
        Geometry geometry;
        Properties properties;

        for (Test test: tests) {
            List<Double> coordinates = new ArrayList<>();
            feature = new Feature();
            geometry = new Geometry();
            properties = new Properties();
            coordinates.add(test.getPosition().getLon());
            coordinates.add(test.getPosition().getLat());

            //geometry.setType("Point");
            geometry.setCoordinates(coordinates);

            properties.setLeq(test.getMeasurement().getLeq());

            feature.setGeometry(geometry);
            //feature.setType("Feature");
            feature.setProperties(properties);

            features.add(feature);
        }



        //geoJson.setType("FeatureCollection");
        geoJson.setFeatures(features);
        return geoJson;
    }
}
