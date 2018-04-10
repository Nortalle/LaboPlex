package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilCalendarModel;
import javax.swing.text.DateFormatter;

import models.*;
import controllers.ControleurProjections;

public class ProjectionEditView extends JPanel{

	private static final long serialVersionUID = -3682017113857302789L;
	
	private ControleurProjections controleurProjections;
	private Projection projection;

	private JLabel espacement = new JLabel("      ");

	private JLabel labelDateHeure = new JLabel("Date");
	private JLabel labelHeure = new JLabel("Heure");
	private JLabel labelFilm = new JLabel("Film");
	private JLabel labelSalle = new JLabel("Salle");

	UtilCalendarModel dateModel = null;
	private JDatePickerImpl datePicker = null;

	JSpinner spinner = null;
	JSpinner.DateEditor editor = null;
	SpinnerDateModel timeModel = null;

	private JComboBox<String> choixFilm;
	private JComboBox<String> choixSalle;

	private JButton acceptButton = new JButton("Appliquer");
	private JButton clearButton = new JButton("Annuler");

	public ProjectionEditView(ControleurProjections ce, Projection _projection, final java.util.List<String> titresFilms, final java.util.List<String> nosSalles) {
		controleurProjections= ce;
		this.projection = _projection;

		dateModel=new UtilCalendarModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

		timeModel = new SpinnerDateModel();
		spinner = new JSpinner(timeModel);
		editor = new JSpinner.DateEditor(spinner , "HH:mm");  // HH pour compteur jusqu'à 24 (hh pour max 12)
		spinner.setEditor(editor);

		DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
		formatter.setAllowsInvalid(false); 
		formatter.setOverwriteMode(true);


		// Liste des films à choix
		String [] titres = new String[titresFilms.size()];
		titres= titresFilms.toArray(titres);
		choixFilm = new JComboBox<String>(titres);

		// Liste des salles à choix
		String [] salles = new String[nosSalles.size()];
		salles= nosSalles.toArray(salles);
		choixSalle = new JComboBox<String>(salles);

		this.setLayout(new BorderLayout());
		String idProjection = ((Long)projection.getId()).toString();
		JLabel vueTitre = new JLabel("<html><body><br/><h2 style=\"margin-left: 10px; font-style:normal; color:black\">"+"Edition projection " + idProjection +"</h2></body></html>");
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
						.addComponent(labelDateHeure)
						.addComponent(labelHeure)
						.addComponent(labelFilm)
						.addComponent(labelSalle)
						.addComponent(clearButton))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								// Groupe des saisies
								.addComponent(datePicker)
								.addComponent(spinner)
								.addComponent(choixFilm)
								.addComponent(choixSalle)
								.addComponent(acceptButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										// Un espacement
										.addComponent(espacement))

				);
		layout.setVerticalGroup( // quatre groupes sur l'axe vertical
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						// Saisie de la date de projection
						.addComponent(labelDateHeure)
						.addComponent(datePicker))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								// Saisie de l'heure de projection
								.addComponent(labelHeure)
								.addComponent(spinner))
								.addComponent(espacement)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										// Saisie du film
										.addComponent(labelFilm)
										.addComponent(choixFilm))
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												// Saisie de la salle
												.addComponent(labelSalle)
												.addComponent(choixSalle))
												.addComponent(espacement)
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(clearButton)
														.addComponent(acceptButton))			
				);

		acceptButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					Calendar dateChoisie = (Calendar)datePicker.getModel().getValue();
					Calendar heure = Calendar.getInstance();
					heure.setTime((Date)timeModel.getValue());					
					dateChoisie.set(Calendar.HOUR_OF_DAY, heure.get(Calendar.HOUR_OF_DAY));
					dateChoisie.set(Calendar.MINUTE, heure.get(Calendar.MINUTE));					
					String titreFilm = (String)choixFilm.getSelectedItem();
					String noSalle = (String)choixSalle.getSelectedItem();
					controleurProjections.updateProjection(projection, dateChoisie, titreFilm, noSalle);
				}
				catch(Exception ex) {System.out.println (ex.toString());}
			}
		});

		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controleurProjections.cancelEdition();
			}
		});

		setInfo(projection);
	}

	public void setInfo(Projection projection){
		choixFilm.setSelectedItem(projection.getFilm().getTitre());
		choixSalle.setSelectedItem(projection.getSalle().getNo());
		Calendar date = projection.getDateHeure();
		dateModel.setDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DAY_OF_MONTH));
		dateModel.setSelected(true);
		timeModel.setValue(date.getTime());
	}

}