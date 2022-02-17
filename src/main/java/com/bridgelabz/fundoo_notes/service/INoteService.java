package com.bridgelabz.fundoo_notes.service;

import com.bridgelabz.fundoo_notes.dto.NoteDto;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;

public interface INoteService {
	
	public ApiResponse getNotes();
	
	public ApiResponse postNote(String token, NoteDto noteDto);

	public ApiResponse updateNote(Integer id, NoteDto noteDto);

	public ApiResponse deleteNote(Integer id);
}
