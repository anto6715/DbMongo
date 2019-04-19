package it.unisalento.db.crud.DbMongo.services;

import it.unisalento.db.crud.DbMongo.Iservices.IPersonaService;
import it.unisalento.db.crud.DbMongo.domain.Persona;
import it.unisalento.db.crud.DbMongo.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonaService implements IPersonaService {

    @Autowired
    PersonaRepository personaRepository;

    @Transactional
    public Persona createPerson(Persona persona) {
        Persona p = personaRepository.save(persona);
        return p;
    }

    @Transactional
    public Persona updatePerson(Persona persona) {
        Persona p = personaRepository.getById(persona.getId());
        p.setIndirizzo(persona.getIndirizzo());
        p.setCitta(persona.getCitta());
        p.setProvincia(persona.getProvincia());

        return personaRepository.save(p);
    }

    @Transactional
    public List<Persona> getAll() {
        return personaRepository.findAll();
    }

    @Transactional
    public Persona getById(String id) {
        Persona p = personaRepository.getById(id);
        return p;
    }

    @Transactional
    public void delete(String id) {
        personaRepository.deleteById(id);
        return;
    }
}
