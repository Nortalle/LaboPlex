package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controllers.*;


public class MainGUI {  
	private static JPanel mainPanel;
	private static JTabbedPane tabbedPane;
	private static ControleurGeneral controleurGeneral;

	private static JLabel warningLabel	= new JLabel();
	private static JLabel errorMessage	= new JLabel();

	private JButton initBD 				= null;
	private JButton createXStreamXML	= null;
	private JButton createXML 			= null;
	private JButton sendJSON 			= null;

	public MainGUI(ControleurGeneral _controleurGeneral) {
		controleurGeneral = _controleurGeneral;

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		// Barre d'outils & Avertissements
		JPanel toolsEtWarnings = new JPanel();            
		toolsEtWarnings.setLayout(new GridLayout(1, 2)); 
		mainPanel.add(toolsEtWarnings, BorderLayout.NORTH);
		toolsEtWarnings.setPreferredSize(new Dimension(100, 50));

		// Outils à gauche
		JPanel tools = new JPanel();                   
		tools.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolsEtWarnings.add(tools);

		// Avertissements à droite
		JPanel warnings = new JPanel();                 
		warnings.setLayout(new FlowLayout(FlowLayout.RIGHT));
		warnings.add(warningLabel);
		toolsEtWarnings.add(warnings);

		// Messages d'erreur (textes d'exception)
		JPanel error_messages = new JPanel();           
		error_messages.setLayout(new FlowLayout(FlowLayout.LEFT));
		error_messages.add(errorMessage);
		mainPanel.add(error_messages, BorderLayout.SOUTH);

		// Onglets
		tabbedPane = new JTabbedPane();                 
		tabbedPane.setPreferredSize(new Dimension(1300, 400));
		//--> Autoriser des onglets en mode scrolling
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		mainPanel.add(tabbedPane, BorderLayout.CENTER);

		// Définition de la barre d'outils
		initBD = new JButton("Init BD");
		initBD.addActionListener (new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controleurGeneral.initBaseDeDonnees();
			}
		});
		tools.add(initBD);


		createXStreamXML = new JButton("XStream XML");
		createXStreamXML.addActionListener (new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controleurGeneral.createXStreamXML();
			}
		});
		tools.add(createXStreamXML);

		createXML = new JButton("XML");
		createXML.addActionListener (new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controleurGeneral.createXML();
			}
		});
		tools.add(createXML);

		sendJSON = new JButton("JSON > Media");
		sendJSON.addActionListener (new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controleurGeneral.sendJSONToMedia();
			}
		});
		tools.add(sendJSON);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}

	public void addOnglet(String title, JPanel onglet){
		tabbedPane.addTab(title, onglet);
	}

	private void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Projections");
		MainMenuBar menuBar = new MainMenuBar(controleurGeneral);


		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);

		//Add content to the window.
		frame.add(mainPanel, BorderLayout.CENTER);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public void disableButtons() {
		initBD.setEnabled(false);
		createXStreamXML.setEnabled(false);
		createXML.setEnabled(false);
		sendJSON.setEnabled(false);  
	}

	public void enableButtons() {
		initBD.setEnabled(true);
		createXStreamXML.setEnabled(true);
		createXML.setEnabled(true);
		sendJSON.setEnabled(true);  
	}

	public void setErrorMessage(String text, String exception_text) {
		Icon warningIcon = new ImageIcon(FilmsGUIMainView.class.getResource("/images/Error-icon.png"));
		warningLabel.setIcon (warningIcon);
		warningLabel.setText ("<html><body><br/><h3 style=\"font-style:normal; color:#DC143C\">"+text+"</h3></body></html>");
		errorMessage.setText ("<html><body><br/><h4 style=\"font-style:normal; color:#DC143C\">"+exception_text+"</h4></body></html>");
	}

	public void setWarningMessage(String text) {
		Icon warningIcon = new ImageIcon(FilmsGUIMainView.class.getResource("/images/Warning-icon.png"));
		warningLabel.setIcon (warningIcon);
		warningLabel.setText ("<html><body><br/><h3 style=\"font-style:normal; color:#FF8C00\">"+text+"</h3></body></html>");
	}

	public void setAcknoledgeMessage(String text) {
		Icon okIcon = new ImageIcon(FilmsGUIMainView.class.getResource("/images/Success-icon.png"));
		warningLabel.setText ("<html><body><br/><h3 style=\"font-style:normal; color:green\">"+text+"</h3></body></html>");
		warningLabel.setIcon (okIcon);
	}

	public void resetMessage() {
		warningLabel.setText ("<html><body><br/><h3 style=\"font-style:normal; color:green\">"+""+"</h3></body></html>");
		warningLabel.setIcon (null);
		errorMessage.setText ("<html><body><br/><h4 style=\"font-style:normal; color:#DC143C\">"+""+"</h4></body></html>");
	}

}