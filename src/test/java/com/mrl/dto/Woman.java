package com.mrl.dto;

import java.util.UUID;

import com.mrl.repository.Identifiable;

public class Woman extends Identifiable {
	
	public String age;
	
	public Woman(UUID id) {
		this.id = id;
	}

}
