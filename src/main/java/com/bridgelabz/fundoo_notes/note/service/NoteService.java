package com.bridgelabz.fundoo_notes.note.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo_notes.entity.Note;
import com.bridgelabz.fundoo_notes.entity.User;
import com.bridgelabz.fundoo_notes.note.dto.NoteDto;
import com.bridgelabz.fundoo_notes.note.exception.NoteException;
import com.bridgelabz.fundoo_notes.note.repository.NotesRepository;
import com.bridgelabz.fundoo_notes.user.exception.UserException;
import com.bridgelabz.fundoo_notes.user.repository.UserRepository;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;
import com.bridgelabz.fundoo_notes.utility.JwtToken;

import antlr.collections.List;

@Service
public class NoteService implements INoteService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	NotesRepository noteRepo;

	@Autowired
	Environment environment;

	@Autowired
	ApiResponse apiResponse;

	@Autowired
	JwtToken jwtToken;

	@Autowired
	UserRepository userRepo;

	ApiResponse userNotFound = new ApiResponse("User Not Found", 601, null);

	@Override
	public ApiResponse postNote(String token, NoteDto noteDto) {
		String email = jwtToken.decodeToken(token);
		User user = userRepo.findByEmail(email).orElseThrow(() -> new NoteException());
		if (user.getNotes() == null) {
			user.setNotes(new ArrayList<>());
		}
		Note note = modelMapper.map(noteDto, Note.class);
		user.getNotes().add(note);

		note.setUser(user);

		noteRepo.save(note);

		apiResponse = new ApiResponse(environment.getProperty("note.addedSuccessfully"), 1, null);

		return apiResponse;
	}

	@Override
	public ApiResponse getNotes() {
		apiResponse = new ApiResponse("Note List", 1, noteRepo.findAll());
		return apiResponse;
	}

	@Override
	public ApiResponse updateNote(Integer id, NoteDto noteDto) {
		Note note = noteRepo.findById(id).orElseThrow(() -> new NoteException());
		if (note != null) {
			if (noteDto.getTitle() != null) {
				note.setTitle(noteDto.getTitle());
			}
			if (noteDto.getNoteBody() != null) {
				note.setNoteBody(noteDto.getNoteBody());
			}
			noteRepo.save(note);

			apiResponse = new ApiResponse(environment.getProperty("note.updateSuccessfully"), 1, null);

			return apiResponse;

		} else {
			apiResponse = new ApiResponse(environment.getProperty("user.invalidDetails"), 2, null);

			return apiResponse;
		}
	}

	@Override
	public ApiResponse deleteNote(Integer id, String token) {
		Note note = noteRepo.findById(id).orElseThrow(() -> new NoteException());
		String email = jwtToken.decodeToken(token);

		User user = userRepo.findByEmail(email).orElseThrow(() -> new UserException());
		if (note != null && user != null) {
			noteRepo.deleteById(id);
			apiResponse = new ApiResponse("Note Successfully Deleted", 1, note);
		}
		return apiResponse;
	}

	@Override
	public ApiResponse trashNote(Integer id, String token) {
		String email = jwtToken.decodeToken(token);
		Note note = noteRepo.findById(id).orElseThrow(() -> new NoteException());
		User user = userRepo.findByEmail(email).orElseThrow(() -> new UserException());
		if (note != null && user != null) {
			note.setInTrash(true);
			note.setTrashedDate(new Date());
		}
		noteRepo.save(note);
		apiResponse = new ApiResponse("Note successfully moved to trash", 1, null);

		return apiResponse;
	}

	@Override
	public ApiResponse archieveNote(Integer id, String token) {
		String email = jwtToken.decodeToken(token);
		Note note = noteRepo.findById(id).orElseThrow(() -> new NoteException());
		User user = userRepo.findByEmail(email).orElseThrow(() -> new UserException());
		if (note != null && user != null) {
			note.setInArchieve(true);
		}
		noteRepo.save(note);
		apiResponse = new ApiResponse("Note successfully moved to Archieves", 1, null);

		return apiResponse;
	}

	@Override
	public void deleteTrashedNote() {
		java.util.List<Note> noteList = noteRepo.findAll();
		noteList.stream().filter(note -> note.isInTrash()).forEach(note -> {
			LocalDate date = note.getTrashedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate currentDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate date2 = date.plusDays(7);
			if (date2.compareTo(currentDate) < 0) {
				noteRepo.deleteById(note.getId());
			}
		});
	}
}
