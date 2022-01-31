package com.mrl.transaction;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import com.mrl.base.TestData;
import com.mrl.dto.Man;
import com.mrl.exception.ResponseCodeException;
import com.mrl.index.Indexes;
import com.mrl.repository.Identifiable;
import com.mrl.store.Storage;



@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class InsertTransactionTest extends TestData {
	
	private final static Storage STORAGE = new Storage();
	private final static String TRANSACTION_PATH = ROOT_PATH + "transaction/";
	private final static List<List<Identifiable>> LISTS = new ArrayList<>();
	
	
	private Transaction transaction = null;
	private Indexes mockIndexes = mock(Indexes.class);
	
	@Captor
	ArgumentCaptor<String> stringCaptor;
	
	@Captor
	ArgumentCaptor<Map<UUID, String>> mapCaptor;
	
	

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
		STORAGE.deleteDirectoryContent(TRANSACTION_PATH);
		STORAGE.deleteDirectoryContent(MAN_PATH);
		
		transaction = new InsertTransaction();
		transaction.setRootPath(ROOT_PATH);
		transaction.setIndex(mockIndexes);
		
		List<Identifiable> list = Arrays.asList(MAN_3, MAN_4);
		LISTS.add(list);
		
		STORAGE.createDirectory(MAN_PATH);
		STORAGE.createDirectory(TRANSACTION_PATH);
		STORAGE.createFileAndContent(MAN_PATH_1, MAN_1);
		STORAGE.createFileAndContent(MAN_PATH_2, MAN_2);
	}

	@AfterEach
	void tearDown() throws Exception {
	
	}
	
	@Test
	void openAndCommitTest() throws ResponseCodeException {
		transaction.open(LISTS);
		
		List<Man> men = STORAGE.readAll(Man.class, MAN_PATH);
		List<InsertTransaction> transactions = STORAGE.readAll(InsertTransaction.class, TRANSACTION_PATH);
		
		assertEquals(men.size(), 4);
		assertEquals(transactions.size(), 1);
		assertEquals(transactions.get(0).getUuidNewPathMappings(Man.class).size(), 2);
		
		transaction.commit();
		
		verify(mockIndexes).put(stringCaptor.capture(), mapCaptor.capture());
		
		assertEquals(stringCaptor.getValue(), Man.class.getName());
		assertEquals(mapCaptor.getValue().size(), 2);
	}
	
	@Test
	void openAndRollbackTest() throws ResponseCodeException {
		transaction.open(LISTS);
		
		List<Man> men = STORAGE.readAll(Man.class, MAN_PATH);
		List<InsertTransaction> transactions = STORAGE.readAll(InsertTransaction.class, TRANSACTION_PATH);
		
		assertEquals(men.size(), 4);
		assertEquals(transactions.size(), 1);
		assertEquals(transactions.get(0).getUuidNewPathMappings(Man.class).size(), 2);
		
		transaction.rollBack();
		
		men = STORAGE.readAll(Man.class, MAN_PATH);
		transactions = STORAGE.readAll(InsertTransaction.class, TRANSACTION_PATH);
		
		assertEquals(men.size(), 2);
		assertEquals(transactions.size(), 0);
	}
	
	
	
	

}
