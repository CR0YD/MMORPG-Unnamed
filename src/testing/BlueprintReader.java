package testing;

import java.io.File;
import java.util.Scanner;

import finished.List;

public class BlueprintReader {
	
	private String path;
	
	public BlueprintReader(String path) {
		this.path = path;
	}
	
	public List<String[]> read() {
		File[] objectFiles = new File(path).listFiles();
		Scanner scan;
		List<String[]> modelObjects = new List<>();
		String blueprintString = "";
		for (int i = 0; i < objectFiles.length; i++) {
			try {
				blueprintString = "name:" + objectFiles[i].getName().split(".object")[0] + "qwertzuiop";
				scan = new Scanner(objectFiles[i]);
				String processingString;
				while (scan.hasNextLine()) {
					processingString = scan.nextLine().trim().replace(" ", "");
					if (processingString.split(":")[0].equals("sprite") && !processingString.split(":")[1].equals("none")) {
						processingString = "sprite:" + path + "/" + processingString.split(":")[1];
					}
					blueprintString += processingString + "qwertzuiop";
				}
				modelObjects.add(blueprintString.split("qwertzuiop"));
				scan.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(objectFiles[i].getName() + " is not readable!");
				continue;
			}
		}
		return modelObjects;
	}
	
}
