package it.unisalento.db.crud.DbMongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/persona")
public class PersonaRestController {

    @Autowired
    PersonaRepository personaRepository;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Persona createPerson(@Valid @RequestBody Persona persona) {
        System.out.println("Ciao");
        personaRepository.save(persona);
        return persona;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Persona updatePerson(@Valid @RequestBody Persona persona) {
        Persona p = personaRepository.getById(persona.getId());
        p.setIndirizzo(persona.getIndirizzo());
        p.setCitta(persona.getCitta());
        p.setProvincia(persona.getProvincia());

        return personaRepository.save(p);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<Persona> getAll() {
        System.out.println("Ciao");
        return personaRepository.findAll();
    }


    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
    public Persona getById(@PathVariable String id) {
        Persona p = personaRepository.getById(id);
        return p;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public void delete(@PathVariable String id) {
        System.out.println("Ciao");
        personaRepository.deleteById(id);
        return;
    }
}
