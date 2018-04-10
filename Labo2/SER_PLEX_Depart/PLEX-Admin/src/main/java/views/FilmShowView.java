package views;

import javax.swing.*;
import java.awt.*;

import models.*;
import controllers.ControleurFilms;

public class FilmShowView extends JPanel{

	private static final long serialVersionUID = -5223894065377703410L;

	private Film film;

	private JLabel espacement = new JLabel("       ");

	private JLabel labelDuree = 		new JLabel("Duree");
	private JLabel labelPhoto = 		new JLabel("Photo");
	private JLabel labelSynopsis = 		new JLabel("Synopsis");
	private JLabel labelCritiques = 	new JLabel("Critiques");

	private JLabel valeurDuree = 		new JLabel();
	private JLabel valeurPhoto = 		new JLabel();
	private JTextPane valeurSynopsis = 	new JTextPane();
	private JTextPane valeurCritiques = new JTextPane();	

	public FilmShowView(ControleurFilms ce, Film _film) {
		this.film = _film;

		valeurSynopsis.setEditable(false);
		valeurCritiques.setEditable(false);

		this.setLayout(new BorderLayout());
		String idFilm = ((Long)film.getId()).toString();
		JLabel vueTitre = new JLabel("<html><body><br/><h2 style=\"margin-left: 10px; font-style:normal; color:black\">"+"Film " + film.getTitre() +" "+ idFilm +"</h2></body></html>");
		this.add(vueTitre, BorderLayout.NORTH);

		JPanel vueContenu = new JPanel();
		this.add (vueContenu, BorderLayout.CENTER);

		GroupLayout layout = new GroupLayout(vueContenu);
		vueContenu.setLayout(layout);

		JScrollPane synopsisScrollPane = new JScrollPane(valeurSynopsis, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		synopsisScrollPane.setBounds(new Rectangle(0, 0, 300, 200));
		valeurSynopsis.setContentType("text/html");
		synopsisScrollPane.setPreferredSize(new Dimension(100, 200));

		JScrollPane critiquesScrollPane = new JScrollPane(valeurCritiques, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//synopsisScrollPane.setBounds(new Rectangle(0, 0, 300, 150));
		valeurSynopsis.setContentType("text/html");
		critiquesScrollPane.setPreferredSize(new Dimension(100, 200));

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(  // Définition des colonnes selon l'axe horizontal
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						// Colonne des labels	      		
						.addComponent(labelDuree)
						.addComponent(labelPhoto)
						.addComponent(labelSynopsis)
						.addComponent(labelCritiques))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(espacement))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										// Colonne des valeurs
										.addComponent(valeurDuree)
										.addComponent(valeurPhoto)
										.addComponent(synopsisScrollPane)
										.addComponent(critiquesScrollPane))

				);
		layout.setVerticalGroup( // Définition des lignes selon l'axe vertical
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						// Ligne Duree
						.addComponent(labelDuree)
						.addComponent(espacement)
						.addComponent(valeurDuree))	
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								// Ligne Duree
								.addComponent(labelPhoto)
								.addComponent(espacement)
								.addComponent(valeurPhoto))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										// Ligne Synopsis
										.addComponent(labelSynopsis)
										.addComponent(espacement)
										.addComponent(synopsisScrollPane))	
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												// Ligne Synopsis
												.addComponent(labelCritiques)
												.addComponent(espacement)
												.addComponent(critiquesScrollPane))	
				);

		setInfo(film);

	}

	public void setInfo(Film film){
		valeurDuree.setText(film.getDuree()+"");
		valeurPhoto.setText(film.getPhoto()+"");
		valeurSynopsis.setText("<html><body><br/><div style=\"font-style:normal;  font-size: 90%; font-family: verdana, arial,courier; color:black\">"+ film.getSynopsis() +"</div></body></html>");
		StringBuffer critiques = new StringBuffer();
		for (Critique uneCritique: film.getCritiques()){
			critiques.append(uneCritique.getNote()+" - ");
			critiques.append(uneCritique.getTexte());
			critiques.append("\r");
		}
		valeurCritiques.setText(critiques.toString());
	}

}