package com.bridgelabz.fundoo_notes.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

//import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Note {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String title;
	private String noteBody;
	private boolean inTrash;
	private boolean inArchieve;
	private Date trashedDate;
	private String noteColor;
	private Date reminder;
	private boolean pined;
	
	@ManyToOne
	@JsonIgnore
	private User user;

}
