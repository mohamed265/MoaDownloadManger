import Tools.*;
import GUI.*;

public class MainClassDownloaad {

	static String genrateName(String url) {

		String result = "";
		try {
			result = url.substring(url.lastIndexOf('/') + 1);
		} catch (Exception e) {
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		// String url =
		// "http://news.sciencemag.org/sites/default/files/styles/thumb_article_l/public/sn-moa.jpg";
		// String url =
		// "http://codeforces.com/static/images/codeforces-logo-with-telegram.png";
		// //
		// "http://ms11.sm3na.com/1071/Sm3na_com_34619.mp3";//
		String url = "http://ms2.sm3na.com/105/Sm3na_com_15511.mp3";
		String name = genrateName(url);
		String path = "C:\\Users\\mohamed265\\Desktop\\";

		Download test = new Download(path, name, url, Download.INTI);

		new DownloadGUI(test);

	}
}
