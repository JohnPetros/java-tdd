package com.johnpetros.tdd.person;

import java.util.Arrays;
import java.util.Collections;
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

    @Test
    void rejectsMissingEmailAssociation() {
        Person person = new Person(1, "Ana Silva", 30, null);

        assertEquals(List.of("Person must have at least one email."), dao.isValidToInclude(person));
    }

    @Test
    void rejectsEmptyEmailAssociation() {
        Person person = new Person(1, "Ana Silva", 30, List.of());

        assertEquals(List.of("Person must have at least one email."), dao.isValidToInclude(person));
    }

    @Test
    void acceptsMultipleAssociatedEmails() {
        Person person = new Person(1, "Ana Silva", 30, List.of(
            new Email(1, "ana@example.com"), new Email(2, "ana@school.edu")
        ));

        assertEquals(List.of(), dao.isValidToInclude(person));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "a@b.c", "ana.silva@example.com", "ana+school@example.com",
        "ana@mail.example.com", "ana@exemplo.com.br"
    })
    void acceptsEmailWithNonemptyLocalDomainAndSuffix(String email) {
        Person person = new Person(1, "Ana Silva", 30, List.of(new Email(1, email)));

        assertEquals(List.of(), dao.isValidToInclude(person));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        "ana.example.com", "ana@example", "@example.com", "ana@.com", "ana@example.",
        "ana@@example.com", "ana@example@school.com", "ana@exam ple.com",
        "ana @example.com", " ana@example.com", "ana@example.com ",
        "ana@example..com", "ana@.example.com", "ana@example.com.",
        "ana@\texample.com", "ana@\u2003example.com", "a@b.c\n"
    })
    void rejectsMalformedEmailNames(String email) {
        Person person = new Person(1, "Ana Silva", 30, List.of(new Email(1, email)));

        assertEquals(List.of("Email at position 1 must match local@domain.suffix."),
            dao.isValidToInclude(person));
    }

    @Test
    void rejectsNullEmailObject() {
        Person person = new Person(1, "Ana Silva", 30, Collections.singletonList(null));

        assertEquals(List.of("Email at position 1 must match local@domain.suffix."),
            dao.isValidToInclude(person));
    }

    @Test
    void checksEmailAfterAValidEmail() {
        Person person = new Person(1, "Ana Silva", 30, List.of(
            new Email(1, "ana@example.com"), new Email(2, "invalid")
        ));

        assertEquals(List.of("Email at position 2 must match local@domain.suffix."),
            dao.isValidToInclude(person));
    }

    @Test
    void reportsInvalidEmailEvenWhenAnotherEmailIsValid() {
        Person person = new Person(1, "Ana Silva", 30, List.of(
            new Email(1, "invalid"), new Email(2, "ana@example.com")
        ));

        assertEquals(List.of("Email at position 1 must match local@domain.suffix."),
            dao.isValidToInclude(person));
    }

    @Test
    void reportsEachInvalidEmailInAssociationOrder() {
        Person person = new Person(1, "Ana Silva", 30, List.of(
            new Email(1, "invalid"), new Email(2, "ana@example.com"), new Email(3, "also-invalid")
        ));

        assertEquals(List.of(
            "Email at position 1 must match local@domain.suffix.",
            "Email at position 3 must match local@domain.suffix."
        ), dao.isValidToInclude(person));
    }

    @Test
    void rejectsMissingPersonWithoutThrowing() {
        assertEquals(List.of("Person must be provided."), dao.isValidToInclude(null));
    }

    @Test
    void collectsNameAgeAndMissingEmailErrors() {
        Person person = new Person(1, "Ana", 0, List.of());

        assertEquals(List.of(
            "Name must contain at least two parts made only of letters.",
            "Age must be between 1 and 200 inclusive.",
            "Person must have at least one email."
        ), dao.isValidToInclude(person));
    }

    @Test
    void collectsNameAgeAndEveryEmailError() {
        Person person = new Person(1, null, 201, Arrays.asList(null, new Email(2, "invalid")));

        assertEquals(List.of(
            "Name must contain at least two parts made only of letters.",
            "Age must be between 1 and 200 inclusive.",
            "Email at position 1 must match local@domain.suffix.",
            "Email at position 2 must match local@domain.suffix."
        ), dao.isValidToInclude(person));
    }

    @Test
    void doesNotCarryErrorsBetweenValidations() {
        List<String> firstErrors = dao.isValidToInclude(personWith("Ana", 30));

        assertEquals(List.of(), dao.isValidToInclude(personWith("Ana Silva", 30)));
        assertEquals(List.of("Name must contain at least two parts made only of letters."), firstErrors);
    }

    private Person personWith(String name, int age) {
        return new Person(1, name, age, List.of(new Email(1, "ana@example.com")));
    }
}
