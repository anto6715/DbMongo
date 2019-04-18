package it.unisalento.db.crud.DbMongo;

import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;


public interface PersonaRepository extends MongoRepository<Persona,String> {

    Persona getByNome(String nome);
    Persona getById(String id);
    void deleteById(String id);
}
