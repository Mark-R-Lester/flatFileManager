package com.mrl.store;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mrl.base.TestData;
import com.mrl.dto.Man;
import com.mrl.exception.ResponseCodeException;



class StorageTest extends TestData{
	
	private final static Storage STORAGE = new Storage();
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		STORAGE.createDirectory(ROOT_PATH);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		STORAGE.deleteDirectoryContent(ROOT_PATH);
	}

	@BeforeEach
	void setUp() throws Exception {
		STORAGE.deleteDirectoryContent(ROOT_PATH);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void canCreateDirectory() throws ResponseCodeException {
		STORAGE.createDirectory(MAN_PATH);
		
		File[] allContents = new File(ROOT_PATH).listFiles();
		
		assertEquals(allContents.length, 1);
		assertTrue(allContents[0].isDirectory());
		assertEquals(allContents[0].getName(), "Man");
	}
	
	
	@Test
	void canCreateFile() throws ResponseCodeException {
		STORAGE.createDirectory(MAN_PATH);
		STORAGE.createFile(MAN_PATH + MAN_1.id);
		
		File[] allContents = new File(MAN_PATH).listFiles();
		
		assertEquals(allContents.length, 1);
		assertEquals(allContents[0].getName(), MAN_1.id.toString());
		assertTrue(!allContents[0].isDirectory());
	}
	
	@Test
	void canWriteToReadFromFile() throws ResponseCodeException {
		STORAGE.createDirectory(MAN_PATH);
		STORAGE.createFile(MAN_PATH + MAN_1.id);
		MAN_1.age = "10";
		STORAGE.writeContentToFile(MAN_PATH + MAN_1.id, MAN_1);
		
		Man man = STORAGE.read(Man.class, MAN_PATH + MAN_1.id);
		
		assertEquals(man.age, "10");
		
	}
	

	@Test
	void canCreateWriteAndReadFromFile() throws ResponseCodeException {
		STORAGE.createDirectory(MAN_PATH);
		MAN_1.age = "10";
		STORAGE.createFileAndContent(MAN_PATH + MAN_1.id, MAN_1);
		
		Man man = STORAGE.read(Man.class, MAN_PATH + MAN_1.id);
		assertEquals(man.age, "10");
	}
	
	@Test
	void canDeleteEmptyDirectory() throws ResponseCodeException {
		STORAGE.createDirectory(MAN_PATH);
		
		File[] allContents = new File(ROOT_PATH).listFiles();
		assertEquals(allContents.length, 1);
		
		STORAGE.deleteDirectory(MAN_PATH);
		allContents = new File(ROOT_PATH).listFiles();
		assertEquals(allContents.length, 0);
	}
	
	@Test
	void canDeleteDirectoryWithContent() throws ResponseCodeException {
		STORAGE.createDirectory(MAN_PATH);
		File[] allContents = new File(ROOT_PATH).listFiles();
		assertEquals(allContents.length, 1);
		
		STORAGE.createDirectory(MAN_PATH + "1");
		STORAGE.createDirectory(MAN_PATH + "2");
		STORAGE.createDirectory(MAN_PATH + "3");
		allContents = new File(MAN_PATH).listFiles();
		assertEquals(allContents.length, 3);
		
		STORAGE.deleteDirectory(MAN_PATH);
		allContents = new File(ROOT_PATH).listFiles();
		assertEquals(allContents.length, 0);
	}
	
	@Test
	void canDeleteDirectoryContentOnly() throws ResponseCodeException {
		STORAGE.createDirectory(MAN_PATH);
		File[] allContents = new File(ROOT_PATH).listFiles();
		assertEquals(allContents.length, 1);
		
		STORAGE.createDirectory(MAN_PATH + "1");
		STORAGE.createDirectory(MAN_PATH + "2");
		STORAGE.createDirectory(MAN_PATH + "3");
		allContents = new File(MAN_PATH).listFiles();
		assertEquals(allContents.length, 3);
		
		STORAGE.deleteDirectoryContent(MAN_PATH);
		
		allContents = new File(ROOT_PATH).listFiles();
		assertEquals(allContents.length,  1);
		
		allContents = new File(MAN_PATH).listFiles();
		assertEquals(allContents.length,  0);
	}
	
	@Test
	void canDeleteFileFromDirectoryOnly() throws ResponseCodeException {
		STORAGE.createDirectory(MAN_PATH);
		File[] allContents = new File(ROOT_PATH).listFiles();
		assertEquals(allContents.length, 1);
		
		STORAGE.createFile(MAN_PATH  + MAN_1.id);
		STORAGE.createFile(MAN_PATH  + MAN_2.id);
		STORAGE.createDirectory(MAN_PATH + "1");
		STORAGE.createDirectory(MAN_PATH + "2");
		allContents = new File(MAN_PATH).listFiles();
		assertEquals(allContents.length, 4);
		
		STORAGE.deleteFilesOnly(MAN_PATH);
		allContents = new File(MAN_PATH).listFiles();
		assertEquals(allContents.length, 2);
	}
	

}
