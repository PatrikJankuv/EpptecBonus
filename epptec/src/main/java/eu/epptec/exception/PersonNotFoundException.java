package eu.epptec.exception;

public class PersonNotFoundException extends Exception {
    public PersonNotFoundException() {
        super("Person not found");
    }
}

