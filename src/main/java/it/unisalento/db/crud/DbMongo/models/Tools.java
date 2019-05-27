package it.unisalento.db.crud.DbMongo.models;

import it.unisalento.db.crud.DbMongo.models.StrategyPattern.Context;
import it.unisalento.db.crud.DbMongo.models.StrategyPattern.DateStrategyImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tools {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static List<Integer> getAgglomerationValue() {
        List<Integer> aggloemrations = new ArrayList<>();
        aggloemrations.add(30);     // zoom 4-5
        aggloemrations.add(50);     // zoom 6-7
        aggloemrations.add(70);     // zoom 8-9
        aggloemrations.add(100);    // zoom 10-11
        aggloemrations.add(300);    // zoom 12-13
        aggloemrations.add(1000);   // zoom 14-15
        aggloemrations.add(1000000);     // zoom 16-17

        return aggloemrations;
    }

    public static String getCollectionName(int zoom, int year, int month, int day){
        Context context = new Context(new DateStrategyImpl());
        Date date = context.executeDateStrategy(year, month, day);
        String dateFormat = context.executeDateStrategy(date);

        switch(zoom) {
            case 5:
                zoom=4;
                break;
            case 7:
                zoom=6;
                break;
            case 9:
                zoom=8;
                break;
            case 11:
                zoom=10;
                break;
            case 13:
                zoom=12;
                break;
            case 15:
                zoom=14;
                break;
            case 17:
                zoom=16;
                break;
        }
        return dateFormat + "_zoom_" + zoom;
    }
}
