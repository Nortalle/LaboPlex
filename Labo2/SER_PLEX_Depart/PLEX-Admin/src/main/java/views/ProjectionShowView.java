package views;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

import models.*;
import controllers.ControleurProjections;

public class ProjectionShowView extends JPanel{

	private static final long serialVersionUID = 7203510123871801970L;

	private JLabel espacement = new JLabel("       ");

	private JLabel labelDateHeure = 	new JLabel("DateHeure");
	private JLabel labelFilm = 			new JLabel("Film");
	private JLabel labelSalle = 		new JLabel("Salle");
	private JLabel labelDuree = 		new JLabel("Duree");
	private JLabel labelSynopsis = 		new JLabel("Synopsis");

	private JLabel valeurDateHeure = 	new JLabel();
	private JLabel valeurFilm = 		new JLabel();
	private JLabel valeurSalle = 		new JLabel();
	private JLabel valeurDuree = 		new JLabel();
	private JTextPane valeurSynopsis = 	new JTextPane();

	public ProjectionShowView(ControleurProjections _ce, Projection _projection) {

		valeurDateHeure.setForeground(new Color(0x00008B));
		valeurFilm.setForeground(new Color(0x00008B));
		valeurSalle.setForeground(new Color(0x00008B));

		this.setLayout(new BorderLayout());
		String idProjection = ((Long)_projection.getId()).toString();
		JLabel vueTitre = new JLabel("<html><body><br/><h2 style=\"margin-left: 10px; font-style:normal; color:black\">"+"Valeur projection " + idProjection +"</h2></body></html>");
		this.add(vueTitre, BorderLayout.NORTH);

		JPanel vueContenu = new JPanel();
		this.add (vueContenu, BorderLayout.CENTER);

		GroupLayout layout = new GroupLayout(vueContenu);
		vueContenu.setLayout(layout);

		JScrollPane synopsisScrollPane = new JScrollPane(valeurSynopsis, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		synopsisScrollPane.setBounds(new Rectangle(0, 0, 300, 150));
		valeurSynopsis.setContentType("text/html");

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(  // Définition des colonnes selon l'axe horizontal
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						// Colonne des labels	      		
						.addComponent(labelDateHeure)
						.addComponent(labelFilm)
						.addComponent(labelSalle)
						.addComponent(labelDuree)
						.addComponent(labelSynopsis))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(espacement))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										// Colonne des valeurs
										.addComponent(valeurDateHeure)
										.addComponent(valeurFilm)
										.addComponent(valeurSalle)
										.addComponent(valeurDuree)
										.addComponent(synopsisScrollPane))

				);
		layout.setVerticalGroup( // Définition des lignes selon l'axe vertical
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						// Ligne DateHeure
						.addComponent(labelDateHeure)
						.addComponent(espacement)
						.addComponent(valeurDateHeure))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								// Ligne Film
								.addComponent(labelFilm)
								.addComponent(espacement)
								.addComponent(valeurFilm))	
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										// Ligne Salle
										.addComponent(labelSalle)
										.addComponent(espacement)
										.addComponent(valeurSalle))	
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												// Ligne Duree
												.addComponent(labelDuree)
												.addComponent(espacement)
												.addComponent(valeurDuree))	
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														// Ligne Synopsis
														.addComponent(labelSynopsis)
														.addComponent(espacement)
														.addComponent(synopsisScrollPane))		
				);

		setInfo(_projection);

	}

	public void setInfo(Projection projection){
		valeurFilm.setText(projection.getFilm().getTitre());
		valeurSalle.setText(projection.getSalle().getNo());
		valeurDateHeure.setText((new SimpleDateFormat("dd-MM-yyyy ' ' HH:mm")).format(projection.getDateHeure().getTime()));
		valeurDuree.setText(projection.getFilm().getDuree()+"");
		valeurSynopsis.setText("<html><body><br/><div style=\"font-style:normal;  font-size: 90%; font-family: verdana, arial,courier; color:black\">"+ projection.getFilm().getSynopsis() +"</div></body></html>");
	}

}