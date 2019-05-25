package it.unisalento.db.crud.DbMongo.models.StrategyPattern;

import java.util.Date;

public class Context {

    private DateStrategy dateStrategy;

    public Context(DateStrategy dateStrategy) {
        this.dateStrategy = dateStrategy;
    }

    public Date executeDateStrategy(int year, int month, int day) {
        return dateStrategy.getDate(year, month, day);
    }
    public Date executeDateStrategy(int day) {
        return dateStrategy.getDate(day);
    }
    public String executeDateStrategy(Date date) {
        return dateStrategy.getFormatDate(date);
    }
    public void changeStrategy(DateStrategy dateStrategy) {
        this.dateStrategy = dateStrategy;
    }
}
