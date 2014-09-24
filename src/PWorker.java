import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;


public class PWorker extends SwingWorker<String, Integer> {

	private String _fName;
	
	private Process _process;
	private ProcessBuilder _builder;
	
	private int _complete;
	private int _exit;
	
	PWorker(String fName, String newName) {
		_fName = fName;
		
		if (_fName.contains(".mp3")) {
			_builder = new ProcessBuilder("/bin/bash", "-c", "file -b " + _fName + 
					" | grep Audio && echo \"Successful\" || echo \"Invalid\"");
			
		} else if (_fName.contains(".mp4")) {
			_builder = new ProcessBuilder("/bin/bash", "-c", "file -b " + _fName + 
					" | grep Media && echo \"Successful\" || echo \"Invalid\"");
			//fontfile=/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-L.ttf:
		}
		
		_builder.redirectErrorStream(true);
	}
	
	@Override
	protected String doInBackground() throws Exception {
		_process = _builder.start();

		// output information and progress to console
		InputStream stdout = _process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

		String line2 = null;
		
	
		while ((line2 = stdoutBuffered.readLine()) != null ) {
			System.out.println(line2);
			if(line2.equals("Successful")){
				_complete = 0;
			}else if (line2.equals("Error")){
				_complete = 1;
			}
		}
		_exit = _process.exitValue();
		return line2;
	}
	
	@Override
	protected void done() {
		if(_complete == 0){
			System.out.println("OKAY");
		}else if(_complete == 1){
			JOptionPane.showMessageDialog(null, "Error Encountered: Cannot Play the file");
		}else if(_exit != 0){
			JOptionPane.showMessageDialog(null, "Error Encountered: Play Failed");
		}
	}
	

}
