import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton; 
import javax.swing.JFrame; 
import javax.swing.JOptionPane;
import javax.swing.JPanel;	
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaPlayer; 
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class PlayFrame extends JFrame{

	private JFrame _frame = new JFrame("Vamix Player");
	
	private String _newName;
	
    private JButton _ff = new JButton("▶▶"); 
    private JButton _skipF = new JButton("skip ▶▶");
    private JButton _rewind = new JButton("◀◀"); 
    private JButton _skipB = new JButton("◀◀ skip");
   	private JButton _play = new JButton("▷"); 
   	private JButton _pause = new JButton("||"); 
   	private JButton _extract = new JButton("Extract audio from video");
   	private JButton _close = new JButton("close");
   	private JButton _mute = new JButton("mute");
   	
   	private JPanel _panel = new JPanel();
   	private JPanel _panel2 = new JPanel();
   	private static String _file;
   	private final EmbeddedMediaPlayerComponent _mediaPlayer; 
   	private final AudioMediaPlayerComponent _audioPlayer; 
   	private FlowLayout _layout = new FlowLayout();
   	
   	private long _time = 4000;
   	private float _speed = 1.5f;
    
    public static void main (final String args[]) { 
    	SwingUtilities.invokeLater(new Runnable() {
    		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new PlayFrame(_file); 
			} 
		}); 
    } 
    
    PlayFrame (String file) { 
    	_file = file;
    	
    	_frame.setLayout(_layout);
    	_panel.add(_skipB);
    	_panel.add(_rewind);
    	_panel.add(_pause);
    	_panel.add(_ff);
    	_panel.add(_skipF);
    	_panel.add(_mute);
    	_panel.add(_extract);
    	_panel.add(_close);
    	_frame.setVisible(true);
    	_mediaPlayer = new EmbeddedMediaPlayerComponent(); 
    	_audioPlayer = new AudioMediaPlayerComponent();
    	
    	_frame.setLocation(100, 100);
    	_frame.setSize(1050, 600); 
    	_frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	
    	String newName = null;
    	if(_file.endsWith(".mp3")){
    		PWorker pworker = new PWorker(_file, newName);
    		pworker.execute();
    		_audioPlayer.getMediaPlayer().playMedia(_file, newName);
    	}else if(_file.endsWith(".mp4")){
    		PWorker pworker = new PWorker(_file, newName);
    		pworker.execute();
    		_frame.setContentPane(_mediaPlayer); 
    		_frame.getContentPane().add(_panel, BorderLayout.SOUTH);
    		_mediaPlayer.getMediaPlayer().playMedia(_file);
    	}
    	    	
    	_close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//_mediaPlayer.getMediaPlayer().stop();
				_frame.setVisible(false);
				_frame.dispose();
			}
		});
    	
    	_pause.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//_mediaPlayer.getMediaPlayer().pause();
				if(_pause.getText().equals("||")){
					_mediaPlayer.getMediaPlayer().pause();
					_pause.setText("▷");
				}else{
					
					if(_mediaPlayer.getMediaPlayer().getRate() != 1){
						_mediaPlayer.getMediaPlayer().setRate(1);
					}else{
						_mediaPlayer.getMediaPlayer().play();
					}
					_pause.setText("||");
				}
			}
    		
    	});
    	
    	_skipF.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				_mediaPlayer.getMediaPlayer().skip(_time);
			}
    		
    	});
    	
    	_skipB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				_mediaPlayer.getMediaPlayer().skip(-_time);
			}
    		
    	});
    	
    	_ff.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				_pause.setText("▷");
				
				if(_mediaPlayer.getMediaPlayer().getRate() == 1){
					_mediaPlayer.getMediaPlayer().setRate(_speed);
				}else if(_mediaPlayer.getMediaPlayer().getRate() == _speed){
					_mediaPlayer.getMediaPlayer().setRate(_speed + 1.5f);
				}else{
					_mediaPlayer.getMediaPlayer().setRate(_speed + 1.5f);
				}
				
			}
    		
    	});

    	_rewind.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				_pause.setText("▷");
				
				if(_mediaPlayer.getMediaPlayer().getRate() == 1){
					_mediaPlayer.getMediaPlayer().setRate(-2f);
				}else if(_mediaPlayer.getMediaPlayer().getRate() == -(_speed)){
					_mediaPlayer.getMediaPlayer().setRate(-(_speed) - 1.5f);
				}else {
					_mediaPlayer.getMediaPlayer().setRate(-(_speed) - 1.5f);
				}
			}
    		
    	});
    	
    	_extract.addActionListener(new ActionListener(){

    		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				_newName = JOptionPane.showInputDialog("Enter new name for audio file (without .mp3 extension): ");
				EWorker eworker = new EWorker(_file, null, null, _newName);
				eworker.execute();
			}
    		
    	});
    	
    	_mute.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				_mediaPlayer.getMediaPlayer().mute();
			}
			
    	});
    }
}

