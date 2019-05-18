package it.unisalento.db.crud.DbMongo.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Position {

    public double lat;
    public double lon;

    public double getRoundedLat(int r) {
        return round(lat,r);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getRoundedLon(int r) {
        return round(lon, r);
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
