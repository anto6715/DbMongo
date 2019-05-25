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

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class TestRestController {

    @Autowired
    TestService testService;

    @RequestMapping(value = "/createLevels", method = RequestMethod.GET)
    public GeoJson createLevels() throws InterruptedException {
        Runnable runnable = new ScheduleLevels();
        runnable.run();
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
