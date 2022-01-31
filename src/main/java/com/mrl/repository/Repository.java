package com.mrl.repository;

import java.util.List;
import java.util.UUID;

import com.mrl.exception.ResponseCodeException;
import com.mrl.index.Indexes;
import com.mrl.store.Storage;
import com.mrl.transaction.InsertTransaction;
import com.mrl.transaction.Transaction;
import com.mrl.transaction.UpdateTransaction;

public class Repository {
	
	
	private Storage storage = new Storage();
	Indexes indexes = Indexes.getInstance();

	/**
	 * Gets all objects of type T from the repository
	 * 
	 * @param <T>
	 * @param clazz
	 * @return List<T>
	 */
	public <T> List<T> getAll(Class<T> clazz) {
		
		try {
			return storage.readAll(clazz, "./");
		} catch (ResponseCodeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Finds and return the persisted data identified by it's class and id (uuid).
	 * 
	 * @param <T>
	 * @param clazz
	 * @param uuid
	 * @return Class T 
	 */
	public <T> T getByUUID(Class<T> clazz, UUID uuid) {
		String path = indexes.get(clazz, uuid);
		try {
			return storage.read(clazz, path);
		} catch (ResponseCodeException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	
	/**
	 * Saves object t of Class T to the repository specified by path
	 * 
	 * @param entries
	 */
	protected void insert(List<List<Identifiable>> entries) {
		Transaction transaction = new InsertTransaction();
		try {
			transaction.open(entries);
		} catch (ResponseCodeException e) {
			transaction.rollBack();
		}
		transaction.commit();
	}
	
	
	
	/**
	 * 
	 * @param entries
	 */
	public void update(List<List<Identifiable>> entries) {
		Transaction transaction = new UpdateTransaction();
		try {
			transaction.open(entries);
		} catch (ResponseCodeException e) {
		    transaction.rollBack();
		}
		transaction.commit();
	}
	
	public boolean update() {
		return true;
	}
	
	

}
