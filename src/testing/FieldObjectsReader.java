package testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FieldObjectsReader {

	private String path;
	
	public FieldObjectsReader(String path) {
		this.path = path;
	}
	
	public String[] read() {
		Scanner scan;
		try {
			scan = new Scanner(new File(path + ".objects"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		String objects = "";
		while (scan.hasNextLine()) {
			objects += scan.nextLine().trim().replace(" ", "") + "qwertzuiop";
		}
		scan.close();
		return objects.split("qwertzuiop");
	}
	
}
