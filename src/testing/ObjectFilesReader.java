package testing;

import java.io.File;
import java.util.Scanner;

import finished.List;

public class ObjectFilesReader {

	public static List<ObjectModel> read(String path) {
		File[] objectFiles = new File(path).listFiles();
		Scanner scan;
		List<ObjectModel> objectModel = new List<>();
		String objectModelString = "";
		String objectModelType = "";
		String objectModelName = "";
		boolean hasSprite = true;
		for (int i = 0; i < objectFiles.length; i++) {
			hasSprite = true;
			if (objectFiles[i].getName().endsWith(".png") || objectFiles[i].getName().endsWith(".jpg") || objectFiles[i].getName().endsWith(".config")) {
				continue;
			}
			try {
				objectModelString = "";
				objectModelName = objectFiles[i].getName().substring(0, objectFiles[i].getName().indexOf("."));
				objectModelType = objectFiles[i].getName().substring(objectFiles[i].getName().indexOf(".") + 1,
						objectFiles[i].getName().length());
				scan = new Scanner(objectFiles[i]);
				String processingString;
				while (scan.hasNextLine()) {
					processingString = scan.nextLine().trim().replace(" ", "");
					if (processingString.startsWith("++")) {
						while (scan.hasNextLine()) {
							if (processingString.equals("++sprite++")) {
								if (scan.hasNextLine()) {
									processingString = scan.nextLine().trim().replace(" ", "");
									if (processingString.equals("none")) {
										hasSprite = false;
										continue;
									}
									processingString = "sprite++" + path + "/" + processingString;
									objectModelString += processingString;
									if (scan.hasNextLine()) {
										processingString = scan.nextLine().trim().replace(" ", "");
										while (!processingString.startsWith("++") && scan.hasNextLine()) {
											objectModelString += processingString;
											processingString = scan.nextLine().trim().replace(" ", "");
										}
									}
								}
								objectModelString += "qwertzuiop";
								continue;
							}
							objectModelString += processingString.substring(2);
							processingString = scan.nextLine().trim().replace(" ", "");
							while (!processingString.startsWith("++")) {
								objectModelString += processingString;
								if (!scan.hasNextLine()) {
									break;
								}
								processingString = scan.nextLine().trim().replace(" ", "");
							}
							objectModelString += "qwertzuiop";
						}
					}
				}
				if (!objectModelString.contains("sprite") && hasSprite) {
					objectModelString += "sprite++" + path + "/"
							+ objectFiles[i].getName().substring(0, objectFiles[i].getName().indexOf("."))
							+ ".png";
				}
				objectModel.add(new ObjectModel(objectModelName, objectModelType, objectModelString.split("qwertzuiop")));
				scan.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(objectFiles[i].getName() + " is not readable!");
				continue;
			}
		}
		return objectModel;
	}

}
