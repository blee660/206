import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class VamixWindow extends JFrame {
	
	private JButton _download = new JButton("Download");
	private JButton _play = new JButton("Play");
	private JButton _quit = new JButton("Quit");
	
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
				
				if(playFrame == null){
					playFrame = new PlayFrame();
				}
				
				
			}
			
		});
	}
	
	
}