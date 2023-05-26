package eu.epptec.exception;

public class PersonAlreadyExistsException extends Exception {
    public PersonAlreadyExistsException() {
        super("Person already exists");
    }
}
