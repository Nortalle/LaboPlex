package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import controllers.ControleurProjections;

public class ProjectionsGUIMainView extends JPanel {

	private static final long serialVersionUID = -7811287718621647592L;

	private ControleurProjections controleurProjections; 

	private ProjectionsListView projectionsListView;

	private JPanel secondaryPanel;

	public ProjectionsGUIMainView(ControleurProjections controller) {
		this.controleurProjections=controller; 
		setLayout (new BorderLayout());

		JPanel tools = new JPanel();			// Outils à gauche
		tools.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(tools, BorderLayout.NORTH);

		JPanel gui = new JPanel();				// Panneau pour les données et les dialogues
		gui.setLayout(new GridLayout(1, 2));
		add(gui, BorderLayout.CENTER);

		// Définition de la barre d'outils
		Icon refreshIcon = new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Reload-icon.png"));
		JButton refreshButton = new JButton(refreshIcon);
		refreshButton.addActionListener (new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controleurProjections.refreshProjectionsList();
			}
		});
		tools.add(refreshButton);

		Icon addIcon = new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Add-icon.png"));
		JButton addProjectionButton = new JButton(addIcon);
		addProjectionButton.addActionListener (new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controleurProjections.addProjection();
			}
		});
		tools.add(addProjectionButton);


		// Définition du panneau pour les données et les dialogues
		projectionsListView = new ProjectionsListView(controleurProjections);
		gui.add(projectionsListView);

		secondaryPanel = new JPanel();
		secondaryPanel.setLayout(new BorderLayout(10, 0));
		gui.add(secondaryPanel);

	}



	public ProjectionsListView getListView(){
		return projectionsListView;
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