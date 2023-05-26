package eu.epptec.model;

import java.time.LocalDate;
import java.time.Period;

public class PersonWithAge extends Person{
    private int age;

    public PersonWithAge(Person person) {
        super(person.getFirstName(), person.getLastName(), person.getBirthNumber());
        this.age = calculateAge(person.getBirthNumber());
    }

    public int getAge() {
        return age;
    }

    public int calculateAge(String birthNumber){
        int twentyCentury = 1900;
        int twentyFirstCentury = 2000;

        String yearPrefix = birthNumber.substring(0, 2);
        String month = birthNumber.substring(2, 4);
        String day = birthNumber.substring(4, 6);

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        int currentDay = LocalDate.now().getDayOfMonth();

        int year = Integer.parseInt(yearPrefix);
        //max age the app predict is 98 years
        //if the person was born in a year that has the same ending as the current year,
        //convert the year of birth to the current year
        int century = (year > currentYear - twentyFirstCentury) ? twentyCentury : twentyFirstCentury;
        int fullYear = century + year;

        LocalDate birthDate = LocalDate.of(fullYear, Integer.parseInt(month), Integer.parseInt(day));
        LocalDate currentDate = LocalDate.of(currentYear, currentMonth, currentDay);
        Period period = Period.between(birthDate, currentDate);

        return period.getYears();
    }
}
