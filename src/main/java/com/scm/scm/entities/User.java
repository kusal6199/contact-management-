package com.scm.scm.entities;

public class User {

    private int userId;
    private String name;
    private String emalil;
    private String password;
    private String about;
    private String profilePic;
    private String phoneNumber;

    private boolean enable = false;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    private String provider;
    private String providerUserId;
}
