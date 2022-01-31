package com.mrl.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.mrl.repository.Identifiable;

/**
 * Stores an index for each of type of object stored
 * Each index if mapped to the objects class name
 * Each index contains mappings from the class instance id (uuid) to a file location;
 * 
 * @author mrles
 *
 */
public class Indexes {
	
	private Map<String, Map<UUID, String>> indexes = new HashMap<>();
	private static final Indexes INSTANCE = new Indexes(); 
	
	
	private Indexes() {
		
	}
	
	public static  Indexes getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param entires
	 */
	public synchronized void put(String clazz, Map<UUID, String> entries) {
		Map<UUID, String> index = indexes.get(clazz);
		if(index == null) {
			index = new LinkedHashMap<>();
			indexes.put(clazz, index);
		}
		
		for(Entry<UUID, String> e: entries.entrySet()) {
			if (e.getKey() != null && e.getValue() != null)
			    index.put(e.getKey(), e.getValue());
		}
	}
	
	/**
	 * get(Class<T> clazz, UUID uuid)
	 * 
	 * Gets a the file path for the identifiable
	 * 
	 * @param UUID uuid
	 * @return String or null if no path is found
	 */
	public String get(Identifiable identifiable) {
		return get(identifiable.getClass(), identifiable.id);
	}
	
	/**
	 * get(Class<T> clazz, UUID uuid)
	 * 
	 * Gets a the file path for the class member with the specified uuid;
	 * 
	 * @param UUID uuid
	 * @return String or null if no path is found
	 */
	public String get(Class<?> clazz, UUID uuid) {
		Map<UUID, String> index = indexes.get(clazz.getName());
		
		if(index == null)
			return null;
		
		return index.get(uuid);
	}
	
	
	/**
	 * get(Class<T> clazz, UUID uuid)
	 * 
	 * Gets a the file path for the class member with the specified uuid;
	 * 
	 * @param Class<T> clazz
	 * @param UUID uuid
	 * @return String or null if no path is found
	 */
	public void remove(String clazz, Map<UUID, String> entries) {
		Map<UUID, String> index = indexes.get(clazz);
		
		if(index == null)
			return;
		
		for(UUID uuid : entries.keySet()) {
			if (uuid != null)
			    index.remove(uuid);
		}
	}
	
	/**
	 * Returns a copy of the paths for a index defined for the Class parameter
	 * 
	 * 
	 * @param String clazz
	 * @return List<String> 
	 */
	public List<String> getPathsFor(String clazz) {
		Map<UUID, String> index = indexes.get(clazz);
		
		if(index == null)
			return new ArrayList<String>();
		
		return new ArrayList<String>(index.values());
	}
	
	/**
	 * numberOfIndexes
	 * Every different type of object has its own index
	 * 
	 * 
	 * @return int the number of indexes currently tracked
	 */
	public int numberOfIndexes() {
		return indexes.size();
	}
	
	/**
	 * 
	 * 
	 * @param clazz
	 * @return
	 */
	public int indexSizeFor(String clazz) {
		return indexes.get(clazz).size();
	}
	
	/**
	 * 
	 * @param <T>
	 */
	public <T> void clear() {
		 indexes = new HashMap<>();
	}

}
