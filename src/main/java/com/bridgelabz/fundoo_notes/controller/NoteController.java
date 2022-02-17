package com.bridgelabz.fundoo_notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo_notes.dto.NoteDto;
import com.bridgelabz.fundoo_notes.entity.Note;

import com.bridgelabz.fundoo_notes.repository.NotesRepository;
import com.bridgelabz.fundoo_notes.service.INoteService;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class NoteController {

	@Autowired
	INoteService iNoteService;
	
	@Autowired
	NotesRepository noteRepo;
	
	@GetMapping("/notes")
    public ResponseEntity<ApiResponse> getAllUser() {
		ApiResponse response = iNoteService.getNotes();
        return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
	}
	
	@PostMapping("/note")
	public ResponseEntity<ApiResponse> postNote(@RequestHeader String token, @RequestBody NoteDto noteDto) {
		ApiResponse response = iNoteService.postNote(token, noteDto);
		return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
	}
	
	@PutMapping("/updateNote/{id}")
	public ResponseEntity<ApiResponse> updateNote(@PathVariable Integer id, @RequestBody NoteDto noteDto) {
		ApiResponse response = iNoteService.updateNote(id,noteDto);
		return new ResponseEntity<ApiResponse>(response , HttpStatus.OK);	
	}
	
	@DeleteMapping("/note/{id}")
	public ResponseEntity<ApiResponse> deleteNote(@PathVariable Integer id) {
		ApiResponse response = iNoteService.deleteNote(id);
		return new ResponseEntity<ApiResponse>(response , HttpStatus.OK);	
	}
}
