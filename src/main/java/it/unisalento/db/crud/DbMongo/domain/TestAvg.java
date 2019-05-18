package it.unisalento.db.crud.DbMongo.domain;

public class TestAvg {

    private String id;
    private Position position = new Position();
    private Measurement measurement = new Measurement();

    public void addTest(Test test) {
        this.position.setLon((test.getPosition().getLon()+this.position.getLon())/2);
        this.position.setLat((test.getPosition().getLat() + this.position.getLat())/2);
        this.measurement.setLeq((test.getMeasurement().getLeq() + this.measurement.getLeq())/2);
    }

    public TestAvg(Test test) {
        this.measurement = test.getMeasurement();
        this.position = test.getPosition();
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
}
