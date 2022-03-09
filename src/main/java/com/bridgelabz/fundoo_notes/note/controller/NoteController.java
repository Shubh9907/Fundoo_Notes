package com.bridgelabz.fundoo_notes.note.controller;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo_notes.note.dto.NoteDto;
import com.bridgelabz.fundoo_notes.note.repository.NotesRepository;
import com.bridgelabz.fundoo_notes.note.service.INoteService;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
//@EnableElasticsearchRepositories
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
	
	@GetMapping("/searchnote")
    public ResponseEntity<ApiResponse> searchNote(@RequestParam String title) {
		ApiResponse response = iNoteService.searchNoteByTitle(title);
        return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
	}
	
	@PutMapping("/updateNote/{id}")
	public ResponseEntity<ApiResponse> updateNote(@PathVariable Integer id, @RequestBody NoteDto noteDto) {
		ApiResponse response = iNoteService.updateNote(id,noteDto);
		return new ResponseEntity<ApiResponse>(response , HttpStatus.OK);	
	}
	
	@DeleteMapping("/note/{id}")
	public ResponseEntity<ApiResponse> deleteNote(@PathVariable Integer id, @RequestHeader String token) {
		ApiResponse response = iNoteService.deleteNote(id, token);
		return new ResponseEntity<ApiResponse>(response , HttpStatus.OK);	
	}
	
	@PutMapping("/trashNote/{id}")
	public ResponseEntity<ApiResponse> trashNote(@PathVariable Integer id, @RequestHeader String token) {
		ApiResponse response = iNoteService.trashNote(id, token);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
	
	@PutMapping("/archieveNote/{id}")
	public ResponseEntity<ApiResponse> archieveNote(@PathVariable Integer id, @RequestHeader String token) {
		ApiResponse response = iNoteService.archieveNote(id, token);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
	
	@PutMapping("/unarchieveNote/{id}")
	public ResponseEntity<ApiResponse> unarchieveNote(@PathVariable Integer id, @RequestHeader String token) {
		ApiResponse response = iNoteService.unarchieveNote(id, token);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
	
	@PutMapping("/restoneNote/{id}")
	public ResponseEntity<ApiResponse> restoreNote(@PathVariable Integer id, @RequestHeader String token) {
		ApiResponse response = iNoteService.restoreNote(id, token);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
	
	@PutMapping("/notereminder/{id}")
	public ResponseEntity<ApiResponse> remindNote(@PathVariable Integer id, @RequestHeader String token, @RequestParam Date date) {
		ApiResponse response = iNoteService.remindNote(id, token, date);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
	
	@PutMapping("/pinnote/{id}")
	public ResponseEntity<ApiResponse> remindNote(@PathVariable Integer id, @RequestHeader String token) {
		ApiResponse response = iNoteService.pinAndUnpinNote(id, token);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
}
