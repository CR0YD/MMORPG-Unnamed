package testing;

import java.io.File;
import java.util.Scanner;

import finished.List;

public class CharacterReader {

	private String path;

	public CharacterReader(String path) {
		this.path = path;
	}

	public List<String[]> read() {
		File[] characterFiles = new File(path).listFiles();
		Scanner scan;
		List<String[]> modelCharacter = new List<>();
		String characterString = "";
		for (int i = 0; i < characterFiles.length; i++) {
			if (!characterFiles[i].getName().endsWith(".character")) {
				continue;
			}
			try {
				characterString = "name:" + characterFiles[i].getName().split(".character")[0] + "qwertzuiop";
				scan = new Scanner(characterFiles[i]);
				String processingString;
				while (scan.hasNextLine()) {
					processingString = scan.nextLine().trim().replace(" ", "");
					if (processingString.split(":")[0].equals("sprite")
							&& !processingString.split(":")[1].equals("none")) {
						processingString = "sprite:" + path + "/" + processingString.split(":")[1];
					}
					characterString += processingString + "qwertzuiop";
				}
				if (!characterString.contains("sprite")) {
					characterString += "sprite:" + path + "/" + characterFiles[i].getName().replace(".character", "")
							+ ".png";
				}
				modelCharacter.add(characterString.split("qwertzuiop"));
				scan.close();
			} catch (Exception e) {
//				e.printStackTrace();
				System.err.println(characterFiles[i].getName() + " is not readable!");
				continue;
			}
		}
		return modelCharacter;
	}

}
