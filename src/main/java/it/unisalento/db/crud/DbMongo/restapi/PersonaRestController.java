package it.unisalento.db.crud.DbMongo.restapi;

import it.unisalento.db.crud.DbMongo.Iservices.IPersonaService;
import it.unisalento.db.crud.DbMongo.repository.PersonaRepository;
import it.unisalento.db.crud.DbMongo.domain.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/persona")
public class PersonaRestController {

    @Autowired
    IPersonaService personaService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Persona createPerson(@Valid @RequestBody Persona persona) {
        return personaService.createPerson(persona);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Persona updatePerson(@Valid @RequestBody Persona persona) {
        return personaService.updatePerson(persona);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<Persona> getAll() {
        return personaService.getAll();
    }


    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
    public Persona getById(@PathVariable String id) {
        return personaService.getById(id);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public void delete(@PathVariable String id) {
        personaService.delete(id);
        return;
    }
}
