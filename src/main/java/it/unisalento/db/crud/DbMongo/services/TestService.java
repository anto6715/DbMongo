package it.unisalento.db.crud.DbMongo.services;

import com.mongodb.*;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import it.unisalento.db.crud.DbMongo.domain.*;
import it.unisalento.db.crud.DbMongo.models.GeoJsonConverter;
import it.unisalento.db.crud.DbMongo.models.Tools;
import it.unisalento.db.crud.DbMongo.repository.TestRepository;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.lang.Math.min;


@Service
public class TestService {
    static List<Test> list1 = new ArrayList<>();
    static List<Test> list2 = new ArrayList<>();
    @Autowired
    TestRepository testRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Transactional
    public List<Test> getAll() throws InterruptedException {
        this.prova();

        return list1;
    }

    public GeoJson getJongo( double minLon, double minLat, double maxLon, double maxLat, int zoom) throws InterruptedException {
        TestThread thread1 = new TestThread(minLon, minLat, maxLon, maxLat, zoom);


        thread1.start();
        thread1.join();
        return thread1.getGeo();
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

    @Transactional
    public List<Test> getAllSquare(double latMin, double lonMin, double latMax, double lonMax) {
        return testRepository.findAllBySquare(latMin, lonMin, latMax, lonMax);
    }

    @Transactional
    public List<Test> getAllSorted(Sort sort) {
        return testRepository.findAll(sort);
    }

    public void prova() {
        MongoClient mongoClient = new MongoClient();
        DB database = mongoClient.getDB("demo");
        DBCollection collection = database.getCollection("test");
        DBCursor cursor = collection.find();
        List<DBObject> tests = new ArrayList<>();
        while (cursor.hasNext()) {
            tests.add(cursor.next());
        }
        System.out.println(tests.size());
    }




}


class TestThread extends Thread {
    GeoJson geo = new GeoJson();
    double minLon;
    double minLat;
    double maxLon;
    double maxLat;
    int zoom;

    public TestThread(double minLon, double minLat, double maxLon, double maxLat, int zoom) {
        this.minLon = minLon;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.maxLat = maxLat;
        this.zoom = zoom;
    }

    public GeoJson getGeo() {
        return geo;
    }

    public GeoJson getJongo() {
        DB db = new MongoClient().getDB("demo");
        Jongo jongo = new Jongo(db);
        Date startGetAll = new Date();
        MongoCollection tests = jongo.getCollection("test");
        Iterator<Test> it = tests.aggregate("{$match: { position: { $geoWithin: { $box: [ [ "+minLat+","+minLon+"],["+maxLat+",+"+maxLon+"]]}}}},{allowDiskUse: false}").as(Test.class).iterator();
        MongoCursor<Test> all = tests.find().as(Test.class);

        Iterator<Test> iterator = all.iterator();
        List<Test> tests1 = new ArrayList<Test>();
        while (it.hasNext()) {
            tests1.add(it.next());
        }
        System.out.println(tests1.size());
        HashMap map = new HashMap();
        Date endGetAll = new Date();
        for (Test test: tests1){
            //Test test = iterator.next();
            double normLat = Tools.round((test.getPosition().getLat()-minLat)/(maxLat-minLat)*1,2);
            double normLon = Tools.round((test.getPosition().getLon()-minLon)/(maxLon-minLon)*1,2);
            String key = Double.toString(normLat)+Double.toString(normLon);
            //System.out.println(key);
            if (!map.containsKey(key)){
                TestAvg avg = new TestAvg(test);
                map.put(key,avg);
                //System.out.println(key);
            } else {
                TestAvg avg = (TestAvg) map.get(key);
                avg.addTest(test);
                map.put(key,avg);
            }
        };
        Date endAgglomeration = new Date();
        List<TestAvg> ts = new ArrayList<TestAvg>(map.values());
        geo = GeoJsonConverter.getGeoJson(ts);
        Date endGeo = new Date();
        System.out.println(map.size());
        System.out.println("Start request: "+ startGetAll);
        System.out.println("End request and start agglomeration: " + endGetAll);
        System.out.println("End agglomeration and start convertion: " + endAgglomeration);
        System.out.println("End conversion: " + endGeo);
        return geo;
    }


    public void run() {
        this.getJongo();
    }
}
