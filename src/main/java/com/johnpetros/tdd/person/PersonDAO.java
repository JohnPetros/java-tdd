package com.johnpetros.tdd.person;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PersonDAO {
    private static final Pattern NAME_PATTERN = Pattern.compile(
        "\\p{L}+(?:\\s+\\p{L}+)+", Pattern.UNICODE_CHARACTER_CLASS
    );
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "[^\\s@]+@[^\\s@.]+(?:\\.[^\\s@.]+)+", Pattern.UNICODE_CHARACTER_CLASS
    );

    public List<String> isValidToInclude(Person person) {
        List<String> errors = new ArrayList<>();
        if (person.name() == null || !NAME_PATTERN.matcher(person.name().strip()).matches()) {
            errors.add("Name must contain at least two parts made only of letters.");
        }
        if (person.age() < 1 || person.age() > 200) {
            errors.add("Age must be between 1 and 200 inclusive.");
        }
        if (person.emails() == null || person.emails().isEmpty()) {
            errors.add("Person must have at least one email.");
        } else {
            for (int i = 0; i < person.emails().size(); i++) {
                Email email = person.emails().get(i);
                if (email == null || email.name() == null || !EMAIL_PATTERN.matcher(email.name()).matches()) {
                    errors.add("Email at position " + (i + 1) + " must match local@domain.suffix.");
                }
            }
        }
        return errors;
    }
}
