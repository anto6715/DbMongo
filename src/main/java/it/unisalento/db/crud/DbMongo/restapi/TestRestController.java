package it.unisalento.db.crud.DbMongo.restapi;

import it.unisalento.db.crud.DbMongo.domain.GeoJson;
import it.unisalento.db.crud.DbMongo.domain.Test;
import it.unisalento.db.crud.DbMongo.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestRestController {

    @Autowired
    TestService testService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<Test> getAll() {
        return testService.getAll();
    }

    @RequestMapping(value = "/getGeo", method = RequestMethod.GET)
    public GeoJson getGeo() {
        return testService.getAllGeoJson();
    }

    @RequestMapping(value = "/getJongo", method = RequestMethod.GET)
    public GeoJson getJongo() {
        return testService.getJongo();
    }

    @RequestMapping(value = "/getMorphia", method = RequestMethod.GET)
    public void getMorphia() {
        testService.getMorphia();
    }

   /* @RequestMapping(value = "/getGeoSquare", method = RequestMethod.GET)
    public GeoJson getGeoSquare() {
        return testService.getAllGeoJsonSqare();
    }*/
}
