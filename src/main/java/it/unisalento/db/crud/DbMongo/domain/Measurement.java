package it.unisalento.db.crud.DbMongo.domain;

public class Measurement {

    private double leq;
    private double spl;

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
