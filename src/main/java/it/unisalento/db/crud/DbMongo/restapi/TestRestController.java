package it.unisalento.db.crud.DbMongo.restapi;

import it.unisalento.db.crud.DbMongo.domain.GeoJson;
import it.unisalento.db.crud.DbMongo.domain.Test;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.Context;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.DateStrategyImpl;
import it.unisalento.db.crud.DbMongo.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @RequestMapping(value = "/prova", method = RequestMethod.GET)
    public GeoJson prova() throws InterruptedException {
        //return testService.prova(minLon, minLat, maxLon, maxLat, zoom);
        //return testService.createLevel();
        testService.createDayLevels();
        return null;
    }

    @RequestMapping(value = "/getDayLevel/{zoom}/{year}/{month}/{day}", method = RequestMethod.GET)
    public GeoJson getDayLevel(    @PathVariable("zoom") int zoom,
                                   @PathVariable("year") int year,
                                   @PathVariable("month") int month,
                                   @PathVariable("day") int day) throws InterruptedException {
        testService.getDayLevel(zoom, year, month, day);
        return null;
    }

    @RequestMapping(value = "/getJongo/{minLon}/{minLat}/{maxLon}/{maxLat}/{zoom}/{year}/{month}/{day}", method = RequestMethod.GET)
    public GeoJson getJongoDate(@PathVariable("minLon") double minLon,
                            @PathVariable("minLat") double minLat,
                            @PathVariable("maxLon") double maxLon,
                            @PathVariable("maxLat") double maxLat,
                            @PathVariable("zoom") int zoom,
                            @PathVariable("year") int year,
                            @PathVariable("month") int month,
                            @PathVariable("day") int day) throws InterruptedException {
        if (zoom > 12) {
            return testService.getData(minLon, minLat, maxLon, maxLat, zoom, year, month, day);
        }
        else return new GeoJson();
    }

    @RequestMapping(value = "/getRealData/{minLon}/{minLat}/{maxLon}/{maxLat}/{zoom}/{year}/{month}/{day}", method = RequestMethod.GET)
    public GeoJson getRealData(@PathVariable("minLon") double minLon,
                                @PathVariable("minLat") double minLat,
                                @PathVariable("maxLon") double maxLon,
                                @PathVariable("maxLat") double maxLat,
                                @PathVariable("zoom") int zoom,
                                @PathVariable("year") int year,
                                @PathVariable("month") int month,
                                @PathVariable("day") int day) throws InterruptedException {
        return testService.getRealTimeData(minLon, minLat, maxLon, maxLat, zoom, year, month, day);

    }


}
