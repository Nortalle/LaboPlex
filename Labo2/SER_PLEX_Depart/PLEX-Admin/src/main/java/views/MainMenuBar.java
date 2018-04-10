package views;

import javax.swing.*;
import java.awt.event.*;

import controllers.*;

public class MainMenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;
	private ControleurGeneral controleurGeneral;
	
	public MainMenuBar(ControleurGeneral _controleurGeneral) {
		this.controleurGeneral = _controleurGeneral;
 
		JMenu fileMenu = new JMenu ("File");
		JMenu helpMenu = new JMenu ("Help");
		JMenuItem quitOption = new JMenuItem ("Quit");
		JMenuItem createXMLOption = new JMenuItem ("Create XML");
		
		// Construction du menu "File"
		add (fileMenu);
		fileMenu.add (quitOption);
		fileMenu.add (createXMLOption);
		quitOption.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		createXMLOption.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controleurGeneral.createXML();
			}
		});
		
		// Construction du menu "Help"
		add (helpMenu);
	}
}
