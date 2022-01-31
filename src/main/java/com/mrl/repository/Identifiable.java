package com.mrl.repository;

import java.util.Objects;
import java.util.UUID;

public class Identifiable {
	
	public UUID id;


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identifiable other = (Identifiable) obj;
		return Objects.equals(id, other.id);
	}
	
	

}
