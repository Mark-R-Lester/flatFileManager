package com.mrl.dto;

import java.util.UUID;

import com.mrl.repository.Identifiable;

public class Man extends Identifiable {
	
	public String age;
	
	public Man(UUID id) {
		this.id = id;
	}

}
