package com.bridgelabz.fundoo_notes.note.repository;

import java.util.Optional;

//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo_notes.entity.Note;

@Repository
public interface NotesRepository extends JpaRepository<Note, Integer> {
//public interface NotesRepository extends ElasticsearchRepository<Note, Integer> {

	Optional<Note> findById(Integer id);

	Optional<Note> findByTitle(String title);
}
