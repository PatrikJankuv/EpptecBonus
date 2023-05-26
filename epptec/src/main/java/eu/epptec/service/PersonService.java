package eu.epptec.service;

import eu.epptec.exception.InvalidDataException;
import eu.epptec.exception.PersonAlreadyExistsException;
import eu.epptec.exception.PersonNotFoundException;
import eu.epptec.model.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class PersonService {
    private static final Map<String, Person> database = new HashMap<>();
    static Logger log = Logger.getLogger(PersonService.class.getName());

    public void addPerson(Person person) throws InvalidDataException, PersonAlreadyExistsException {
        validateName(person.getFirstName(), "First name");
        validateName(person.getLastName(), "Last name");
        validateBirthNumber(person.getBirthNumber());

        if (database.containsKey(convertBirthNumberToTenDigits(person.getBirthNumber()))) {
            throw new PersonAlreadyExistsException();
        }

        database.put(convertBirthNumberToTenDigits(person.getBirthNumber()), person);
        log.info("The person has been successfully added to the database.");
    }

    public void removePerson(String birthNumber) throws PersonNotFoundException {
        if (!database.containsKey(convertBirthNumberToTenDigits(birthNumber))) {
            throw new PersonNotFoundException();
        }
        database.remove(convertBirthNumberToTenDigits(birthNumber));
        log.info("The person with the birth number " + birthNumber + " has been successfully removed from the database.");
    }

    public Person searchPersonByBirthNumber(String birthNumber) throws PersonNotFoundException {
        if (!database.containsKey(convertBirthNumberToTenDigits(birthNumber))) {
            throw new PersonNotFoundException();
        }
        return database.get(convertBirthNumberToTenDigits(birthNumber));
    }

    private static void validateName(String name, String fieldName) throws InvalidDataException {
        if (name.isEmpty()) {
            throw new InvalidDataException(fieldName + " must not be empty.");
        }
    }

    private static void validateBirthNumber(String birthNumber) throws InvalidDataException {
        // Validating the ID format (YYMMDDXXXX or YYMMDD/XXXX)
        if (!birthNumber.matches("\\d{6}(\\d{4}|/\\d{4})")) {
            throw new InvalidDataException("The birth number must be in the format YYMMDDXXXX or YYMMDD/XXXX.");
        }

        String yearPrefix = birthNumber.substring(0, 2);
        String month = birthNumber.substring(2, 4);
        String day = birthNumber.substring(4, 6);

        int year = Integer.parseInt(yearPrefix);
        int fullYear = 1900 + year;

        String dateStr = String.format("%d%s%s", fullYear, month, day);
        var formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            LocalDate dateOfBirth = LocalDate.parse(dateStr, formatter);
            LocalDate currentDate = LocalDate.now();

            if (dateOfBirth.isAfter(currentDate)) {
                throw new InvalidDataException("Invalid date of birth");
            }
        } catch (DateTimeParseException e) {
            throw new InvalidDataException("Invalid date of birth");
        }
    }

    private static String convertBirthNumberToTenDigits(String birthNumber) {
        return birthNumber.length() <= 10 ? birthNumber : birthNumber.substring(0, 6) + birthNumber.substring(7);
    }
}
