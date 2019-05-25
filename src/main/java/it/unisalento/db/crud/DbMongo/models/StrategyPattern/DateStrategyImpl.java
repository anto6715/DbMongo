package it.unisalento.db.crud.DbMongo.models.StrategyPattern;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateStrategyImpl implements DateStrategy {
    @Override
    public Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ITALY);
        calendar.set(year, month-1,day,0,0,0);
        Date date = calendar.getTime();
        return date;
    }
}