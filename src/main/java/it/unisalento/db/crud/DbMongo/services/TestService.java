package it.unisalento.db.crud.DbMongo.services;

import com.mongodb.*;
import it.unisalento.db.crud.DbMongo.domain.*;
import it.unisalento.db.crud.DbMongo.models.GeoJsonConverter;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.Context;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.DateStrategyImpl;
import it.unisalento.db.crud.DbMongo.models.Tools;
import it.unisalento.db.crud.DbMongo.repository.TestRepository;
import org.jongo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TestService {
    @Autowired
    TestRepository testRepository;
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional
    public List<Test> getAll() {

        return testRepository.findAll();
    }

    public GeoJson getData(double minLon, double minLat, double maxLon, double maxLat, int zoom, int year, int month, int day, String collectionName) {
        List<Test> tests = new ArrayList<>();
        DB db = new MongoClient().getDB("demo");
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(collectionName);
        Context context = new Context(new DateStrategyImpl());
        Date date_start = context.executeDateStrategy(year, month, day);
        Date date_end = context.executeDateStrategy(year, month, day+1);
        System.out.println(date_start);
        System.out.println(date_end);

        MongoCursor<Test> iterator = collection.find(" { position: { $geoWithin: { $box: [ [ "+minLat+","+minLon+"],["+maxLat+",+"+maxLon+"]]}}, measureTimestamp.date : {$gte: #, $lt: # }},{allowDiskUse: false}", context.executeDateStrategy(year, month, day),context.executeDateStrategy(year, month, day+1)).map(result -> {
            //MongoCursor<Test> iterator = collection.find(" { position: { $geoWithin: { $box: [ [ "+minLat+","+minLon+"],["+maxLat+",+"+maxLon+"]]}}},{allowDiskUse: false}").map(result -> {
            Test t = new Test(result.get("_id").toString(),
                    new Position((Double) ((DBObject) result.get("position")).get("lat"),(Double) ((DBObject) result.get("position")).get("lon")),
                    new Measurement((Double) ((DBObject) result.get("measurement")).get("leq")));
            return t;
        });
        while (iterator.hasNext()){
            Test t = iterator.next();
            tests.add(t);
        }
        System.out.println("Dati ottenuti: " + tests.size());
        return GeoJsonConverter.getGeoJson(tests);
    }

    public GeoJson getRealTimeData( double minLon, double minLat, double maxLon, double maxLat, int zoom, int year, int month, int day) throws InterruptedException {
        List<TestThread> threads = new ArrayList<>();
        List<Test> list = new ArrayList<>();
        int totalList=0;
        int n_threads=1;
        double interval = (maxLon-minLon)/n_threads;
        for (int i = 0; i<n_threads; i++) {
            double minlon = minLon+i*interval+0.0000000000001;
            double maxlon =minLon+(i+1)*interval;
            TestThread thread = new TestThread( minlon, minLat, maxlon, maxLat, zoom, i, year, month, day);
            thread.start();
            threads.add(thread);
        }
        for (int i = 0; i<n_threads; i++) {
            threads.get(i).join();
            list.addAll(threads.get(i).getTs());
            totalList += threads.get(i).getLength();
        }
        System.out.println("Lista completa: " + totalList);
        System.out.println("Lista compattata:" + list.size());
        return GeoJsonConverter.getGeoJson(list);
    }



    public GeoJson getDayLevel(int zoom, int year, int month, int day) {
        List<Test> tests = new ArrayList<>();
        DB db = new MongoClient().getDB("demo");
        Jongo jongo = new Jongo(db);
        String collectionName =  year + "-" + month + "-" + day + "_zoom_" + zoom;
        MongoCollection collection = jongo.getCollection(collectionName);

        Iterator<Test> it = collection.find().as(Test.class).iterator();
        while (it.hasNext()) {
            tests.add(it.next());
        }
        return GeoJsonConverter.getGeoJson(tests);
    }




    public void createDayLevels() {

        Context context = new Context(new DateStrategyImpl());
        Date date_start = context.executeDateStrategy(2019, 5, 24);
        Date date_end = context.executeDateStrategy(2019, 5, 24+1);
        int zoom = 8;
        int approx = 70;
        for (int i = 0; i<1; i++){
            String collectionName =  2019 + "-" + "0"+5 + "-" + 24 + "_zoom_" + zoom;
            createLevel(date_start,date_end,approx,zoom, collectionName);
            approx *= 10;
            zoom += 2;
        }
    }

    private Date getDate(int day) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, day); //0 today, -1 yesterday..
        return cal.getTime();
    }

    public void createLevel(Date date_start, Date date_end, int approx, int zoom, String collectionName) {
        List<Test> tests = new ArrayList<>();
        DB db = new MongoClient().getDB("demo");
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection(2019 + "-" + "0"+5 + "-" + 24 + "_zoom_" + 14);


        System.out.println(format.format(date_end));

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

}


class TestThread extends Thread {
    private double minLon;
    private double minLat;
    private double maxLon;
    private double maxLat;
    private double intervalLat;
    private double intervalLon;
    private int zoom;
    private int id;
    int year;
    int month;
    int day;
    private List<Test> ts;
    int count =0;


    public TestThread(double minLon, double minLat, double maxLon, double maxLat, int zoom, int id,int year, int month, int day) {
        this.minLon = minLon;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.maxLat = maxLat;
        this.zoom = zoom;
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.intervalLat = maxLat - minLat;
        this.intervalLon = maxLon - minLon;

    }

    public List<Test> getTs() {
        return ts;
    }

    public int getLength(){
        return count;
    }

    public void getData() {
        DB db = new MongoClient().getDB("demo");
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection("demo");

        // Uso del patter Strategy per creare la data secondo il formato corretto
        Context context = new Context(new DateStrategyImpl());
        Date date_start = context.executeDateStrategy(year, month, day);
        Date date_end = context.executeDateStrategy(year, month, day+1);

        MongoCursor<Test> iterator = collection.find(" { position: { $geoWithin: { $box: [ [ "+minLat+","+minLon+"],["+maxLat+",+"+maxLon+"]]}}, measureTimestamp.date : {$gte: #, $lt: # }},{allowDiskUse: false}", date_start,date_end).map(result -> {
        //MongoCursor<Test> iterator = collection.find(" { position: { $geoWithin: { $box: [ [ "+minLat+","+minLon+"],["+maxLat+",+"+maxLon+"]]}}},{allowDiskUse: false}").map(result -> {
        Test t = new Test(result.get("_id").toString(),
                    new Position((Double) ((DBObject) result.get("position")).get("lat"),(Double) ((DBObject) result.get("position")).get("lon")),
                    new Measurement((Double) ((DBObject) result.get("measurement")).get("leq")));
            return t;
        });
        HashMap<String, Test> map = new HashMap();
        while (iterator.hasNext()){
            count +=1;
            Test test = iterator.next();
            String key = Double.toString(Tools.round((test.getPosition().getLat()-minLat)/(intervalLat)*0.04/(zoom*2),6))+Double.toString(Tools.round((test.getPosition().getLon()-minLon)/(intervalLon)*0.04/(zoom*2),6));
            if (!map.containsKey(key)){
                map.put(key,test);
            } else {
                test.addTest(map.get(key));
                map.put(key,test);
            }
        }
        ts = new ArrayList<>(map.values());
    }


    public void run() {
        System.out.println("avvio thread");
        this.getData();
    }
}
