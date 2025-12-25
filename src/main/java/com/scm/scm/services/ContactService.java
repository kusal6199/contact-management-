package com.scm.scm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.scm.scm.entities.Contact;
import com.scm.scm.entities.User;

public interface ContactService {

    Contact saveContact(Contact contact);

    Optional<Contact> updateContact(Contact contact);

    List<Contact> getAll();

    Contact getContactById(String id);

    void deleteContact(String id);

    Page<Contact> searchByName(String name, int page, int size, String sortBy, String dirrection);

    Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String dirrection);

    Page<Contact> seachByPhone(String phone, int page, int size, String sortBy, String dirrection);

    List<Contact> getContactByUserId(String id);

    Page<Contact> findByUser(User user, int page, int size, String sortBy, String dirrection);

}
