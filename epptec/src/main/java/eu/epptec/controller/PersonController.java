package eu.epptec.controller;

import eu.epptec.exception.InvalidDataException;
import eu.epptec.exception.PersonAlreadyExistsException;
import eu.epptec.exception.PersonNotFoundException;
import eu.epptec.model.Person;
import eu.epptec.model.PersonWithAge;
import eu.epptec.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        try {
            personService.addPerson(person);
            return ResponseEntity.status(HttpStatus.CREATED).body("Person added");
        } catch (InvalidDataException | PersonAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removePerson(@PathVariable String id) {
        try {
            personService.removePerson(id);
            return ResponseEntity.ok("Person with birth number " + id + " removed");
        } catch (PersonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable String id) {
        try {
            PersonWithAge person = new PersonWithAge(personService.searchPersonByBirthNumber(id));
            return ResponseEntity.ok(person);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}