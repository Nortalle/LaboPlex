package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import models.*;
import controllers.ControleurFilms;

public class FilmEditView extends JPanel{

	private static final long serialVersionUID = 5049742941478313284L;
	
	private ControleurFilms filmsController;
	private Film film;

	private JLabel espacement = new JLabel("      ");
	
	private JLabel labelPhoto = new JLabel("Photo");
	private JTextField pathPhoto = new JTextField();

	
	private JButton acceptButton = new JButton("Appliquer");
	private JButton clearButton = new JButton("Annuler");


	public FilmEditView(ControleurFilms _ce, final Film _film) {
		filmsController =_ce;
		this.film = _film;

		this.setLayout(new BorderLayout());
		String idFilm = ((Long)film.getId()).toString();
		JLabel vueTitre = new JLabel("<html><body><br/><h2 style=\"margin-left: 10px; font-style:normal; color:black\">"+"Edition film " + film.getTitre() + " " + idFilm +"</h2></body></html>");
		this.add(vueTitre, BorderLayout.NORTH);

		JPanel vueContenu = new JPanel();
		this.add(vueContenu, BorderLayout.CENTER);

		GroupLayout layout = new GroupLayout(vueContenu);
		vueContenu.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(  // trois groupes sur l'axe horizontal
		   layout.createSequentialGroup()
		      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		      		// Groupe des labels
		           .addComponent(labelPhoto)
				   .addComponent(clearButton))
			  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			  		// Groupe des saisies
			       .addComponent(pathPhoto)
			       .addComponent(acceptButton))
			  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
					// Un espacement
			       .addComponent(espacement))
				
		);
		layout.setVerticalGroup( // quatre groupes sur l'axe vertical
		   layout.createSequentialGroup()
		   	  	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		   	  		// Saisie de la date de film
	               .addComponent(labelPhoto)
	               .addComponent(pathPhoto))
		      	.addComponent(espacement)
			  	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			       .addComponent(clearButton)
			       .addComponent(acceptButton))			
		);

		acceptButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					filmsController.updateFilm(film, pathPhoto.getText());
				}
				catch(Exception ex) {System.out.println (ex.toString());}
			}
		});

		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				filmsController.cancelPhotoEdition();
			}
		});

		setInfo(film);
	}

	public void setInfo(Film film){
		pathPhoto.setText(film.getPhoto());
	}

}