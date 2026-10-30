package com.johnpetros.tdd.person;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonDAOTest {
    private final PersonDAO dao = new PersonDAO();

    @Test
    void acceptsValidPersonWithMinimumEmailParts() {
        Person person = new Person(1, "Ana Silva", 30, List.of(new Email(1, "a@b.c")));

        assertEquals(List.of(), dao.isValidToInclude(person));
    }
}
