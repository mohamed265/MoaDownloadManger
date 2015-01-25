package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Tools.Download;
import Tools.Helpers;

public class NewDownloadGUI extends JFrame implements ActionListener {

	JPanel panel;
	JLabel urlLabel, nameLabel, destLabel;
	JTextField urlField, nameField, destField;
	JButton verify, ok, cancel, browse;
	JFileChooser fileChooser;

	DownloadGUI downloadGui;
	Download download;

	public NewDownloadGUI() {
		super("New Download");
		setBounds((1300 - 500) / 2, (700 - 200) / 2, 500, 125); // 500 // 210
		setLayout(null);
		setVisible(true);
		panel = new JPanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Boolean flag = true;

		urlLabel = new JLabel("URL");
		urlLabel.setVisible(flag);
		urlLabel.setBounds(20, 10, 50, 30);
		panel.add(urlLabel);
		add(urlLabel);

		urlField = new JTextField();// "http://ms2.sm3na.com/105/Sm3na_com_15511.mp3" for test 
		panel.add(urlField);
		add(urlField);
		urlField.setVisible(flag);
		urlField.setBounds(60, 10, 400, 30);

		nameLabel = new JLabel("Name");
		panel.add(nameLabel);
		add(nameLabel);
		nameLabel.setVisible(!flag);
		nameLabel.setBounds(20, 50, 50, 30);

		nameField = new JTextField();
		panel.add(nameField);
		add(nameField);
		nameField.setVisible(!flag);
		nameField.setBounds(60, 50, 200, 30);

		destLabel = new JLabel("Destination");
		panel.add(destLabel);
		add(destLabel);
		destLabel.setVisible(!flag);
		destLabel.setBounds(20, 90, 100, 30);

		destField = new JTextField();
		panel.add(destField);
		add(destField);
		destField.setVisible(!flag);
		destField.setBounds(90, 90, 370, 30);

		browse = new JButton("Browse");
		panel.add(browse);
		add(browse);
		browse.setVisible(!flag);
		browse.setBounds(360, 50, 100, 30);
		browse.addActionListener(this);

		ok = new JButton("OK");
		panel.add(ok);
		add(ok);
		ok.setVisible(!flag);
		ok.setBounds(360, 130, 100, 30);
		ok.addActionListener(this);

		cancel = new JButton("Cancel");
		panel.add(cancel);
		add(cancel);
		cancel.setVisible(!flag);
		cancel.setBounds(250, 130, 100, 30);
		cancel.addActionListener(this);

		verify = new JButton("Verify");
		panel.add(verify);
		add(verify);
		verify.setVisible(flag);
		verify.setBounds(200, 50, 100, 30);
		verify.addActionListener(this);

		File currentDirectory = Helpers.getCurrentDirectory();
		//Helpers.test(currentDirectory.getPath());
		fileChooser = new JFileChooser(currentDirectory);
		// add(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == verify) {
			String url = urlField.getText();
			// Helpers.test("verify Button");
			if (url.length() != 0) {
				if (Helpers.isValidURL(url)) {
					setBounds((1300 - 500) / 2, (700 - 200) / 2, 500, 210);
					boolean flag = false;
					verify.setVisible(flag);
					urlField.setEnabled(flag);
					nameLabel.setVisible(!flag);
					nameField.setVisible(!flag);
					destLabel.setVisible(!flag);
					destField.setVisible(!flag);
					browse.setVisible(!flag);
					ok.setVisible(!flag);
					cancel.setVisible(!flag);
					nameField.setText(Helpers.genrateName(url));
				} else {
					JOptionPane.showMessageDialog(this,
							"You Should Enter File URL", "Valid URL Missing",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				// MainClassDownload.test("Error");
				JOptionPane.showMessageDialog(this, "You Should Enter URL",
						"URL Missing", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == ok) {
			// Helpers.test("OK Button");
			String url = urlField.getText();
			String name = nameField.getText();
			String path = destField.getText();
			if (Helpers.isDirectory(path)) {
				download = new Download(path, name, url, Download.INTI);
				if (!download.hasError()) {
					downloadGui = new DownloadGUI(download);
					dispose();
				} else {
					download.cancelDownload();
					ok.setEnabled(false);
					JOptionPane.showMessageDialog(this, "Unexpected Error",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Chose Valid Path",
						"Valid Path Missing", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == cancel) {
			dispose();
		} else if (e.getSource() == browse) {
			fileChooser.setSelectedFile(new File(nameField.getText()));
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				// Helpers.test(fileChooser.getSelectedFile().getAbsolutePath());
				nameField.setText(fileChooser.getSelectedFile().getName());
				destField.setText(Helpers.getDirectory(fileChooser
						.getSelectedFile().getAbsolutePath()));
			}
		}
	}
}
