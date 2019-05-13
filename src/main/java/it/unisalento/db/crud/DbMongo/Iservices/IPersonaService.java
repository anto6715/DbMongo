package it.unisalento.db.crud.DbMongo.Iservices;

import it.unisalento.db.crud.DbMongo.domain.Persona;

import java.util.List;

public interface IPersonaService {
    public Persona createPerson(Persona persona);
    public Persona updatePerson(Persona persona);
    public List<Persona> getAll();
    public Persona getById(String id) ;
    public void delete(String id);
}
