package it.unisalento.db.crud.DbMongo.services;

import it.unisalento.db.crud.DbMongo.domain.*;
import it.unisalento.db.crud.DbMongo.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {
    @Autowired
    TestRepository testRepository;

     @Transactional
    public List<Test> getAll() {
         return testRepository.findAll();
     }

    @Transactional
    public List<Test> getAllGeo() {

        return testRepository.findAll();
    }

    @Transactional
    public GeoJson getGeo() {
        GeoJson geo = new GeoJson();


         List<Feature> features = new ArrayList<>();
         for (Test test: testRepository.findAll()) {
             List<Double> coordinates = new ArrayList<>();
             Feature feature = new Feature();
             Geometry geometry = new Geometry();
             Properties properties = new Properties();
             coordinates.add(test.getPosition().getLon());
             coordinates.add(test.getPosition().getLat());

             geometry.setType("Point");
             geometry.setCoordinates(coordinates);

             properties.setLeq(test.getMeasurement().getLeq());

             feature.setGeometry(geometry);
             feature.setType("Feature");
             feature.setProperties(properties);

             features.add(feature);



         }



        geo.setType("FeatureCollection");
        geo.setFeatures(features);


         return geo;
    }
}
