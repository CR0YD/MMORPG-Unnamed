package testing;

import java.io.File;

import finished.List;

public class Map {
	
	public final List<Field> FIELDS;
	
	public Map() {
		FIELDS = new List<>();
		File[] mapFolder = new File("assets/map").listFiles();
		for (int i = 0; i < mapFolder.length; i++) {
			FIELDS.add(new Field(mapFolder[i].getPath()));
		}
		
	}
	
}
