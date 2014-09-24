import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultStyledDocument;



public class TextWorker extends SwingWorker<String, Integer>{

	

	private String _vidName;
	private String _newName;
	private String _timeS;
	private String _timeF;
	private String _textS;
	private String _textE;
	private String _font;
	private String _fontSize;
	private String _fontColor;

	private int _complete;
	private int _exit;

	private ProcessBuilder _builder;
	private Process _process;

	TextWorker(String name, String newName, String timeS, String timeF, String textS, String textE, String font, String fontSize, String fontColor){
		_vidName = name;
		_newName = newName;
		_timeS = timeS;
		_timeF = timeF;
		_textS = textS;
		_textE = textE;
		_fontSize = fontSize;
		_fontColor = fontColor;

		if(font.equals("Normal")){
			_font = "Ubuntu-L";
		}else if(font.equals("Italics")){
			_font = "Ubuntu_LI";
		}else if(font.equals("Bold")){
			_font = "Ubuntu-B";
		}else if(font.equals("Bold + Italics")){
			_font = "Ubuntu-BI";
		}
		
		
		_builder = new ProcessBuilder("/bin/bash", "-c", "avconv -i "+ _vidName +" -vf \"drawtext="
				+ "fontfile='/usr/share/fonts/truetype/ubuntu-font-family/"+ _font +".ttf': text='"+ _textS +"':x=(main_w-text_w)/2: "
				+ "fontsize="+ _fontSize +":fontcolor="+ _fontColor +":draw='lt(t,"+ _timeS +")':,drawtext=fontfile='/usr/share/fonts/truetype/ubuntu-font-family/"+ _font +".ttf': "
				+ "text='" + _textE + "':x=(main_w-text_w)/2: fontsize="+ _fontSize +":fontcolor="+ _fontColor +":draw='gt(t," + _timeF + ")':\" -c:a copy " + _newName + " && echo \"Successful\" || echo \"Error\"");

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
			JOptionPane.showMessageDialog(null, "Add Text Complete");
		}else if(_complete == 1 || _exit != 0){
			JOptionPane.showMessageDialog(null, "Error Encountered: Error adding text to video");

		}
	}

}
