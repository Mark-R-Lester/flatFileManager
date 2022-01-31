package com.mrl.transaction;

public class TransactionLocker {

	
	
	private static final TransactionLocker INSTANCE = new TransactionLocker(); 
	
	
	private TransactionLocker() {
		
	}
	
	public static TransactionLocker getInstance() {
		return INSTANCE;
	}
}
