package com.bridgelabz.fundoo_notes.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String email;
    private String password;
    private String number;
    private Date registerDate;
    private Boolean isVerified = false;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL ,  fetch=FetchType.LAZY )
    private List<Note> notes;
}
