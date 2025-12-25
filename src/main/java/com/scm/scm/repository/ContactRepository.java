package com.scm.scm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.scm.scm.entities.Contact;
import com.scm.scm.entities.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

    List<Contact> findByUser(User user);

    Page<Contact> findByUser(User user, Pageable pageable);

    // @Query("SELECT c FROM Contact c WHERE c.user.id = :userId ")
    List<Contact> findByUser_UserId(String userId);

    Page<Contact> findByNameContaining(String name, Pageable pageable);

    Page<Contact> findByEmailContaining(String email, Pageable pageable);

    Page<Contact> findByPhoneNumberContaining(String phone, Pageable pageable);

}
