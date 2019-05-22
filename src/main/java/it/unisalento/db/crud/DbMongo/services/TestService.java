package it.unisalento.db.crud.DbMongo.services;

import com.mongodb.*;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import it.unisalento.db.crud.DbMongo.domain.*;
import it.unisalento.db.crud.DbMongo.models.GeoJsonConverter;
import it.unisalento.db.crud.DbMongo.models.Tools;
import it.unisalento.db.crud.DbMongo.repository.TestRepository;
import org.jongo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;



@Service
public class TestService {
    @Autowired
    TestRepository testRepository;


    @Transactional
    public List<Test> getAll() {

        return testRepository.findAll();
    }

    public GeoJson getJongo( double minLon, double minLat, double maxLon, double maxLat, int zoom) throws InterruptedException {
        List<TestThread> threads = new ArrayList<>();
        List<Test> list = new ArrayList<>();
        int n_threads=8;
        double interval = (maxLon-minLon)/n_threads;
        for (int i = 0; i<n_threads; i++) {
            double minlon = minLon+i*interval+0.0000000000001;
            double maxlon =minLon+(i+1)*interval;
            TestThread thread = new TestThread( minlon, minLat, maxlon, maxLat, zoom, i);
            thread.start();
            threads.add(thread);
        }
        for (int i = 0; i<n_threads; i++) {
            threads.get(i).join();
            list.addAll(threads.get(i).getTs());
        }
        System.out.println("Lista compattata:" + list.size());
        return GeoJsonConverter.getGeoJson(list);
    }

    public void getMorphia() {
        final Morphia morphia = new Morphia();
        morphia.map(Test.class);

        morphia.mapPackage("it.unisalento.db.crud.DbMongo.domain");
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "demo");
        //datastore.ensureIndexes();

        /*final Query<Test> query = datastore.createQuery(Test.class);
        final List<Test> tests = query.asList();
        System.out.println(tests.size());*/
        final Query<Test> query = datastore.find(Test.class);
        List<Test> list= query.find().toList();

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
    private List<Test> ts;


    public TestThread(double minLon, double minLat, double maxLon, double maxLat, int zoom, int id) {
        this.minLon = minLon;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.maxLat = maxLat;
        this.zoom = zoom;
        this.id = id;
        this.intervalLat = maxLat - minLat;
        this.intervalLon = maxLon - minLon;
    }

    public List<Test> getTs() {
        return ts;
    }

    public void getJongo() {
        DB db = new MongoClient().getDB("demo");
        Jongo jongo = new Jongo(db);
        MongoCollection collection = jongo.getCollection("prova");

        Aggregate.ResultsIterator<Test> iterator = collection.aggregate("{$match: { position: { $geoWithin: { $box: [ [ "+minLat+","+minLon+"],["+maxLat+",+"+maxLon+"]]}}}},{allowDiskUse: false}").map(result -> {
            Test t = new Test(result.get("_id").toString(),
                    new Position((Double) ((DBObject) result.get("position")).get("lat"),(Double) ((DBObject) result.get("position")).get("lon")),
                    new Measurement((Double) ((DBObject) result.get("measurement")).get("leq")));
            return t;
        });
        HashMap<String, Test> map = new HashMap();
        while (iterator.hasNext()){
            Test test = iterator.next();
            String key = Double.toString(Tools.round((test.getPosition().getLat()-minLat)/(intervalLat)*0.01/zoom,5))+Double.toString(Tools.round((test.getPosition().getLon()-minLon)/(intervalLon)*0.01/zoom,5));
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
        this.getJongo();
    }
}
