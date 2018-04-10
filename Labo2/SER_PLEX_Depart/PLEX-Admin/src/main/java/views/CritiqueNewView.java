package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import models.*;
import controllers.ControleurFilms;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class JTextFieldLimit extends PlainDocument {

	private static final long serialVersionUID = -7298097404902803256L;
	private int limit;
	JTextFieldLimit(int limit) {
		super();
		this.limit = limit;
	}

	JTextFieldLimit(int limit, boolean upper) {
		super();
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;

		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}

public class CritiqueNewView extends JPanel {

	private static final long serialVersionUID = -8464720278861662537L;
	
	private ControleurFilms filmsController;
	private Film film;

	private JLabel espacement = new JLabel("      ");

	private JLabel labelCritique = new JLabel("Critique");
	private JTextField critique = new JTextField();

	private JPanel selectionNote = new JPanel();
	private JPanel labelNote = new JPanel();
	private ButtonGroup groupeBoutons = new ButtonGroup();


	private JButton acceptButton = new JButton("Ajouter");
	private JButton clearButton = new JButton("Annuler");

	public CritiqueNewView(ControleurFilms ce, Film _film) {
		filmsController= ce;
		this.film = _film;


		critique.setDocument(new JTextFieldLimit(100));
		selectionNote.setLayout(new FlowLayout(FlowLayout.LEFT));
		selectionNote.setMaximumSize(new Dimension(100, 40));
		String [] labelsBoutons = new String[] {"1", "2", "3", "4", "5"};
		for (String label: labelsBoutons){
			JRadioButton bouton = new JRadioButton(label);
			bouton.setActionCommand(label);
			groupeBoutons.add(bouton);
			selectionNote.add(bouton);
			if (label.equals(labelsBoutons[labelsBoutons.length-1])) bouton.setSelected(true);
		}
		labelNote.add(new JLabel("Note"));
		labelNote.setLayout(new FlowLayout(FlowLayout.LEFT));
		labelNote.setMaximumSize(new Dimension(100, 40));


		this.setLayout(new BorderLayout());
		JLabel vueTitre = new JLabel("<html><body><br/><h2 style=\"margin-left: 10px; font-style:normal; color:black\">"+"Nouvelle critique " +"</h2></body></html>");
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
						.addComponent(labelNote)
						.addComponent(labelCritique)
						.addComponent(clearButton))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								// Groupe des saisies
								.addComponent(selectionNote)
								.addComponent(critique)
								.addComponent(acceptButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										// Un espacement
										.addComponent(espacement))

				);
		layout.setVerticalGroup( // trois groupes sur l'axe vertical
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						// Saisie de la date de projection
						.addComponent(labelNote)
						.addComponent(selectionNote))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								// Saisie de la date de projection
								.addComponent(labelCritique)
								.addComponent(critique))
								.addComponent(espacement)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(clearButton)
										.addComponent(acceptButton))			
				);

		acceptButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					String choixNote = groupeBoutons.getSelection().getActionCommand();
					int note_film=Integer.parseInt(choixNote);
					filmsController.newCritique(film, note_film, critique.getText());
				}
				catch(Exception ex) {System.out.println (ex.toString());}
			}
		});

		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				filmsController.cancelCritiqueCreation();
			}
		});


		setInfo();

	}

	public void setInfo(){
		critique.setText("");
	}

}