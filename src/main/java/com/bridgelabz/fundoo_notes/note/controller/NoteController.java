package com.bridgelabz.fundoo_notes.note.controller;


import java.io.UnsupportedEncodingException;
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

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
//@EnableElasticsearchRepositories
public class NoteController {

	@Autowired
	INoteService iNoteService;
	
	@GetMapping("/notes")
	@ApiOperation("This api is used for getting all the records from Note")
    public ResponseEntity<ApiResponse> getAllUser() {
		ApiResponse response = iNoteService.getNotes();
        return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
	}
	
	@PostMapping("/note")
	@ApiOperation("This api is used for creating a new note")
	public ResponseEntity<ApiResponse> postNote(@RequestHeader String token, @RequestBody NoteDto noteDto) {
		ApiResponse response = iNoteService.postNote(token, noteDto);
		return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
	}
	
	@GetMapping("/searchnote")
	@ApiOperation("This api is used for serching a note based on keyword")
    public ResponseEntity<ApiResponse> searchNote(@RequestParam String key, @RequestHeader String token) throws IllegalArgumentException, UnsupportedEncodingException {
		ApiResponse response = iNoteService.searchNoteByKeyword(key, token);
        return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
	}
	
	@PutMapping("/updateNote/{id}")
	@ApiOperation("This api is used for updating a note")
	public ResponseEntity<ApiResponse> updateNote(@PathVariable Integer id, @RequestBody NoteDto noteDto) {
		ApiResponse response = iNoteService.updateNote(id,noteDto);
		return new ResponseEntity<ApiResponse>(response , HttpStatus.OK);	
	}
	
	@DeleteMapping("/note/{id}")
	@ApiOperation("This api is used for deleting a note")
	public ResponseEntity<ApiResponse> deleteNote(@PathVariable Integer id, @RequestHeader String token) {
		ApiResponse response = iNoteService.deleteNote(id, token);
		return new ResponseEntity<ApiResponse>(response , HttpStatus.OK);	
	}
	
	@PutMapping("/trashNote/{id}")
	@ApiOperation("This api is used for trashing and restoring a note")
	public ResponseEntity<ApiResponse> trashAndRestoreNote(@PathVariable Integer id, @RequestHeader String token) {
		ApiResponse response = iNoteService.trashAndRestoreNote(id, token);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
	
	@PutMapping("/archieveNote/{id}")
	@ApiOperation("This api is used for archiving and unarchiving a note")
	public ResponseEntity<ApiResponse> archieveNote(@PathVariable Integer id, @RequestHeader String token) {
		ApiResponse response = iNoteService.archieveAndUnarchiveNote(id, token);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
	
	@PutMapping("/notereminder/{id}")
	@ApiOperation("This api is used for setting reminder for a note")
	public ResponseEntity<ApiResponse> remindNote(@PathVariable Integer id, @RequestHeader String token, @RequestParam Date date) {
		ApiResponse response = iNoteService.remindNote(id, token, date);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
	
	@PutMapping("/pinnote/{id}")
	@ApiOperation("This api is used for pinning and unpinning a note")
	public ResponseEntity<ApiResponse> pinAndUnPinNote(@PathVariable Integer id, @RequestHeader String token) {
		ApiResponse response = iNoteService.pinAndUnpinNote(id, token);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
}
