package it.unisalento.db.crud.DbMongo.configurations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("it.unisalento.db.crud.DbMongo.repository")
/*
@ComponentScan({"it.unisalento.db.crud.DbMongo.Iservices"})
@ComponentScan({"it.unisalento.db.crud.DbMongo.restapi"})

@EntityScan("it.unisalento.db.crud.DbMongo.domain")*/
public class DbMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbMongoApplication.class, args);
	}

}
