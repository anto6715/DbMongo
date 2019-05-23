package it.unisalento.db.crud.DbMongo.models.StrategyPattern;

import java.util.Date;

public interface DateStrategy {
    public Date getDate(int year, int month, int day);
}
