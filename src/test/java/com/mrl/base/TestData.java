package com.mrl.base;

import java.util.UUID;

import com.mrl.dto.Man;

public class TestData {
	
	public final static String ROOT_PATH = "src/test/resources/repo/";
	public final static String MAN_PATH = "src/test/resources/repo/com.mrl.dto.Man/";
	public final static String WOMAN_PATH = "src/test/resources/repo/com.mrl.dto.Woman/";
	
	public final static UUID MAN_UUID_1 = UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120001");
	public final static UUID MAN_UUID_2 = UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120002");
	public final static UUID MAN_UUID_3 = UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120003");
	public final static UUID MAN_UUID_4 = UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120004");
	
	public final static Man MAN_1 = new Man(MAN_UUID_1);
	public final static Man MAN_2 = new Man(MAN_UUID_2);
	public final static Man MAN_3 = new Man(MAN_UUID_3);
	public final static Man MAN_4 = new Man(MAN_UUID_4);
	
	public final static String MAN_PATH_1 = (ROOT_PATH + "com.mrl.dto.Man/297ddf4e-7941-11ec-90d6-0242ac120011");
	public final static String MAN_PATH_2 = (ROOT_PATH + "com.mrl.dto.Man/297ddf4e-7941-11ec-90d6-0242ac120022");
	

}
