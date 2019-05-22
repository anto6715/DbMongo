package it.unisalento.db.crud.DbMongo.domain;

public class Measurement {

    public double leq;
    public double spl;

    public Measurement(Double leq) {
        this.leq = leq;
    }

    public Measurement() {
    }

    public double getLeq() {
        return leq;
    }

    public void setLeq(double leq) {
        this.leq = leq;
    }

    public double getSpl() {
        return spl;
    }

    public void setSpl(double spl) {
        this.spl = spl;
    }
}
