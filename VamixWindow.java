import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class VamixWindow extends JFrame {
	
	private JButton _download = new JButton("Download");
	private JButton _play = new JButton("Play");
	private JButton _extract = new JButton("Extract");
	private JButton _quit = new JButton("Quit");
	private JPanel _listPane = new JPanel();
	private JTextField _Fname = new JTextField(40);
	private String _dirPath;
	private FlowLayout _fLayout = new FlowLayout();
	//private JFrame playFrame = null;
	
	JLabel select = new JLabel("<html><br>Welcome to VAMIX! <br><br> Please select from one of the following: <br><html>");
	
	private VamixWindow(){
		_download.addActionListener(new ActionListener() {
			
			JFrame downloadFrame = null;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(downloadFrame == null){
					downloadFrame = new DownloadFrame();
				}

				downloadFrame.setVisible(!downloadFrame.isVisible());

			}
		});
		
		_play.addActionListener(new ActionListener(){
			
			JFrame playFrame = null;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(playFrame == null || playFrame.isVisible() == false){
					JFileChooser chooseFile = new JFileChooser();
					//set directory to be current directory
					File dir = new File(System.getProperty("user.dir"));
					chooseFile.setCurrentDirectory(dir);
					int result = chooseFile.showDialog(null, null);

					if (chooseFile.getSelectedFile() == null) {
						try {
							_dirPath = null;
							playFrame.dispose();
						} catch (NullPointerException e) {
							// do nothing
						}
					} else if(result == JFileChooser.APPROVE_OPTION) {
						if(chooseFile.getSelectedFile() != null){

							_dirPath = chooseFile.getSelectedFile().getName();
							
						}
					}
					if (_dirPath != null) {
						playFrame = new PlayFrame(_dirPath);
					}
				}
				
			
			
			}
			
		});
		
		_extract.addActionListener(new ActionListener(){

			JFrame extractFrame = null;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(extractFrame == null){
					extractFrame = new ExtractFrame();
				}
				extractFrame.setVisible(!extractFrame.isVisible());
			}
			
		});
		
		_quit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
			
		});
	}
	
	public static void main(String[] agrs){

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				VamixWindow vamix = new VamixWindow();
				vamix.createAndShowGUI();
			}
		});
	}

	protected void createAndShowGUI() {

		VamixWindow mainframe = new VamixWindow();

		mainframe.setTitle("Audio Mixer");
		mainframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		mainframe.setLayout(_fLayout);
		mainframe.setSize(600, 300);
		_listPane.add(_download);
		_listPane.add(_play);
		_listPane.add(_extract);
		_listPane.add(_quit);

//		ImageIcon vamix = new ImageIcon("src/vamix.png");
//		JLabel vam = new JLabel(vamix);

//		mainframe.add(vam);
		mainframe.add(select);
		mainframe.add(_listPane);

		mainframe.setVisible(true);

	}
	
	
}
