package Tools;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class Download {

	final public static int INTI = 0;
	final public static int ACTIVE = 1;
	final public static int PAUSE = 2;
	final public static int RESUME = 3;
	final public static int DONE = 4;
	final public static int CANCEL = 5;
	final public static int ERROE = Integer.MIN_VALUE;

	private String path, fileName, fileUrl, fileStringSize;
	private int state, fileSize, progress;
	private File target;
	private FileOutputStream fos;
	private InputStream inputStream;
	private URLConnection connection;

	public Download(String path_, String fileName_, String url_, int state_) {
		try {
			path = path_;
			fileName = "test" + fileName_;
			fileUrl = url_;
			state = state_;
			fileSize = Init();
			fileStringSize = getStringSize(fileSize);
			System.out.println("The File Size Is " + fileSize + " byte");
			switch (state) {
			case INTI:
				fos = new FileOutputStream(path + fileName + ".TEMP");
				inputStream = connection.getInputStream();
				progress = 0;
				// startDownload();
				break;
			case PAUSE:
				break;
			default:
				state = ERROE;
				break;
			}
		} catch (Exception e) {
			state = ERROE;
		}
	}

	public boolean isRunning() {
		return state == ACTIVE ? true : false;
	}

	public boolean isDone() {
		return state == DONE ? true : false;
	}

	public void pauseDownload() {
		// pause dwonload
		setState(PAUSE);
	}

	public void cancelDownload() {
		// Cancel dwonload
		setState(CANCEL);
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
		return (((float) progress / (float) fileSize) * 100) + "%";
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
		setState(RESUME);
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
			state = ERROE;
		}
		return ERROE;
	}

	private void downloding() throws IOException {
		int temp;
		while (state == ACTIVE) {
			temp = downloadByte();
			if (temp == -1 || temp == ERROE)
				break;
			storeByte((byte) temp);
			progress++;
			// System.out.println(progress);
		}
		if (state == ACTIVE) {
			done();
		} else if (state == PAUSE)
			pause();
		else if (state == CANCEL) {
			cancel();
		}

	}

	private void pause() {

	}

	private void resume() {
		setState(ACTIVE);
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
			setState(DONE);
			String name = target.getName().substring(0,
					target.getName().lastIndexOf(".TEMP"));
			File dest = new File(path + name);
			target.renameTo(dest);
			target = dest;
		} catch (IOException e) {
		}
	}

	private void setState(int state_) {
		state = state_;
	}

	private int downloadByte() {
		try {
			return inputStream.read();
		} catch (IOException e) {
			state = ERROE;
		}
		return ERROE;
	}

	private void storeByte(byte temp) {
		try {
			fos.write(temp);
		} catch (IOException e) {
			state = ERROE;
		}
	}

}
//public void startDownload() {
	// setState(ACTIVE);
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