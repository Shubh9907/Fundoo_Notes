package com.bridgelabz.fundoo_notes.utility;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo_notes.entity.Note;
import com.bridgelabz.fundoo_notes.note.repository.NotesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticSearchService {
	
	String INDEX = "notedb";
	String TYPE = "notetype";
	
	@Autowired
	private RestHighLevelClient client;

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	NotesRepository noteRepo;
	
	@Autowired
	JwtToken jwtToken;


	public Note createNote (Note note) {
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = mapper.convertValue(note, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX , TYPE, String.valueOf(note.getId())).source(dataMap);
		System.out.println(dataMap);
			
		try {
			client.index(indexRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return note;
	}
	
	
	public Note updateNote(Note note) {
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = mapper.convertValue(note, Map.class);
		UpdateRequest updateRequest = new UpdateRequest(INDEX , TYPE, String.valueOf(note.getId()));
		
		updateRequest.doc(dataMap);
		
		try {
			client.update(updateRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return note;
	}
	
	public void deleteNote(int noteId) {
		@SuppressWarnings("unchecked")
		DeleteRequest deleteRequest = new DeleteRequest(INDEX , TYPE, String.valueOf(noteId));		
		try {
			client.delete(deleteRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public List<Note> searchData() {
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;		
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getSearchResult(searchResponse);
	}


	private List<Note> getSearchResult(SearchResponse searchResponse) {
		SearchHit[] searchHit = searchResponse.getHits().getHits();
		List<Note> notes = new ArrayList<>();
		
		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
				.forEach(hit -> notes.add(mapper.convertValue(hit.getSourceAsMap(),Note.class)));
		}
		return notes;
	}
	
	public List<Note> searchAll(String query, String token) throws IllegalArgumentException, UnsupportedEncodingException{
		
		String email = jwtToken.decodeToken(token);
		SearchRequest searchRequest = new SearchRequest(INDEX).types(TYPE);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
											.must(QueryBuilders.queryStringQuery("*" +query+ "*")
													.analyzeWildcard(true).field("title",2.0f).field("noteBody").field("title"));
//											.filter(QueryBuilders.termsQuery("email", String.valueOf(email)));
				
		searchSourceBuilder.query(queryBuilder);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;		
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getSearchResult(searchResponse);
	}
	
}
