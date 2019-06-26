package it.unisalento.db.crud.DbMongo.configurations;

import it.unisalento.db.crud.DbMongo.models.ScheduleLevels;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
//@EnableMongoRepositories("it.unisalento.db.crud.DbMongo.repository")
/*
@ComponentScan({"it.unisalento.db.crud.DbMongo.Iservices"})
@ComponentScan({"it.unisalento.db.crud.DbMongo.restapi"})

@EntityScan("it.unisalento.db.crud.DbMongo.domain")*/
public class DbMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbMongoApplication.class, args);

		// scheduling function to periodically agglomerating data
		/*ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
		ZonedDateTime nextRun = now.withHour(0).withMinute(10).withSecond(00);
		if(now.compareTo(nextRun) > 0)
			nextRun = nextRun.plusDays(1);

		Duration duration = Duration.between(now, nextRun);
		long initalDelay = duration.getSeconds();
		System.out.println(initalDelay);
		System.out.println(TimeUnit.DAYS.toSeconds(1));
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new ScheduleLevels(),
				0,
				TimeUnit.DAYS.toSeconds(1),
				TimeUnit.SECONDS);*/

	}




}
