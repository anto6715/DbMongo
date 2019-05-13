package it.unisalento.db.crud.DbMongo.repository;

import it.unisalento.db.crud.DbMongo.domain.Persona;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends MongoRepository<Persona,String> {

    Persona getByNome(String nome);
    Persona getById(String id);
    void deleteById(String id);
}
