package com.bridgelabz.fundoo_notes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo_notes.entity.Note;
import com.bridgelabz.fundoo_notes.entity.User;

@Repository
public interface NotesRepository extends JpaRepository<Note , Integer> {

	Optional<Note> findById(Integer id);
	}
