package com.bridgelabz.fundoo_notes.note.service;

import com.bridgelabz.fundoo_notes.note.dto.NoteDto;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;

public interface INoteService {
	
	public ApiResponse getNotes();
	
	public ApiResponse postNote(String token, NoteDto noteDto);

	public ApiResponse updateNote(Integer id, NoteDto noteDto);

	public ApiResponse deleteNote(Integer id, String token) ;

	public ApiResponse trashNote(Integer id, String token);

	public ApiResponse archieveNote(Integer id, String token);
	
	public void deleteTrashedNote();
}
