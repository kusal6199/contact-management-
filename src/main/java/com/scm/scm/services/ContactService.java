package com.scm.scm.services;

import java.util.List;
import java.util.Optional;

import com.scm.scm.entities.Contact;

public interface ContactService {

    Contact saveContact(Contact contact);

    Optional<Contact> updateContact(Contact contact);

    List<Contact> getAll();

    Contact getContactById(String id);

    void deleteContact(String id);

    List<Contact> searchContact(String name, String email, String phoneNumber);

    List<Contact> getContactByUserId(String id);

}
