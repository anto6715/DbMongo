package it.unisalento.db.crud.DbMongo.domain;

import dev.morphia.annotations.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "prova")
public class Test {

    @Id
    public String id;
    public Position position = new Position();
    public Measurement measurement = new Measurement();

    public Test(String id, Position p, Measurement m) {
        this.id = id;
        this.position = p;
        this.measurement = m;
    }
    public Test() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public Test addTest(Test test) {
        this.position.setLon((test.getPosition().getLon()+this.position.getLon())/2);
        this.position.setLat((test.getPosition().getLat() + this.position.getLat())/2);
        this.measurement.setLeq((test.getMeasurement().getLeq() + this.measurement.getLeq())/2);
    return this;
    }


}
