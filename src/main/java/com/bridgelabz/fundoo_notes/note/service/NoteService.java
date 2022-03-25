package com.bridgelabz.fundoo_notes.note.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo_notes.configuration.RabbitConfiguration;
import com.bridgelabz.fundoo_notes.entity.Note;
import com.bridgelabz.fundoo_notes.entity.User;
import com.bridgelabz.fundoo_notes.note.dto.NoteDto;
import com.bridgelabz.fundoo_notes.note.exception.NoteException;
import com.bridgelabz.fundoo_notes.note.repository.NotesRepository;
import com.bridgelabz.fundoo_notes.user.exception.UserException;
import com.bridgelabz.fundoo_notes.user.repository.UserRepository;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;
import com.bridgelabz.fundoo_notes.utility.ElasticSearchService;
import com.bridgelabz.fundoo_notes.utility.JwtToken;
import com.bridgelabz.fundoo_notes.utility.MailService;

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

	@Autowired
	RabbitTemplate template;

	@Autowired
	MailService mailService;

	@Autowired
	private ElasticSearchService elasticService;

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

		Note savedNote = noteRepo.save(note);
//		savedNote.setUser(null);	

//		System.out.println(savedNote);

		elasticService.createNote(savedNote);
		apiResponse = new ApiResponse(environment.getProperty("note.addedSuccessfully"), 1, null);

		return apiResponse;
	}

	@Override
	public ApiResponse getNotes() {
		apiResponse = new ApiResponse("Note List", 1, noteRepo.findAll());
//		List<Note> noteList = elasticService.searchData();
//		apiResponse = new ApiResponse("Note List", 1, noteList);
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
			note.setInTrash(noteDto.isInTrash());
			note.setInArchieve(noteDto.isInArchieve());
			note.setNoteColor(noteDto.getNoteColor());
			note.setReminder(noteDto.getReminder());
			note.setPined(noteDto.isPined());
			

			Note savedNote = noteRepo.save(note);

			elasticService.updateNote(savedNote);

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
			elasticService.deleteNote(id);
			apiResponse = new ApiResponse("Note Successfully Deleted", 1, note);
		}
		return apiResponse;
	}

	@Override
	public ApiResponse trashAndRestoreNote(Integer id, String token) {
		boolean flag = false;
		String email = jwtToken.decodeToken(token);
		Note note = noteRepo.findById(id).orElseThrow(() -> new NoteException());
		User user = userRepo.findByEmail(email).orElseThrow(() -> new UserException());
		if (note != null && user != null) {
			if (!note.isInTrash()) {
				note.setInTrash(true);
				note.setTrashedDate(new Date());
				note.setInArchieve(false);
				note.setPined(false);
				flag = true;
			} else {
				note.setInTrash(false);
				note.setTrashedDate(null);
			}
		}
		Note savedNote = noteRepo.save(note);
		elasticService.updateNote(savedNote);
		if (flag) {
			apiResponse = new ApiResponse("Note Trashed", 1, null);
		}else apiResponse = new ApiResponse("Note Restored", 1, null);
		
		return apiResponse;
	}

	@Override
	public ApiResponse archieveAndUnarchiveNote(Integer id, String token) {
		boolean flag = false;
		String email = jwtToken.decodeToken(token);
		Note note = noteRepo.findById(id).orElseThrow(() -> new NoteException());
		User user = userRepo.findByEmail(email).orElseThrow(() -> new UserException());
		if (note != null && user != null) {
			if (!note.isInArchieve()) {
				note.setInArchieve(true);
				note.setInTrash(false);
				note.setPined(false);
				flag = true;
			} else
				note.setInArchieve(false);
		}
		Note savedNote = noteRepo.save(note);
		elasticService.updateNote(savedNote);
		if (flag) {
			apiResponse = new ApiResponse("Note successfully Archieves", 1, null);
		} else
			apiResponse = new ApiResponse("Note successfully Unarchieved", 1, null);

		return apiResponse;
	}

	@Override
	public void deleteTrashedNote() {
		java.util.List<Note> noteList = (java.util.List<Note>) noteRepo.findAll();
		noteList.stream().filter(note -> note.isInTrash()).forEach(note -> {
			LocalDate date = note.getTrashedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate currentDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate date2 = date.plusDays(7);
			if (date2.compareTo(currentDate) < 0) {
				noteRepo.deleteById(note.getId());
				elasticService.deleteNote(note.getId());
			}
		});
	}

	@Override
	public ApiResponse searchNoteByKeyword(String key, String token)
			throws IllegalArgumentException, UnsupportedEncodingException {
//		apiResponse = new ApiResponse("Note List", 1, noteRepo.findByTitle(key));
		List<Note> noteList = elasticService.searchAll(key, token);
		apiResponse = new ApiResponse("Note List", 1, noteList);
		return apiResponse;
	}

	@Override
	public ApiResponse remindNote(Integer id, String token, Date date) {

		String email = jwtToken.decodeToken(token);
		Note note = noteRepo.findById(id).orElseThrow(() -> new NoteException());
		User user = userRepo.findByEmail(email).orElseThrow(() -> new UserException());
		if (note != null && user != null) {
			note.setReminder(date);
		}
		Note savedNote = noteRepo.save(note);
		elasticService.updateNote(savedNote);

		apiResponse = new ApiResponse("Note successfully added to Reminder", 1, null);

		return apiResponse;
	}

	@Override
	public void sendReminderEmail() {
		java.util.List<Note> noteList = (java.util.List<Note>) noteRepo.findAll();
		noteList.stream().filter(note -> note.getReminder() != null).forEach(note -> {
			LocalDate remindDate = note.getReminder().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate currentDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if (remindDate.compareTo(currentDate) < 0) {
				mailService.sendNoteReminderMail(note.getUser().getEmail(), note);
			}
		});
	}

	@Override
	public ApiResponse pinAndUnpinNote(Integer id, String token) {
		boolean flag = false;
		String email = jwtToken.decodeToken(token);
		Note note = noteRepo.findById(id).orElseThrow(() -> new NoteException());
		User user = userRepo.findByEmail(email).orElseThrow(() -> new UserException());
		if (note != null && user != null) {
			if (note.isPined() == false) {
				note.setPined(true);
				note.setInArchieve(false);
				note.setInTrash(false);
				flag = true;
			} else
				note.setPined(false);
		}
		Note savedNote = noteRepo.save(note);
		elasticService.updateNote(savedNote);

		if (flag) {
			apiResponse = new ApiResponse("Note successfully pinned", 1, null);
		} else
			apiResponse = new ApiResponse("Note successfully unpinned", 1, null);

		return apiResponse;
	}
}
