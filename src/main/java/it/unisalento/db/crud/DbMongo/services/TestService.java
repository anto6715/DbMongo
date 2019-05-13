package it.unisalento.db.crud.DbMongo.services;

import it.unisalento.db.crud.DbMongo.domain.*;
import it.unisalento.db.crud.DbMongo.models.GeoJsonConverter;
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
    public GeoJson getAllGeoJson(){
        List<Test> tests = this.getAll();
        GeoJson geo = GeoJsonConverter.getGeoJson(tests);
         return geo;
    }
}
