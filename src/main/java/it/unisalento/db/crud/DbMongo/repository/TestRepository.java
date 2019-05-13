package it.unisalento.db.crud.DbMongo.repository;

import it.unisalento.db.crud.DbMongo.domain.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepository extends MongoRepository<Test, String> {
}
