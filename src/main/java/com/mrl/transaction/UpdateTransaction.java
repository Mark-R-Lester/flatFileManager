package com.mrl.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.SerializationUtils;

import com.mrl.exception.ResponseCodeException;
import com.mrl.repository.Identifiable;

public class UpdateTransaction  extends Transaction {
	
	
	private HashMap<String, HashMap<UUID, String >> new_class_uuid_path_mappings = new HashMap<>();
	private HashMap<String, HashMap<UUID, String >> old_class_uuid_path_mappings = new HashMap<>();
	
	public HashMap<String, HashMap<UUID, String >> getOldClassUuidPathMappings() {
		return SerializationUtils.clone(old_class_uuid_path_mappings);
	}
	
	public HashMap<String, HashMap<UUID, String >> getNewClassUuidPathMappings() {
		return SerializationUtils.clone(new_class_uuid_path_mappings);
	}
	
	
	
	/** 
	 * {inherit}
	 */
	@Override
	public void open(List<List<Identifiable>> lists) throws ResponseCodeException {
		if(lists.isEmpty())
			return;
		
		for(List<Identifiable> list: lists)
			mapPaths(list);
		
		saveTransaction();
		
		for(List<Identifiable> list: lists) 
			createContent(list, new_class_uuid_path_mappings);
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
			HashMap<UUID, String> new_uuid_path_mappings =  get_uuid_path_map(identifiable, new_class_uuid_path_mappings);
			new_uuid_path_mappings.put(identifiable.id, newLocation );
			
			String oldLocation = indexes.get(identifiable);
			HashMap<UUID, String> old_uuid_path_mappings =  get_uuid_path_map(identifiable, old_class_uuid_path_mappings);
			old_uuid_path_mappings.put(identifiable.id, oldLocation );
		});
	}
	
	/** 
	 * {inherit}
	 */
	@Override
	public void commit() {
		updateIndexes(new_class_uuid_path_mappings);
		deleteFiles(old_class_uuid_path_mappings);
		deleteTransaction();
	}
	
	
	
	
	/** 
	 * {inherit}
	 */
	@Override
	public void rollBack() {
		updateIndexes(old_class_uuid_path_mappings);
		deleteFiles(new_class_uuid_path_mappings);
		deleteTransaction();
		
	}




	


	

}
