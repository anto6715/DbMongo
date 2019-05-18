package it.unisalento.db.crud.DbMongo.domain;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.jongo.marshall.jackson.oid.MongoId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity("test")
public class Test {

    @Id
    public String id;
    public Position position = new Position();
    public Measurement measurement = new Measurement();

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


}
