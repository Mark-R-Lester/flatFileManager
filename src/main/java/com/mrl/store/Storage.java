package com.mrl.store;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.mrl.exception.ResponseCodeException;
import com.mrl.repository.Identifiable;

public class Storage {
	
	
	
	/**
	 * Tests if the path is that of a valid file
	 * 
	 * @param path
	 * @return boolean true if and only if a file exists at path
	 */
	public boolean isFile(String path) {
		File file = new File(path);
		return file.isFile();
	}
	
	/**
	 * If no repository file currently exists create it.
	 * 
	 * @param String path: Path to file
	 * @return boolean true if the repository file is accessible false if not
	 * @throws ResponseCodeException 
	 */
	public void createDirectory(String path) throws ResponseCodeException {
		File file = new File(path);
		if(!file.exists() || !file.isDirectory())
			try {
				Path p = Paths.get(path);
				Files.createDirectory(p);
			} 
      		catch (IOException e) {
      			throw new ResponseCodeException();
      		}
    }
	
	/**
	 * If no repository file currently exists create it.
	 * 
	 * @param String path: Path to file
	 * @return boolean true if the repository file is accessible false if not
	 * @throws ResponseCodeException 
	 */
	public void createFile(String path) throws ResponseCodeException {
		File file = new File(path);
		
		if(!file.exists())
			try {
				file.createNewFile();
			} 
      		catch (IOException e) {
      			throw new ResponseCodeException();
      		}
    }
	
	/**
	 * Saves the provided json to a file at the specified path
	 * 
	 * @param path
	 * @param json
	 * @return
	 * @throws ResponseCodeException 
	 */
	public void writeContentToFile(String path,  Identifiable identifiable) throws ResponseCodeException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(identifiable);
		
		try (FileWriter myWriter = new FileWriter(path)){
		    myWriter.write(json);
		} 
		catch (IOException e) {
			throw new ResponseCodeException();
		}
	}
	
	
	/**
	 * If no repository file currently exists create it.
	 * 
	 * @param String path: Path to file
	 * @return boolean true if the repository file is accessible false if not
	 * @throws ResponseCodeException 
	 */
	public void createFileAndContent(String path, Identifiable identifiable) throws ResponseCodeException {
		createFile(path);
		writeContentToFile(path, identifiable);
    }
	
	
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public boolean deleteFile(String path) {
		File file = new File(path);
		if(!file.isFile()) 
			return true;
		
		return file.delete();
	}
	
	/**
	 * Deletes the directory and specified by path and all content
	 * 
	 * @param path
	 * @return
	 */
	public boolean deleteDirectory(String path) {
	    return deleteDirectory(new File(path));
	}
	
	/**
	 * Deletes all content from  directory specified by path
	 * 
	 * @param path
	 * @return
	 */
	public boolean deleteDirectoryContent(String path) {
		File[] allContents = new File(path).listFiles();
		boolean result = true;
		
		if (allContents != null)
			for (File file : allContents)
				result = deleteDirectory(file);
		
		return result;
	}
	
	/*
	 * @param directoryToBeDeleted
	 * 
	 * @return boolean true if no errors occur
	 */
	private boolean deleteDirectory(File directory) {
	    File[] allContents = directory.listFiles();
	    if (allContents != null)
	        for (File file : allContents)
	            deleteDirectory(file);
	        
	    return directory.delete();
	}
	
	
	
	/**
	 * Deletes all files and only files from the directory specified by path
	 * 
	 * @param path
	 */
	public void deleteFilesOnly(String path) {
	    File[] files = new File(path).listFiles();
	    
	    if(files==null)
	    	return;
	    
        for(File file: files)
        	if (!file.isDirectory())
        		file.delete();
	}
	
	/**
	 * Reads a file  at path and returns an instance of Class T
	 * 
	 * @param <T>
	 * @param Class<T> clazz
	 * @param String path
	 * @return T an instance of the of Class T
	 */
	public <T> T read(Class<T> clazz, String path) throws ResponseCodeException {
		try (FileReader reader = new FileReader(path)) {
			T t = new Gson().fromJson(new JsonReader(reader), clazz);
			return t;
		} 
		catch (JsonIOException | JsonSyntaxException | IOException e) {
			throw new ResponseCodeException();
		}
	}
	
	/**
	 * Reads a file  at path and returns an instance of Class T
	 * 
	 * @param <T>
	 * @param Class<T> clazz
	 * @param String path
	 * @return T an instance of the of Class T
	 */
	public <T> List<T> readAll(Class<T> clazz, String path) throws ResponseCodeException{
		List<T> list = new LinkedList<>();
		File[] files = new File(path).listFiles();
	
		for(File file: files) {
			list.add(read(clazz, file.getPath()));
		}
		return list;
	}

	

}
