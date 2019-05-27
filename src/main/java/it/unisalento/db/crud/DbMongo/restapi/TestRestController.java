package it.unisalento.db.crud.DbMongo.restapi;

import it.unisalento.db.crud.DbMongo.domain.GeoJson;
import it.unisalento.db.crud.DbMongo.domain.Test;
import it.unisalento.db.crud.DbMongo.models.ScheduleLevels;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.Context;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.DateStrategyImpl;
import it.unisalento.db.crud.DbMongo.models.Tools;
import it.unisalento.db.crud.DbMongo.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestRestController {

    @Autowired
    TestService testService;


    @RequestMapping(value = "/prova", method = RequestMethod.GET)
    public GeoJson prova() throws InterruptedException {
        Context context = new Context(new DateStrategyImpl());
        System.out.println(context.executeDateStrategy(2019,5,30));
        return null;
    }

    @RequestMapping(value = "/createLevels", method = RequestMethod.GET)
    public GeoJson createLevels() throws InterruptedException {
        Runnable runnable = new ScheduleLevels();
        runnable.run();
        return null;
    }
    @RequestMapping(value = "/createLevels/{year}/{month}/{day}", method = RequestMethod.GET)
    public GeoJson createLevelsDate(@PathVariable("year") int year,
                                    @PathVariable("month") int month,
                                    @PathVariable("day") int day) throws InterruptedException {
        Runnable runnable = new ScheduleLevels(year, month, day);
        runnable.run();
        return null;
    }

    @RequestMapping(value = "/getData/{minLon}/{minLat}/{maxLon}/{maxLat}/{zoom}/{year}/{month}/{day}", method = RequestMethod.GET)
    public GeoJson getJongoDate(@PathVariable("minLon") double minLon,
                            @PathVariable("minLat") double minLat,
                            @PathVariable("maxLon") double maxLon,
                            @PathVariable("maxLat") double maxLat,
                            @PathVariable("zoom") int zoom,
                            @PathVariable("year") int year,
                            @PathVariable("month") int month,
                            @PathVariable("day") int day) throws InterruptedException {
        return testService.getData(minLon, minLat, maxLon, maxLat, Tools.getCollectionName(zoom, year, month, day));

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
