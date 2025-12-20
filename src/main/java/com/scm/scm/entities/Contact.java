package com.scm.scm.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class Contact {

    @Id
    private String id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;

    @Lob
    @Column(columnDefinition = "Text")
    private String description;
    @Lob
    @Column(columnDefinition = "Text")

    private String picture;
    private boolean favorite = false;
    private String websiteLink;
    private String linkedInLink;

    private String cloudinaryImagePublicId;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SocialLink> socialLinks = new ArrayList<>();

}
