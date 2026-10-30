package com.johnpetros.tdd.person;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonDAOTest {
    private final PersonDAO dao = new PersonDAO();

    @Test
    void acceptsValidPersonWithMinimumEmailParts() {
        Person person = new Person(1, "Ana Silva", 30, List.of(new Email(1, "a@b.c")));

        assertEquals(List.of(), dao.isValidToInclude(person));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Ana Silva", "Ana Maria Silva", "João Gonçalves", "Élise Müller",
        "  Ana   Silva  ", "Ana\tSilva", "Ana\u2003Silva"
    })
    void acceptsNamesWithAtLeastTwoLetterParts(String name) {
        assertEquals(List.of(), dao.isValidToInclude(personWith(name, 30)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        " ", "Ana", "Ana3 Silva", "Ana Silva2", "Ana-Maria Silva",
        "Ana O'Neil", "Ana_Silva", "123 456", "\t\n"
    })
    void rejectsNamesWithoutAtLeastTwoLetterOnlyParts(String name) {
        assertEquals(
            List.of("Name must contain at least two parts made only of letters."),
            dao.isValidToInclude(personWith(name, 30))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 100, 199, 200})
    void acceptsAgeWithinInclusiveLimits(int age) {
        assertEquals(List.of(), dao.isValidToInclude(personWith("Ana Silva", age)));
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 201, Integer.MAX_VALUE})
    void rejectsAgeOutsideInclusiveLimits(int age) {
        assertEquals(
            List.of("Age must be between 1 and 200 inclusive."),
            dao.isValidToInclude(personWith("Ana Silva", age))
        );
    }

    private Person personWith(String name, int age) {
        return new Person(1, name, age, List.of(new Email(1, "ana@example.com")));
    }
}
