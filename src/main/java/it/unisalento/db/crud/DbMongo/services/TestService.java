package it.unisalento.db.crud.DbMongo.services;

import com.mongodb.*;
import it.unisalento.db.crud.DbMongo.Iservices.ITestService;
import it.unisalento.db.crud.DbMongo.domain.*;
import it.unisalento.db.crud.DbMongo.models.GeoJsonConverter;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.Context;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.DateStrategyImpl;
import it.unisalento.db.crud.DbMongo.models.Tools;
import org.jongo.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TestService implements ITestService {
    // connesione al db
    DB db = new MongoClient().getDB("demo");
    Jongo jongo = new Jongo(db);


    public GeoJson getData(double minLon, double minLat, double maxLon, double maxLat,  String collectionName) {
        List<Test> tests = new ArrayList<>();

        // effettuo al query alla collezione già agglomerata filtrando per coordinate
        MongoCollection collection = jongo.getCollection(collectionName);
        MongoCursor<Test> iterator = collection.find(" { position: { $geoWithin: { $box: [ [ "+minLat+","+minLon+"],["+maxLat+",+"+maxLon+"]]}}}")
                .map(result -> {
                    Test t =new Test(result.get("_id").toString(),
                            new Position((Double) ((DBObject) result.get("position")).get("lat"),(Double) ((DBObject) result.get("position")).get("lon")),
                            new Measurement((Double) ((DBObject) result.get("measurement")).get("leq")));
                    return t;
                });

        while (iterator.hasNext()){
            tests.add(iterator.next());
        }
        System.out.println("Dati ottenuti: " + tests.size());
        // ritorna i dati convertiti in GeoJson
        return GeoJsonConverter.getGeoJson(tests);
    }

    public GeoJson getRealTimeData( double minLon, double minLat, double maxLon, double maxLat, int zoom) throws InterruptedException {
        List<Test> tests = new ArrayList<>();

        DB db = new MongoClient().getDB("demo");
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection("demo");

        // crea data odierna e del giorno precedente usando lo strategy
        Context context = new Context(new DateStrategyImpl());
        Date date_start = context.executeDateStrategy(0);
        Date date_end = context.executeDateStrategy(1);
        int approx = Tools.getApprox(zoom);
        Iterator<Test> it  = collection.aggregate("{$match: {measureTimestamp.date : {$gte: #, $lt: # }}}", date_start,date_end)
                .and("{ $project : {lon_a: { $divide: [{ $trunc: { $multiply: ['$position.lon', "+approx+"]} }, "+approx+"]},lat_a: {$divide: [{$trunc: {$multiply: ['$position.lat', "+approx+"]}}, "+approx+"] }, lon : '$position.lon', lat: '$position.lat', leq: '$measurement.leq'}}")
                .and("{$project : {_id: { $concat: [ { $toString: '$lon_a' } , '_' , { $toString: '$lat_a' } ] }, lon: 1,lat: 1, leq: 1}}")
                .and("{$group : {_id: '$_id', lon: { $avg: '$lon' },lat: { $avg: '$lat' }, leq: { $avg: '$leq' }}}")
                .and("{$project : {position: {lat: '$lat', lon: '$lon'},measurement: {leq: '$leq' },measureTimestamp: {date: #}}}", date_start)
                .options(AggregationOptions.builder().allowDiskUse(true).build()).as(Test.class);

        while (it.hasNext()){
            tests.add(it.next());
        }
        System.out.println("Dati ottenuti: " + tests.size());
        return GeoJsonConverter.getGeoJson(tests);
    }



}
