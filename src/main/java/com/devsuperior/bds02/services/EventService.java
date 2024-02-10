package com.devsuperior.bds02.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repository.EventRepository;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {
	
	@Autowired
	private EventRepository repository;
	
	@Transactional(readOnly = true)
	public Page<EventDTO> findAllPaged(Pageable _pageable) {
		Page<Event> list = repository.findAll(_pageable);
		return list.map(x -> new EventDTO(x));
	}
	
	@Transactional
	public EventDTO update(Long _id, EventDTO _dto) {
		try {
			Event entity = repository.getOne(_id);
			
			entity.setName(_dto.getName());
			entity.setDate(_dto.getDate());
			entity.setUrl(_dto.getUrl());
			entity.setCity(new City(_dto.getCityId(), null));
			entity = repository.save(entity);
			
			return new EventDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + _id);
		}		
	}
}
