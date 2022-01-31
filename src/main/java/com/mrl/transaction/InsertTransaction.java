package com.mrl.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.mrl.exception.ResponseCodeException;
import com.mrl.repository.Identifiable;


/**
 * 
 * @author mrles
 *
 */
public class InsertTransaction extends Transaction {
	

	private HashMap<String, HashMap<UUID, String >> class_uuid_path_mappings = new HashMap<>();
	
	public HashMap<UUID, String> getUuidNewPathMappings(Class<?> clazz) {
		return new HashMap<>(class_uuid_path_mappings.get(clazz.getName()));
	}
	
	/**
	 * This method will not allow insert anything if one or more entries already exist.
	 * Internal indexing is created for all entries and will be maintained by the transaction until committed
	 * The data is inserted and can be rolled back at any time before commit is called.
	 * 
	 *
	 * @param List<List<Identifiable>>
	 * @throws Response code exception
	 */
	@Override
	public void open(List<List<Identifiable>> lists) throws ResponseCodeException {
		if(lists.isEmpty())
			return;
		
		for(List<Identifiable> list: lists) 
			ensureEntryDoesNotExist(list);
		
		for(List<Identifiable> list: lists) 
			mapPaths(list);
		
		saveTransaction();
		
		for(List<Identifiable> list: lists) 
			createContent(list, class_uuid_path_mappings);
	}
	
	/*
	 * 
	 * @param list
	 * @throws ResponseCodeException
	 */
	private void ensureEntryDoesNotExist(List<Identifiable> list) throws ResponseCodeException {
		if(list.isEmpty())
			return;
		
		for(Identifiable identifiable: list) {
			
			String path = indexes.get(identifiable);
			if(path != null)
				throw new ResponseCodeException();
		};
	}
	
	/*
	 * 
	 * @param list
	 */
	private void mapPaths(List<Identifiable> list) {
		if(list.isEmpty())
			return;
		
		list.forEach(identifiable -> {
			String newLocation = rootPath + identifiable.getClass().getName() + "/" + UUID.randomUUID();
			HashMap<UUID, String> uuid_path_mappings = get_uuid_path_map(identifiable, class_uuid_path_mappings);
			uuid_path_mappings.put(identifiable.id, newLocation );
		});
	}
	
	/** 
	 * Updates the indexes and deletes the transaction
	 * 
	 * 
	 */
	@Override
	public void commit() {
		updateIndexes(class_uuid_path_mappings);
		deleteTransaction();
	}
	

	/** 
	 * Deletes all files indexed by the transaction
	 * Reverts indexes to it's previous state
	 * Deletes the transaction
	 *
	 */
	@Override
	public void rollBack() {
		removeIndexes();
		deleteFiles(class_uuid_path_mappings);
		deleteTransaction();
	}
	
	/*
	 * 
	 */
	private void removeIndexes() {
		class_uuid_path_mappings.forEach((key, val) -> indexes.remove(key, val));
	}
	
	
	
}
