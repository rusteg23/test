package com.example.myapplication.data;

import java.util.ArrayList;
import java.util.List;

public class Contact {
    public final String id;
    public final String name;
    public final List<ContactEmail> emails;
    public final List<ContactPhone> numbers;

    public Contact(String id, String name) {
        this.id = id;
        this.name = name;
        this.emails = new ArrayList<>();
        this.numbers = new ArrayList<>();
    }

    @Override
    public String toString() {
        String result = name;
        if (numbers.size() > 0) {
            ContactPhone number = numbers.get(0);
            result += " (" + number.number + " - " + number.type + ")";
        }
        if (emails.size() > 0) {
            ContactEmail email = emails.get(0);
            result += " [" + email.address + " - " + email.type + "]";
        }
        return result;
    }

    public void addEmail(String address, String type) {
        emails.add(new ContactEmail(address, type));
    }

    public void addNumber(String number, String type) {
        numbers.add(new ContactPhone(number, type));
    }
}