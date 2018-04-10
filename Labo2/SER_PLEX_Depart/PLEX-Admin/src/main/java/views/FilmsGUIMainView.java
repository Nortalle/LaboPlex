package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import controllers.ControleurFilms;

public class FilmsGUIMainView extends JPanel {

	private static final long serialVersionUID = -6995186126588290299L;

	private ControleurFilms filmsController; 

	private FilmsListView filmsListView;

	private JPanel secondaryPanel;

	public FilmsGUIMainView(ControleurFilms controller) {
		this.filmsController=controller; 
		setLayout (new BorderLayout());


		JPanel tools = new JPanel();			// Outils à gauche
		tools.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(tools, BorderLayout.NORTH);

		JPanel gui = new JPanel();			// Panneau pour les données et les dialogues
		gui.setLayout(new GridLayout(1, 2));
		add(gui, BorderLayout.CENTER);

		// Définition de la barre d'outils
		Icon refreshIcon = new ImageIcon(FilmsGUIMainView.class.getResource("/images/Reload-icon.png"));
		JButton refreshButton = new JButton(refreshIcon);
		refreshButton.addActionListener (new ActionListener(){
			public void actionPerformed(ActionEvent e){
				filmsController.refreshFilmsList();
			}
		});
		tools.add(refreshButton);


		// Définition du panneau pour les données et les dialogues
		filmsListView = new FilmsListView(filmsController);
		gui.add(filmsListView);

		secondaryPanel = new JPanel();
		secondaryPanel.setLayout(new BorderLayout(10, 0));
		gui.add(secondaryPanel);

	} 

	public FilmsListView getListView(){
		return filmsListView;
	}

	public void setSecondaryPanel(JPanel contenu){
		secondaryPanel.removeAll();
		if (contenu != null){
			contenu.setBackground(new Color(153, 153, 255));
			secondaryPanel.add(contenu, BorderLayout.CENTER);
		}
		else{
			secondaryPanel.add(new JPanel(), BorderLayout.CENTER);
		}
		secondaryPanel.validate();
	}

}