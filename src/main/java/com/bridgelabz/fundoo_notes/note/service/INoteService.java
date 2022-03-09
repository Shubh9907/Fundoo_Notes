package com.bridgelabz.fundoo_notes.note.service;

import java.util.Date;

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

	public ApiResponse searchNoteByTitle(String title);

	public ApiResponse unarchieveNote(Integer id, String token);

	public ApiResponse restoreNote(Integer id, String token);

	public ApiResponse remindNote(Integer id, String token, Date date);

	public void sendReminderEmail();

	public ApiResponse pinAndUnpinNote(Integer id, String token);
}
