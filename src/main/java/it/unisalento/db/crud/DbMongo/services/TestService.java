package it.unisalento.db.crud.DbMongo.services;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import it.unisalento.db.crud.DbMongo.domain.*;
import it.unisalento.db.crud.DbMongo.models.GeoJsonConverter;
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
    @Autowired
    TestRepository testRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Transactional
    public List<Test> getAll() {
         Sort sort = Sort.by(Sort.Order.asc("position.lon"));
         return  mongoTemplate.findAll(Test.class);
         //return testRepository.findAll();
    }

    public GeoJson getJongo() {
        DB db = new MongoClient().getDB("demo");
        Jongo jongo = new Jongo(db);
        Date startGetAll = new Date();
        MongoCollection tests = jongo.getCollection("test");
        MongoCursor<Test> all = tests.find().as(Test.class);

        Iterator<Test> iterator = all.iterator();
        List<Test> tests1 = new ArrayList<Test>();
        while (iterator.hasNext()) {
            tests1.add(iterator.next());
        }
        HashMap map = new HashMap();
        Date endGetAll = new Date();
        for (Test test: tests1){
            //Test test = iterator.next();
            String key = Double.toString(test.getPosition().getRoundedLat(0))+Double.toString(test.getPosition().getRoundedLon(0));
            if (!map.containsKey(key)){
                TestAvg avg = new TestAvg(test);
                map.put(key,avg);
            } else {
                TestAvg avg = (TestAvg) map.get(key);
                avg.addTest(test);

                /*Measurement m = new Measurement();
                Position p = new Position();
                double avg = (t.getMeasurement().getLeq() + test.getMeasurement().getLeq())/2.0;
                double avgLat = (t.getPosition().getLat() + test.getPosition().getLat())/2;
                double avgLon = (t.getPosition().getLon() + test.getPosition().getLon())/2;
                m.setLeq(avg);
                p.setLat(avgLat);
                p.setLon(avgLon);
                t.setMeasurement(m);
                t.setPosition(p);*/
                map.put(key,avg);
            }
        }
        Date endAgglomeration = new Date();
        List<TestAvg> ts = new ArrayList<TestAvg>(map.values());
        GeoJson geo = GeoJsonConverter.getGeoJson(ts);
        Date endGeo = new Date();
        System.out.println(map.size());
        System.out.println("Start request: "+ startGetAll);
        System.out.println("End request and start agglomeration: " + endGetAll);
        System.out.println("End agglomeration and start convertion: " + endAgglomeration);
        System.out.println("End conversion: " + endGeo);
        return geo;
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

    @Transactional
    public GeoJson getAllGeoJson(){
        HashMap map = new HashMap();
        Date startGetAll = new Date();
        List<Test> tests = this.getAll();
        Date endGetAll = new Date();


        for (Test test: tests){
            String key = Double.toString(test.getPosition().getRoundedLat(0))+Double.toString(test.getPosition().getRoundedLon(0));
            if (!map.containsKey(key)){
                TestAvg avg = new TestAvg(test);
                map.put(key,avg);
            } else {
                TestAvg avg = (TestAvg) map.get(key);
                avg.addTest(test);

                /*Measurement m = new Measurement();
                Position p = new Position();
                double avg = (t.getMeasurement().getLeq() + test.getMeasurement().getLeq())/2.0;
                double avgLat = (t.getPosition().getLat() + test.getPosition().getLat())/2;
                double avgLon = (t.getPosition().getLon() + test.getPosition().getLon())/2;
                m.setLeq(avg);
                p.setLat(avgLat);
                p.setLon(avgLon);
                t.setMeasurement(m);
                t.setPosition(p);*/
                map.put(key,avg);
            }
        }
        Date endAgglomeration = new Date();
        List<TestAvg> ts = new ArrayList<TestAvg>(map.values());
        GeoJson geo = GeoJsonConverter.getGeoJson(ts);
        Date endGeo = new Date();
        System.out.println(map.size());
        System.out.println("Start request: "+ startGetAll);
        System.out.println("End request and start agglomeration: " + endGetAll);
        System.out.println("End agglomeration and start convertion: " + endAgglomeration);
        System.out.println("End conversion: " + endGeo);
         return geo;
    }

    /*@Transactional
    public GeoJson getAllGeoJsonSqare(){

        double min_lat =  35.63827;
        double max_lat =  41.81516;
        double min_lon =  12.49102;
        double max_lon =  18.14385;
        List<Test> ridotti = new ArrayList<>();
        double ml = min_lat;
        while (ml <= max_lat) {
            double m = min_lon;
            while (m <= max_lon){
                List<Test> tests = this.getAllSquare(ml,m,ml + 0.3,m + 0.3);
                double lon = 0;
                double lat = 0;
                double leq = 0;
                for (int i = 0; i< tests.size(); i++){
                    lon = lon + tests.get(i).getPosition().getLon();
                    lat = lat + tests.get(i).getPosition().getLat();
                    leq = leq + tests.get(i).getMeasurement().getLeq();
                }
                Test t = new Test();
                Position p = new Position();
                Measurement mis = new Measurement();
                p.setLat(lat/(tests.size()));
                p.setLon(lon/(tests.size()));
                mis.setLeq(leq/tests.size());
                t.setPosition(p);
                t.setMeasurement(mis);
                ridotti.add(t);
                m = m + 0.3;
            }
            ml = ml + 0.3;
        }

/*
        Sort sort = Sort.by(
                Sort.Order.desc("position.lat"));
        List<Test> tests = this.getAllSorted(sort);
        Collections.sort(tests, new Comparator<Test>() {
            @Override
            public int compare(Test o1, Test o2) {
                int xComp = Double.compare(o1.getPosition().getLon(), o2.getPosition().getLon());
                if(xComp == 0)
                    return Double.compare(o1.getPosition().getLat(), o2.getPosition().getLat());
                else
                    return xComp;
            }
        });*/
        /*
        for(int i = 0; i<tests.size();i=i+25) {

            double lon = 0;
            double lat = 0;
            double leq = 0;
            for(int j = 0; j< 25; j++){
                System.out.println(i+j);
                lon = lon + tests.get(i+j).getPosition().getLon();
                lat = lat + tests.get(i+j).getPosition().getLat();
                leq = leq + tests.get(i+j).getMeasurement().getLeq();
            }
            System.out.println("qui");
            Test t = new Test();
            Position p = new Position();
            Measurement m = new Measurement();
            p.setLat(lat/25);
            p.setLon(lon/25);
            m.setLeq(leq/25);
            t.setPosition(p);
            t.setMeasurement(m);
            System.out.println("qui");
            ridotti.add(t);
            System.out.println("qui");
            System.out.println(i);
        }
        */

        /*GeoJson geo = GeoJsonConverter.getGeoJson(ridotti);
        return geo;
    }*/
}
