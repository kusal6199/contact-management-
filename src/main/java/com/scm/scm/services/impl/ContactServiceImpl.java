package com.scm.scm.services.impl;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.scm.entities.Contact;
import com.scm.scm.entities.User;
import com.scm.scm.helper.ResourceNotFoundException;
import com.scm.scm.repository.ContactRepository;
import com.scm.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ImageServiceImpl imageServiceImpl;

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

        return Optional.of(contactRepository.save(contact2));
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

        if (contact.getCloudinaryImagePublicId() != null) {
            imageServiceImpl.deleteImage(contact.getCloudinaryImagePublicId());
        }
        contactRepository.delete(contact);
    }

    @Override
    public List<Contact> getContactByUserId(String id) {
        List<Contact> contacts = contactRepository.findByUser_UserId(id);
        return contacts;
    }

    @Override
    public Page<Contact> findByUser(User user, int page, int size, String sortBy, String dirrection) {

        Sort sort = dirrection.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size);
        return contactRepository.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortBy, String dirrection) {

        Sort sort = dirrection.equals("dsc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String dirrection) {
        Sort sort = dirrection.equals("dsc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepository.findByEmailContaining(email, pageable);
    }

    @Override
    public Page<Contact> seachByPhone(String phone, int page, int size, String sortBy, String dirrection) {
        Sort sort = dirrection.equals("dsc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepository.findByPhoneNumberContaining(phone, pageable);
    }
}
