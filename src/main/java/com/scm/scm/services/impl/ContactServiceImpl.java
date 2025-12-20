package com.scm.scm.services.impl;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.scm.entities.Contact;
import com.scm.scm.helper.ResourceNotFoundException;
import com.scm.scm.repository.ContactRepository;
import com.scm.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepository contactRepository;

    @Override
    public Contact saveContact(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepository.save(contact);
    }

    @Override
    public Optional<Contact> updateContact(Contact contact) {

        Contact contact2 = contactRepository.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("contact is not found "));

        contact2.setAddress(contact.getAddress());
        contact2.setEmail(contact.getAddress());
        contact2.setFavorite(contact.isFavorite());
        contact2.setDescription(contact.getDescription());
        contact2.setLinkedInLink(contact.getLinkedInLink());
        contact2.setPhoneNumber(contact.getPhoneNumber());
        contact2.setName(contact.getName());

        contact2.setWebsiteLink(contact.getWebsiteLink());
        contact2.setPicture(contact.getPicture());
        contact2.setSocialLinks(contact.getSocialLinks());

        return Optional.ofNullable(contactRepository.save(contact2));
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("contact with id " + id + " is not found "));

    }

    @Override
    public void deleteContact(String id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no such contact found"));
        contactRepository.delete(contact);
    }

    @Override
    public List<Contact> searchContact(String name, String email, String phoneNumber) {
        return null;
    }

    @Override
    public List<Contact> getContactByUserId(String id) {
        List<Contact> contacts = contactRepository.findByUser_UserId(id);
        return contacts;
    }

}
