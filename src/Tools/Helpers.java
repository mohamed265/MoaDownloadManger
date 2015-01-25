package Tools;

import java.io.File;

public class Helpers {

	public static void test(String Message) {
		System.out.println("test " + Message);
	}

	public static boolean isValidURL(String url) {
		if (url.contains("http://") || url.contains("https://")) {
			if (url.contains(".")) {
				return true;
			}
		}
		return false;
	}

	public static String genrateName(String url) {

		String result = "";
		try {
			result = url.substring(url.lastIndexOf('/') + 1);
		} catch (Exception e) {
		}
		return result;
	}

	public static String getDirectory(String path) {
		String osName = System.getProperty("os.name");
		// test(osName);
		if (osName.contains("Win")) {
			path = path.substring(0, path.lastIndexOf('\\') + 1);
		} else {
			path = path.substring(0, path.lastIndexOf('/') + 1);
		}
		return path;
	}

	public static boolean isDirectory(String path) {
		return new File(path).isDirectory();
	}

	public static File getCurrentDirectory() {
		return new File(System.getProperty("user.dir"));
	}
}
