package com.mrl.transaction;


import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mrl.base.TestData;
import com.mrl.dto.Man;
import com.mrl.exception.ResponseCodeException;
import com.mrl.index.Indexes;
import com.mrl.repository.Identifiable;
import com.mrl.store.Storage;

class UpdateTransactionTest extends TestData {
	
	private final static Storage STORAGE = new Storage();
	private final static String TRANSACTION_PATH = ROOT_PATH + "transaction/";
	private final static List<List<Identifiable>> LISTS = new ArrayList<>();
	
	
	private UpdateTransaction transaction = null;
	private Indexes mockIndexes = mock(Indexes.class);
	
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		STORAGE.createDirectory(ROOT_PATH);
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		//STORAGE.deleteDirectoryContent(ROOT_PATH);
	}

	@BeforeEach
	void setUp() throws Exception {
		STORAGE.deleteDirectoryContent(TRANSACTION_PATH);
		STORAGE.deleteDirectoryContent(MAN_PATH);
		
		transaction = new UpdateTransaction();
		transaction.setRootPath(ROOT_PATH);
		transaction.setIndex(mockIndexes);
		
		List<Identifiable> insertList = Arrays.asList(MAN_1, MAN_2);
		LISTS.add(insertList);
		
		STORAGE.createDirectory(MAN_PATH);
		STORAGE.createDirectory(TRANSACTION_PATH);
		
	}

	@AfterEach
	void tearDown() throws Exception {
	
	}
	
	@Test
	void openAndCommit() throws ResponseCodeException {
		getInitialIndex();
		
		MAN_1.age = "1";
		MAN_2.age = "2";
		STORAGE.createFileAndContent(MAN_PATH_1, MAN_1);
		STORAGE.createFileAndContent(MAN_PATH_2, MAN_2);
		
		assertNumberOfMen(2);
		assertNumberOfTransactions(0);
	
		MAN_1.age = "3";
		MAN_2.age = "4";
		
		List<Identifiable> insertList = Arrays.asList(MAN_1, MAN_2);
		LISTS.add(insertList);
		
		transaction.open(LISTS);
		assertNumberOfMen(4);
		assertOldPathsAreCorrect();
		assertNewPathsAreCorrect();
		assertNumberOfTransactions(1);
		
		
		HashMap<UUID, String> newmap = transaction.getNewClassUuidPathMappings().get(Man.class.getName());
		
		transaction.commit();
		assertNumberOfMen(2);
		assertNumberOfTransactions(0);
		String newPath1 = newmap.get(MAN_1.id);
		String newPath2 = newmap.get(MAN_2.id);
		Man man1 = STORAGE.read(Man.class, newPath1);
		Man man2 = STORAGE.read(Man.class, newPath2);
		assertEquals(man1.age, "3");
		assertEquals(man2.age, "4");
		
		
	}
	
	@Test
	void openAndRollbackTest() throws ResponseCodeException {
		getInitialIndex();
	
		MAN_1.age = "1";
		MAN_2.age = "2";
		STORAGE.createFileAndContent(MAN_PATH_1, MAN_1);
		STORAGE.createFileAndContent(MAN_PATH_2, MAN_2);
		
		assertNumberOfMen(2);
		assertNumberOfTransactions(0);
	
		MAN_1.age = "3";
		MAN_2.age = "4";
		
		List<Identifiable> insertList = Arrays.asList(MAN_1, MAN_2);
		LISTS.add(insertList);
		
		transaction.open(LISTS);
		assertNumberOfMen(4);
		assertOldPathsAreCorrect();
		assertNewPathsAreCorrect();
		assertNumberOfTransactions(1);
		
		
		HashMap<UUID, String> oldmap = transaction.getOldClassUuidPathMappings().get(Man.class.getName());		
		
		transaction.rollBack();
		
		assertNumberOfMen(2);
		assertNumberOfTransactions(0);
		String oldPath1 = oldmap.get(MAN_1.id);
		String oldPath2 = oldmap.get(MAN_2.id);
		Man man1 = STORAGE.read(Man.class, oldPath1);
		Man man2 = STORAGE.read(Man.class, oldPath2);
		assertEquals(man1.age, "1");
		assertEquals(man2.age, "2");
	}
	
	/*
	 * 
	 */
	private void assertNumberOfMen(int number) throws ResponseCodeException {
		List<Man> men = STORAGE.readAll(Man.class, MAN_PATH);
		assertEquals(men.size(), number);
	}
	
	/*
	 * 
	 */
	private void assertNumberOfTransactions(int number) throws ResponseCodeException {
		List<InsertTransaction> transactions = STORAGE.readAll(InsertTransaction.class, TRANSACTION_PATH);
		assertEquals(transactions.size(), number);
	}
	
	/*
	 * 
	 */
	private void assertOldPathsAreCorrect() {
		HashMap<UUID, String> oldmap = transaction.getOldClassUuidPathMappings().get(Man.class.getName());
		String oldPath1 = oldmap.get(MAN_1.id);
		String oldPath2 = oldmap.get(MAN_2.id);
		assertEquals(oldPath1, MAN_PATH_1);
		assertEquals(oldPath2, MAN_PATH_2);
	}
	
	/*
	 * 
	 */
	private void assertNewPathsAreCorrect() {
		HashMap<UUID, String> newmap = transaction.getNewClassUuidPathMappings().get(Man.class.getName());
		String newPath1 = newmap.get(MAN_1.id);
		String newPath2 = newmap.get(MAN_2.id);
		assertNotEquals(newPath1, MAN_PATH_1);
		assertNotEquals(newPath2, MAN_PATH_2);
		assertEquals(newmap.size(), 2);
		
		assertTrue(STORAGE.isFile(newPath1));
		assertTrue(STORAGE.isFile(newPath2));
	}
	
	/*
	 * 
	 */
	private void getInitialIndex() {
		when(mockIndexes.get(MAN_1)).thenReturn(MAN_PATH_1);
		when(mockIndexes.get(MAN_2)).thenReturn(MAN_PATH_2);
	}
	
	

}
