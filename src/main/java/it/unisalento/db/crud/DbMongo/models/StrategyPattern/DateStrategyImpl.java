package it.unisalento.db.crud.DbMongo.models.StrategyPattern;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateStrategyImpl implements DateStrategy {
    @Override
    public Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        calendar.set(year, month-1,day,1,59,0);
        return calendar.getTime();
    }

    @Override
    public Date getDate(int day) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, day); //0 today, -1 yesterday..
        return calendar.getTime();
    }

    @Override
    public String getFormatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
