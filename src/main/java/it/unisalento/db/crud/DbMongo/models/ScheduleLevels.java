package it.unisalento.db.crud.DbMongo.models;

import com.mongodb.AggregationOptions;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import it.unisalento.db.crud.DbMongo.domain.Test;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.Context;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.DateStrategyImpl;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.*;

public class ScheduleLevels implements Runnable {

    public void createDayLevels() {

        Context context = new Context(new DateStrategyImpl());
        Date date_start = context.executeDateStrategy(-1);
        Date date_end = context.executeDateStrategy(0);
        String dateFormat = context.executeDateStrategy(date_start);

        int zoom = 4;
        List<Integer> approsimations = Tools.getAgglomerationValue();
        for (int i = 0; i<7; i++){
            String collectionName =  dateFormat + "_zoom_" + zoom;
            createLevel(date_start, date_end,   approsimations.get(i),  zoom, collectionName);
            zoom += 2;
        }
    }


    public void createLevel(Date date_start, Date date_end, int approx, int zoom, String collectionName) {
        List<Test> tests = new ArrayList<>();
        DB db = new MongoClient().getDB("demo");
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection("demo");




        Iterator<Test> it = collection.aggregate("{$match: {measureTimestamp.date : {$gte: #, $lt: # }}}", date_start,date_end)
                .and("{ $project : {lon_a: { $divide: [{ $trunc: { $multiply: ['$position.lon', "+approx+"]} }, "+approx+"]},lat_a: {$divide: [{$trunc: {$multiply: ['$position.lat', "+approx+"]}}, "+approx+"] }, lon : '$position.lon', lat: '$position.lat', leq: '$measurement.leq'}}")
                .and("{$project : {_id: { $concat: [ { $toString: '$lon_a' } , '_' , { $toString: '$lat_a' } ] }, lon: 1,lat: 1, leq: 1}}")
                .and("{$group : {_id: '$_id', lon: { $avg: '$lon' },lat: { $avg: '$lat' }, leq: { $avg: '$leq' }}}")
                .and("{$project : {position: {lat: '$lat', lon: '$lon'},measurement: {leq: '$leq' },measureTimestamp: {date: #}}}", date_start)
                .and("{$out: #}",collectionName).options(AggregationOptions.builder().allowDiskUse(true).build()).as(Test.class).iterator();
        int count = 0;
        while (it.hasNext()) {
            //tests.add(it.next());
            it.next();
            count += 1;
        }

        System.out.println("zoom: " + zoom + " punti: " + count);
        //return new GeoJson();
    }


    @Override
    public void run() {
        System.out.println("Start creation levels...");
        this.createDayLevels();
        System.out.println("...end");
    }
}