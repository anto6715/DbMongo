package it.unisalento.db.crud.DbMongo.restapi;

import it.unisalento.db.crud.DbMongo.domain.GeoJson;
import it.unisalento.db.crud.DbMongo.domain.Test;
import it.unisalento.db.crud.DbMongo.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    public List<Test> getAll() throws InterruptedException {
        return testService.getAll();
    }

    @RequestMapping(value = "/getJongo/{minLon}/{minLat}/{maxLon}/{maxLat}/{zoom}", method = RequestMethod.GET)
    public GeoJson getJongo(@PathVariable("minLon") double minLon,
                            @PathVariable("minLat") double minLat,
                            @PathVariable("maxLon") double maxLon,
                            @PathVariable("maxLat") double maxLat,
                            @PathVariable("zoom") int zoom) throws InterruptedException {
        //return testService.prova(minLon, minLat, maxLon, maxLat, zoom);
        return testService.getJongo(minLon, minLat, maxLon, maxLat, zoom);
    }

    @RequestMapping(value = "/getMorphia", method = RequestMethod.GET)
    public void getMorphia() {
        testService.getMorphia();
    }
}
