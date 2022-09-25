package testing;

import java.io.File;
import java.util.Scanner;

import finished.List;

public class ObjectFilesReader {

	public static List<ObjectModel> read(String path) {
		File[] characterFiles = new File(path).listFiles();
		Scanner scan;
		List<ObjectModel> objectModel = new List<>();
		String objectModelString = "";
		String objectModelType = "";
		String objectModelName = "";
		for (int i = 0; i < characterFiles.length; i++) {
			if (characterFiles[i].getName().endsWith(".png") || characterFiles[i].getName().endsWith(".jpg")) {
				continue;
			}
			try {
				objectModelString = "";
				objectModelName = characterFiles[i].getName().substring(0, characterFiles[i].getName().indexOf("."));
				objectModelType = characterFiles[i].getName().substring(characterFiles[i].getName().indexOf(".") + 1,
						characterFiles[i].getName().length());
				scan = new Scanner(characterFiles[i]);
				String processingString;
				while (scan.hasNextLine()) {
					processingString = scan.nextLine().trim().replace(" ", "");
					if (processingString.split(":")[0].equals("sprite")
							&& !processingString.split(":")[1].equals("none")) {
						processingString = "sprite:" + path + "/" + processingString.split(":")[1];
					}
					objectModelString += processingString + "qwertzuiop";
				}
				if (!objectModelString.contains("sprite")) {
					objectModelString += "sprite:" + path + "/"
							+ characterFiles[i].getName().substring(0, characterFiles[i].getName().indexOf("."))
							+ ".png";
				}
				objectModel.add(new ObjectModel(objectModelName, objectModelType, objectModelString.split("qwertzuiop")));
				scan.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(characterFiles[i].getName() + " is not readable!");
				continue;
			}
		}
		return objectModel;
	}

}
