import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class DWorker extends SwingWorker<String,Integer> implements ActionListener{

	//DWorker carries out tasks on appropriate threads
	private boolean _success;
	private String _URL;
	private int _override;
	private Process process;
	private ProcessBuilder _builder;
	private int _complete;
	private JProgressBar _progressBar;

	//constructor	
	DWorker(String url, int override, JProgressBar prog){

		_URL = url;
		_override = override;
		_progressBar = prog;

		//standard wget command
		if (override == 0){
			_builder = new ProcessBuilder("/bin/bash", "-c", "wget " + _URL + " && echo \"Complete\" || echo \"Error Encountered\"");
		}

		//command for wget which will resume cancelled download
		if (override == 1){
			_builder = new ProcessBuilder("/bin/bash", "-c", "wget -c " + _URL + " && echo \"Complete\" || echo \"Error Encountered\"");
		}
		_builder.redirectErrorStream(true);
	}



	@Override
	protected String doInBackground() throws Exception {
		//start the download process
		process = _builder.start();

		//print messages to console

		InputStream stdout = process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

		String line2 = null;

		while ((line2 = stdoutBuffered.readLine()) != null ) {
			System.out.println(line2);
			if(line2.equals("Complete")){
				_success = true;
			}else{
				_success = false;
			}

			//find relevant output on console which shows progress of download
			if (line2.contains("..........")){
				int num = Integer.parseInt(line2.substring(line2.indexOf(".........."), line2.indexOf('%')).replaceAll("[^0-9]", ""));
				//publish values
				publish(num);
			}
		}
		//determine whether download process was successful or not
		_complete = process.exitValue();
		return line2;
	}

	@Override
	//update progress bar in 'chunks' according to status
	public void process(List<Integer> chunks){
		for (int num: chunks){
			_progressBar.setValue(num);
		}
	}

	//method which gets called when the pause button is pressed while downloading
	@Override
	public void actionPerformed(ActionEvent arg0) {
		process.destroy();
	}

	@Override
	protected void done(){
		if(_complete == 0){
			if(_success == true){
				//if exit status was 0, download was successful. 
				JOptionPane.showMessageDialog(null, "Download Completed");
			}
			//log details into log text file
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)))) {

				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date now = new Date();
				String strDate = sdfDate.format(now);

				BufferedReader reader = new BufferedReader(new FileReader("log.txt"));
				int lines = 0;
				while (reader.readLine() != null) lines++;
				reader.close();

				out.println(lines + "	DOWNLOAD	" + strDate);

				out.close();

			}catch (IOException e) {
				e.printStackTrace();
			}
			//if exit status is bigger than 0, Download was unsuccessful
		}else if(_complete > 0){
			JOptionPane.showMessageDialog(null, "Error Encountered");
		}
	}
}
