package com.bridgelabz.fundoo_notes.note.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.bridgelabz.fundoo_notes.note.dto.NoteDto;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;

public interface INoteService {
	
	public ApiResponse getNotes();
	
	public ApiResponse postNote(String token, NoteDto noteDto);

	public ApiResponse updateNote(Integer id, NoteDto noteDto);

	public ApiResponse deleteNote(Integer id, String token) ;

	public ApiResponse trashAndRestoreNote(Integer id, String token);

	public ApiResponse archieveAndUnarchiveNote(Integer id, String token);
	
	public void deleteTrashedNote();

	public ApiResponse searchNoteByKeyword(String key, String token) throws IllegalArgumentException, UnsupportedEncodingException;

	public ApiResponse remindNote(Integer id, String token, Date date);

	public void sendReminderEmail();

	public ApiResponse pinAndUnpinNote(Integer id, String token);
}
