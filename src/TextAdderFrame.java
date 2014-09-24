import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultStyledDocument;
public class TextAdderFrame extends JFrame implements ActionListener {

	private JLabel _reqTextS = new JLabel("Enter Text to add into start of Video: ");
	private JLabel _reqTimeStart = new JLabel("How long do you wish the text to appear for?");
	private JLabel _reqTextEnd = new JLabel("Enter Text to add into end of Video: ");
	private JLabel _reqTimeEnd = new JLabel("How long do you wish the text to appear for?");
	private JLabel _reqNewName = new JLabel("Save edited video as: (with .mp4 extension)");
	
	private JPanel _tS = new JPanel();
	private JPanel _tF = new JPanel();
	
	private JTextArea _enterTextS = new JTextArea(10, 40);
	private JTextField _enterTimeStart = new JTextField("in seconds:", 10);
	private JTextArea _enterTextEnd = new JTextArea(10,40);
	private JTextField _enterTimeEnd = new JTextField("in seconds:", 10);
	private JTextField _enterNewName = new JTextField(20);
	
	private JButton _cancel = new JButton("Cancel");
	private JButton _add = new JButton("Add Text");
	private FlowLayout _layout = new FlowLayout();
	
	private String _textS;
	private String _timeS;
	private String _textE;
	private String _timeE;
	private String _newName;
	private String _vidName;
	private long _time;
	private String _font;
	private String _fontColor;
	private String _fontSize;
	
	private DefaultStyledDocument doc;
	
	TextAdderFrame(String vidName, long time){
		_time = time;
		_vidName = vidName;
		
		_enterTextS.setLineWrap(true);
		_enterTextS.setWrapStyleWord(true);
		_enterTextEnd.setLineWrap(true);
		_enterTextEnd.setWrapStyleWord(true);
		
		String[] fontsString = new String[] {"Normal", "Italics", "Bold", "Bold + Italics"};
		JComboBox<String> fonts = new JComboBox<>(fontsString);
		String[] fontSize = new String[] {"14", "16", "18", "20", "22", "24", "26", "28", "30"};
		JComboBox<String> sizes = new JComboBox<>(fontSize);
		String[] fontColor = new String[] {"Black", "White", "Yello", "Blue", "Red"};
		JComboBox<String> fontColors = new JComboBox<>(fontColor);
		
		JScrollPane spS = new JScrollPane(_enterTextS);
		JScrollPane spE = new JScrollPane(_enterTextEnd);
		
		setTitle("Add Text to Video");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(550,600);
		setLayout(_layout);
		
		add(_reqTextS);
		add(spS);
		
		_tS.add(_reqTimeStart);
		_tS.add(_enterTimeStart);
		
		add(_tS);
		
		add(_reqTextEnd);
		add(spE);
		
		_tF.add(_reqTimeEnd);
		_tF.add(_enterTimeEnd);
		
		add(_tF);
		
		add(_reqNewName);
		add(_enterNewName);
		
		add(new JLabel("font style:"));
		add(fonts);
		add(new JLabel("font size:"));
		add(sizes);
		add(new JLabel("font Color:"));
		add(fontColors);
		
		add(_add);
		add(_cancel);
		
//		 doc = new DefaultStyledDocument();
//         doc.setDocumentFilter(new DocumentSizeFilter(500));
//         doc.addDocumentListener(new DocumentListener(){
//             @Override
//             public void changedUpdate(DocumentEvent e) { updateCount();}
//             @Override
//             public void insertUpdate(DocumentEvent e) { updateCount();}
//             @Override
//             public void removeUpdate(DocumentEvent e) { updateCount();}
//         });
//         textField.setDocument(doc);
//
//         updateCount();
		
		_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});
		
		fonts.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
			        JComboBox<String> cb = (JComboBox<String>)e.getSource();
			        String fontName = (String)cb.getSelectedItem();
			        _font = fontName;
			    }
		});
		
		sizes.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
			        JComboBox<String> cb = (JComboBox<String>)e.getSource();
			        String fontSize = (String)cb.getSelectedItem();
			        _fontSize = fontSize;
			    }
		});
		
		fontColors.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
			        JComboBox<String> cb = (JComboBox<String>)e.getSource();
			        String color = (String)cb.getSelectedItem();
			        _fontColor = color;
			    }
		});
		
		_add.addActionListener(this);

		
		_enterTimeStart.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
					_enterTimeStart.setText("");
				}
		});
	
		
		_enterTimeEnd.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
					_enterTimeEnd.setText("");
				}
		});
	
	}
	
	
	
		@Override
	public void actionPerformed(ActionEvent e) {
		
			
			
		_textS = _enterTextS.getText();
		_timeS = _enterTimeStart.getText();
		_textE = _enterTextEnd.getText();
		_timeE = _enterTimeEnd.getText();
		_newName = _enterNewName.getText();
	
		int time = (int) (_time - Integer.parseInt(_timeE));
		System.out.println(_timeE); 
		System.out.println(time);
		_timeE = Integer.toString(time);
		TextWorker textWorker = new TextWorker(_vidName, _newName, _timeS, _timeE, _textS, _textE, _font, _fontSize, _fontColor);
		
		System.out.println(_timeE);
		textWorker.execute();
	
		
	}
	
}
