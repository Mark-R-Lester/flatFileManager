package com.mrl.index;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mrl.dto.Man;
import com.mrl.dto.Woman;

class IndexTest {
	
	Indexes indexes = Indexes.getInstance();
	
	Map<UUID, String> map1 = new LinkedHashMap<>();
	Map<UUID, String> map2 = new LinkedHashMap<>();
	Map<UUID, String> map3 = new LinkedHashMap<>();
	 
	@BeforeEach
	void setUp() throws Exception {
		indexes.clear();
		map1.put(UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120003"), "1");
		map1.put(UUID.fromString("297de444-7941-11ec-90d6-0242ac120003"), "2");
		map1.put(UUID.fromString("297de57a-7941-11ec-90d6-0242ac120003"), "3");
		map1.put(UUID.fromString("297de692-7941-11ec-90d6-0242ac120003"), "4");
		map1.put(UUID.fromString("297de7b4-7941-11ec-90d6-0242ac120003"), "5");
		map1.put(UUID.fromString("297de930-7941-11ec-90d6-0242ac120003"), "6");
		map1.put(UUID.fromString("297dec82-7941-11ec-90d6-0242ac120003"), "7");
		map1.put(UUID.fromString("297dedf4-7941-11ec-90d6-0242ac120003"), "8");
		map1.put(UUID.fromString("297df02e-7941-11ec-90d6-0242ac120003"), "9");
		map1.put(UUID.fromString("297df150-7941-11ec-90d6-0242ac120003"), "10");
	}

	@Test
	void emptyMapUpdatesCorrectly() {
		indexes.put(Man.class.getName(), map1);
		assertTrue(indexes.indexSizeFor(Man.class.getName()) == map1.size());
	}
	
	@Test
	void populatedMapUpdatesCorreclty() {
		Map<UUID, String> map2 = new LinkedHashMap<>();
		map2.put(UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120003"), "11");
		map2.put(UUID.fromString("297de444-7941-11ec-90d6-0242ac120003"), "12");
		map2.put(UUID.fromString("297de57a-7941-11ec-90d6-0242ac120003"), "13");
		map2.put(UUID.fromString("297de692-7941-11ec-90d6-0242ac120003"), "14");
		map2.put(UUID.fromString("297de7b4-7941-11ec-90d6-0242ac120003"), "15");
		
		indexes.put(Man.class.getName(), map1);
		indexes.put(Man.class.getName(), map2);
		
		String answer = indexes.get(new Man(UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120003")));
		
		assertTrue(indexes.indexSizeFor(Man.class.getName()) == map1.size());
		assertTrue(answer.equals("11"));
	}
	
	@Test
	void populatedMapUpdatesCorrecltyWithNull() {
		Map<UUID, String> map3 = new LinkedHashMap<>();
		map3.put(UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120003"), "11");
		map3.put(UUID.fromString("297de444-7941-11ec-90d6-0242ac120003"), "12");
		map3.put(UUID.fromString("297de57a-7941-11ec-90d6-0242ac120003"), "13");
		map3.put(null, null);
		map3.put(UUID.fromString("297de7b4-7941-11ec-90d6-0242ac120003"), "15");
		 
		indexes.put(Man.class.getName(), map1);
		indexes.put(Man.class.getName(), map3);
		
		String answer = indexes.get(new Man(UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120003")));
		
		assertTrue(indexes.indexSizeFor(Man.class.getName()) == map1.size());
		assertTrue(answer == "11");
	}
	
	@Test
	void populatedMapUpdatesCorrecltyWithNewValues() {
		Map<UUID, String> map3 = new LinkedHashMap<>();
		map3.put(UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120004"), "11");
		map3.put(UUID.fromString("297de444-7941-11ec-90d6-0242ac120004"), "12");
		map3.put(UUID.fromString("297de57a-7941-11ec-90d6-0242ac120004"), "13");
		map3.put(UUID.fromString("297de692-7941-11ec-90d6-0242ac120004"), "14");
		map3.put(UUID.fromString("297de7b4-7941-11ec-90d6-0242ac120004"), "15");
		 
		indexes.put(Man.class.getName(), map1);
		indexes.put(Man.class.getName(), map3);
		
		String answer = indexes.get(new Man(UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120004")));
		
		assertTrue(indexes.indexSizeFor(Man.class.getName()) == 15);
		assertTrue(answer == "11");
	}
	
	@Test
	void differentClassesUseDifferentIndexes() {
		Map<UUID, String> map3 = new LinkedHashMap<>();
		map3.put(UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120004"), "11");
		map3.put(UUID.fromString("297de444-7941-11ec-90d6-0242ac120004"), "12");
		map3.put(UUID.fromString("297de57a-7941-11ec-90d6-0242ac120004"), "13");
		map3.put(UUID.fromString("297de692-7941-11ec-90d6-0242ac120004"), "14");
		map3.put(UUID.fromString("297de7b4-7941-11ec-90d6-0242ac120004"), "15");
		 
		indexes.put(Man.class.getName(), map1);
		indexes.put(Woman.class.getName(), map3);
		
		String man = indexes.get(new Man(UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120003")));
		String woman = indexes.get(new Woman(UUID.fromString("297ddf4e-7941-11ec-90d6-0242ac120004")));
		
		assertTrue(indexes.indexSizeFor(Man.class.getName()) == 10);
		assertTrue(indexes.indexSizeFor(Woman.class.getName()) == 5);
		assertTrue(man == "1");
		assertTrue(woman == "11");
	}
	
	
	@Test
	void removeIndexes() {
		indexes.put(Man.class.getName(), map1);
		assertTrue(indexes.indexSizeFor(Man.class.getName()) == 10);
		
		indexes.remove(Man.class.getName(), map1);
		assertTrue(indexes.indexSizeFor(Man.class.getName()) == 0);
	}

}
