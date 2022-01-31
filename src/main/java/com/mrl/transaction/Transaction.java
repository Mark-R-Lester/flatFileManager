package com.mrl.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.mrl.exception.ResponseCodeException;
import com.mrl.index.Indexes;
import com.mrl.repository.Identifiable;
import com.mrl.store.Storage;

public abstract class Transaction extends Identifiable{

	protected transient String rootPath = "./";
	protected transient Storage storage = new Storage();
	protected transient Indexes indexes = Indexes.getInstance();
	
	
	public void setStorage(Storage storage) {
		this.storage = storage;
	}
	
	public void setIndex(Indexes indexes) {
		this.indexes = indexes;
	}
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
	
	protected void saveTransaction() throws ResponseCodeException {
		if(id == null)
			id = UUID.randomUUID();
		storage.createFileAndContent(rootPath + "transaction/" + id, this);
	}
	
	/*
	 * Deletes this transaction from storage using it's id
	 * 
	 * 
	 */
	protected void deleteTransaction() {
		storage.deleteFile(rootPath + "transaction/" + id);
	}
	
	/*
	 * Iterates over class_uuid_path_mappings and deletes persisted data at each path
	 * 
	 */
	protected void deleteFiles(HashMap<String, HashMap<UUID, String >> class_uuid_path_mappings) {
		class_uuid_path_mappings.forEach((className, uuid_path_mappings) -> {
			uuid_path_mappings.forEach((uuid, path) -> {
				storage.deleteFile(path);
			});
		});
	}
	
	/*
	 * 
	 */
	protected void updateIndexes(HashMap<String, HashMap<UUID, String >> class_uuid_path_mappings) {
		class_uuid_path_mappings.forEach((key, val) -> indexes.put(key, val));
	}
	
	/*
	 * Each identifiable in the list is persisted to storage
	 * The class_uuid_path_mappings contains a persistence path for each identifiable
	 * 
	 * @param list
	 * @throws ResponseCodeException
	 */
	protected void createContent(
			List<Identifiable> list,  
			HashMap<String, HashMap<UUID, String >> class_uuid_path_mappings) throws ResponseCodeException {
		
		if(list.isEmpty())
			return;
		
		for(Identifiable identifiable: list) {
			String newLocation = class_uuid_path_mappings
					.get(identifiable.getClass().getName())
					.get(identifiable.id);
			storage.createFileAndContent(newLocation, identifiable);
		}
	}
	
	/*
	 * Gets a map from the map provided for the class of the identifiable object.
	 * If the map does not exist a new map is created and added to the map provided
	 * the new map will then be returned
	 * 
	 * @param HashMap<String, HashMap<UUID, String >> class_uuid_path_mappings
	 * @param identifiable
	 * @return map<UUID, String> for the class of the identifiable.
	 */
	protected HashMap<UUID, String> get_uuid_path_map(
			Identifiable identifiable, 
			HashMap<String, HashMap<UUID, String >> class_uuid_path_mappings) {
		
		String className = identifiable.getClass().getName();
		
		if(class_uuid_path_mappings.get(className) == null)
			class_uuid_path_mappings.put(className,  new HashMap<UUID, String>());
		
		return class_uuid_path_mappings.get(className);
	}
	
	/**
	 * 
	 * @param List<List<Identifiable>> lists
	 * @throws ResponseCodeException
	 */
	public abstract void open(List<List<Identifiable>> list) throws ResponseCodeException;
	
	/**
	 * 
	 * @param <T>
	 * @return
	 */
	public abstract void commit();
	
	
	/**
	 * 
	 * @param <T>
	 * @return
	 */
	public abstract void rollBack();
	
	
}
