package com.bridgelabz.fundoo_notes.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoo_notes.note.service.INoteService;

@Component
public class EventScheduler {
	
	@Autowired
	INoteService iNoteService;

	
	@Scheduled(fixedRate = 1000 * 60 * 60 * 24)
	public void DeleteTrashNotes() {
		iNoteService.deleteTrashedNote();
		System.out.println("Cron Job Works");
	}

}
