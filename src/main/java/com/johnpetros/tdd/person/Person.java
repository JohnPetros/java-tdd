package com.johnpetros.tdd.person;

import java.util.List;

public record Person(int id, String name, int age, List<Email> emails) {
}
