package Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Download {

	final public static int INTI = 0;
	final public static int ACTIVE = 1;
	final public static int PAUSE = 2;
	final public static int RESUME = 3;
	final public static int DONE = 4;
	final public static int CANCEL = 5;
	final public static int ERROE = Integer.MIN_VALUE;

	private String path, fileName, fileUrl, fileStringSize;
	private int status, fileSize, progress;
	private File target;
	private FileOutputStream fos;
	private InputStream inputStream;
	private URLConnection connection;

	public Download(String path_, String fileName_, String url_, int status_) {
		try {
			path = path_;
			fileName = fileName_;
			fileUrl = url_;
			status = status_;
			fileSize = Init();
			fileStringSize = getStringSize(fileSize);
			// Helpers.test("The File Size Is " + fileSize + " byte");
			switch (status) {
			case INTI:
				fos = new FileOutputStream(path + fileName + ".TEMP");
				inputStream = connection.getInputStream();
				progress = 0;
				// startDownload();
				break;
			case PAUSE:
				break;
			default:
				status = ERROE;
				break;
			}
		} catch (Exception e) {
			status = ERROE;
		}
	}

	public boolean skipWith(int temp) {
		try {
			if (inputStream.skip(temp) == temp)
				return true;
		} catch (IOException e) {
			return false;
		}
		return false;
	}

	public String getFileStringSize() {
		return fileStringSize;
	}

	public int getStatus() {
		return status;
	}

	public boolean hasError() {
		return status == ERROE;
	}

	public boolean isRunning() {
		return status == ACTIVE ? true : false;
	}

	public boolean isDone() {
		return status == DONE ? true : false;
	}

	public void pauseDownload() {
		// pause dwonload
		setstatus(PAUSE);
	}

	public void cancelDownload() {
		// Cancel dwonload
		setstatus(CANCEL);
		try {
			Thread.sleep(100);
			cancel();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public int getFileSize() {
		return fileSize;
	}

	public String getFilePath() {
		return path + fileName;
	}

	public String getParentPath() {
		return path;
	}

	public String getFileName() {
		return fileName;
	}

	public String getProgress() {
		return getStringSize(progress) + " / " + fileStringSize
				+ getProgressPercentage();
	}

	public String getProgressPercentage() {
		return String.format("%.2f",
				(((float) progress / (float) fileSize) * 100)) + "%";
	}

	public int getProgressValue() {
		return progress;
	}

	private String getStringSize(int num) {
		if (num < 1024)
			return num + " byte ";
		else if (num < 1048576)
			return String.format("%.2f", (float) num / 1024.) + " KB ";
		else if (num < 1073741824)
			return String.format("%.2f", (float) num / 1048576.) + " MB ";
		else
			return String.format("%.2f", (float) num / 1073741824.) + " GB ";
	}

	public void resumeDownload() {
		setstatus(RESUME);
		resume();
	}

	private int Init() {
		try {
			target = new File(path + fileName + ".TEMP");
			URL url = new URL(fileUrl);
			connection = url.openConnection();
			connection
					.setRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
			return connection.getContentLength();
		} catch (Exception e) {
			status = ERROE;
		}
		return ERROE;
	}

	private void downloding() throws IOException {
		int temp;
		while (status == ACTIVE) {
			temp = downloadByte();
			if (temp == -1 || temp == ERROE)
				break;
			storeByte((byte) temp);
			progress++;
			// System.out.println(progress);
		}
		if (status == ACTIVE) {
			done();
		} else if (status == PAUSE)
			pause();
	}

	private void pause() {

	}

	private void resume() {
		setstatus(ACTIVE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					downloding();
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private void cancel() {
		try {
			fos.close();
		} catch (Exception x) {

		}
		target.delete();
	}

	private void done() {
		try {
			fos.close();
			setstatus(DONE);
			String name = target.getName().substring(0,
					target.getName().lastIndexOf(".TEMP"));
			File dest = new File(path + name);
			target.renameTo(dest);
			target = dest;
		} catch (IOException e) {
		}
	}

	private void setstatus(int status_) {
		status = status_;
	}

	private int downloadByte() {
		try {
			return inputStream.read();
		} catch (IOException e) {
			status = ERROE;
		}
		return ERROE;
	}

	private void storeByte(byte temp) {
		try {
			fos.write(temp);
		} catch (IOException e) {
			status = ERROE;
		}
	}

}
// public void startDownload() {
// setstatus(ACTIVE);
// new Thread(new Runnable() {
// @Override
// public void run() {
// try {
// downloding();
// } catch (Exception e) {
// }
// }
// }).start();
// }
// private static byte[] inputStreamToByteArray(InputStream inStream)
// throws IOException {
// ByteArrayOutputStream baos = new ByteArrayOutputStream();
// byte[] buffer = new byte[8192];
// int bytesRead;
// while ((bytesRead = inStream.read(buffer)) > 0) {
// baos.write(buffer, 0, bytesRead);
// System.out.println(baos.size());
// }
// return baos.toByteArray();
// }
//
// private static void byteArrayToFile(byte[] byteArray, String outFilePath)
// throws Exception {
// FileOutputStream fos = new FileOutputStream(outFilePath);
// fos.write(byteArray);
// fos.close();
// }