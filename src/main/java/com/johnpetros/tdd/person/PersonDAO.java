package com.johnpetros.tdd.person;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PersonDAO {
    private static final Pattern NAME_PATTERN = Pattern.compile(
        "\\p{L}+(?:\\s+\\p{L}+)+", Pattern.UNICODE_CHARACTER_CLASS
    );

    public List<String> isValidToInclude(Person person) {
        List<String> errors = new ArrayList<>();
        if (person.name() == null || !NAME_PATTERN.matcher(person.name().strip()).matches()) {
            errors.add("Name must contain at least two parts made only of letters.");
        }
        return errors;
    }
}
