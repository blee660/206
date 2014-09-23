

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

public class EWorker extends SwingWorker<String, Integer> {

	private String _fName;
	private String _time;
	private String _length;
	private String _newName;
	private String _temp = "temp";

	private Process process;
	private ProcessBuilder _builder;
	private ProcessBuilder _builder2;

	private int _complete;

	private int _exit;

	//constructor which takes in 4 parameters, all of which MUST not be null
	EWorker(String fName, String time, String length, String newName){

		_fName = fName;
		_time = time;
		_length = length;
		_newName = newName;

		//bash command for extracting files
		
		if(_fName.endsWith(".mp3")){
			_builder = new ProcessBuilder("/bin/bash", "-c", "file -b " + _fName + 
					" | grep Audio && echo \"Successful\" || echo \"Invalid\" ; avconv -i " + _fName + 
					" -ss " + _time + " -t " + _length + " -acodec copy " + _newName + 
					".mp3 && echo \"Successful\" || echo \"Error\"");
		}else if(_fName.endsWith(".mp4")){
			
				_builder = new ProcessBuilder("/bin/bash", "-c", "file -b " + _fName + 
					" | grep Media && echo \"Successful\" || echo \"Invalid\" ; avconv -i " + _fName + 
					" | grep Audio ; avconv -i " + _fName + " -acodec copy -vn -y " + _temp + 
					".aac && echo \"Successful\" || echo \"Error\" ; avconv -i " + _temp + ".aac" + 
					" -aq 2 " + _temp + ".mp3 ; rm " + _newName + ".aac ; avconv -i " + _temp + 
					".mp3 -ss " + _time + " -t " + _length + " -acodec copy " + _newName + 
					".mp3 && echo \"Successful\" || echo \"Error\"");
			if (_time != null && _length != null){
				//NEED TO DO SOMETHING HERE
			}
		}

		_builder.redirectErrorStream(true);
	}

	@Override
	protected String doInBackground() throws Exception {
		// Start extraction process
		process = _builder.start();

		// output information and progress to console
		InputStream stdout = process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

		String line2 = null;
		
	
		while ((line2 = stdoutBuffered.readLine()) != null ) {
			System.out.println(line2);
			if(line2.equals("Successful")){
				_complete = 0;
			}else if (line2.equals("Error")){
				_complete = 1;
			}else if(line2.equals("Invalid")){
				_complete = 2;
			}
		}
		_exit = process.exitValue();
		return line2;
	}

	@Override
	protected void done() {
		// TODO Auto-generated method stub
		//display message box informing completion
		if(_complete == 0){
			JOptionPane.showMessageDialog(null, "Extraction Complete");
			
			try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)))) {
				//formatting date
				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date now = new Date();
				String strDate = sdfDate.format(now);
				//read number of lines in text file
				BufferedReader reader = new BufferedReader(new FileReader("log.txt"));
				int lines = 0;
				while (reader.readLine() != null) {
					lines++;
				}
			
				reader.close();

			//print line and close
				out.println(lines + "	EXTRACT	" + strDate);

				out.close();

			}catch (IOException e) {
				e.printStackTrace();
			}
		}else if(_complete == 1){
			JOptionPane.showMessageDialog(null, "Error Encountered: \nFile does not contain audio component. \nExtraction failed");
		}else if(_complete == 2){
			JOptionPane.showMessageDialog(null, "Error Encountered: \nFile is not a valid Audio/Video file");
		}
		else if(_exit != 0){
			JOptionPane.showMessageDialog(null, "Error Encountered: Extraction failed");
		}
	}

}