package GUI;
import java.awt.Desktop;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import Tools.Download;

@SuppressWarnings("serial")
public class DownloadGUI extends JFrame implements ActionListener {

	Panel panel;
	JProgressBar pb;
	Download download;
	JButton downloadButton, cancelButton, openlocation;

	public DownloadGUI(Download download_) {
		super(download_.getFileName());
		download = download_;
		setBounds((1300 - 410) / 2, (700 - 150) / 2, 410, 150);
		setLayout(null);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		panel = new Panel();

		pb = new JProgressBar(0, download.getFileSize());
		pb.setBounds(50, 20, 300, 30);
		pb.setVisible(true);
		pb.setStringPainted(true);
		panel.add(pb);
		add(pb);

		downloadButton = new JButton("Start");
		downloadButton.setBounds(50, 65, 80, 30);
		downloadButton.setVisible(true);
		downloadButton.addActionListener(this);
		panel.add(downloadButton);
		add(downloadButton);

		openlocation = new JButton("Location");
		openlocation.setBounds(150, 65, 100, 30);
		openlocation.setVisible(false);
		openlocation.addActionListener(this);
		panel.add(openlocation);
		add(openlocation);

		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(270, 65, 80, 30);
		cancelButton.setVisible(true);
		cancelButton.addActionListener(this);
		panel.add(cancelButton);
		add(cancelButton);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (download.isRunning())
					download.cancelDownload();
			}
		});

		add(panel);
	}

	void previewProgress() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (download.isRunning()) {
					pb.setValue(download.getProgressValue());
					pb.setString(download.getProgress());
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
					}
				}
				if (download.isDone()) {
					pb.setValue(download.getProgressValue());
					pb.setString("Done");
					downloadButton.setText("Open");
					cancelButton.setText("Close");
					openlocation.setVisible(true);
				}
			}
		}).start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == downloadButton) { // Start & Resume Download and
												// deal with Pause
			if (download.isDone()) {
				try {
					Desktop.getDesktop().open(new File(download.getFilePath()));
				} catch (IOException e1) {
					System.out.println(e1.toString());
				}
				// this.dispose();
			} else if (download.isRunning()) {
				downloadButton.setText("Resume");
				download.pauseDownload();
			} else {
				downloadButton.setText("Pause");
				download.resumeDownload();
				previewProgress();
			}
		} else if (e.getSource() == cancelButton) { // Cancel Download
			if (!download.isDone())
				download.cancelDownload();
			dispose();
		} else if (e.getSource() == openlocation) {
			try {
				Desktop.getDesktop().open(new File(download.getParentPath()));
			} catch (IOException e1) {
				System.out.println(e1.toString());
			}
		}

	}

}
