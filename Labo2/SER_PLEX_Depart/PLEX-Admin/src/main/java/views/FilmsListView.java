package views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


import models.*;
import controllers.ControleurFilms;

public class FilmsListView extends JPanel {

	private static final long serialVersionUID = 1806867205898748570L;

	//private JLabel message;
	private ControleurFilms filmsController;

	private JTable table;
	private MyTableModel tableModel;


	class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = -7002171563246141387L;

		private String[] columnNames = {"id", "Titre", "Duree", "Photo", "Note", "Show", "Edit", "Critique"};
		private ArrayList<Film> data;


		public MyTableModel() {
			data = new ArrayList<Film>();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.size();
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			Film film = data.get(row);
			Object value="";
			switch (col) {
			case 0: value= film.getId(); break;
			case 1: value= film.getTitre(); break;
			case 2: value= film.getDuree(); break;	 
			case 3: value= film.getPhoto(); break;   
			case 4: value= film.getNoteMoyenne(); break;  	
			case 5: value= film.getId(); break;
			case 6: value= film.getId(); break;
			case 7: value= film.getId(); break;
			}
			return value;
		}

		public Class<?> getColumnClass(int colonne) {
			return "".getClass(); 
		}

		public boolean isCellEditable(int row, int column){
			// Les colonnes de boutons sont éditables
			return (column == 5) || (column == 6) || (column == 7);
		}


		public void clearAll() {
			data = new ArrayList<Film>();
		}

		public void addFilm(Film film){
			data.add(film);
			this.fireTableDataChanged();
		}

	}

	public class ButtonRenderer extends JButton implements TableCellRenderer {

		private static final long serialVersionUID = -1416630318648787897L;

		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else{
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			setIcon( (value ==null) ? new ImageIcon(FilmsGUIMainView.class.getResource("/images/Question-icon.png")) : new ImageIcon(FilmsGUIMainView.class.getResource("/images/Show-icon.png")) );
			return this;
		}
	}

	public class ButtonShowRenderer extends ButtonRenderer {

		private static final long serialVersionUID = 714400286257709740L;

		public ButtonShowRenderer() {
			super();
		}

		@Override public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setIcon( (value ==null) ? new ImageIcon(FilmsGUIMainView.class.getResource("/images/Question-icon.png")) : new ImageIcon(FilmsGUIMainView.class.getResource("/images/Show-icon.png")) );
			return this;
		}
	}

	public class ButtonEditPhotoRenderer extends ButtonRenderer {

		private static final long serialVersionUID = -1109425262510546717L;

		public ButtonEditPhotoRenderer() {
			super();
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setIcon( (value ==null) ? new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Question-icon.png")) : new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Edit-icon.png")) );
			return this;
		}
	}

	public class ButtonAddCritiqueRenderer extends ButtonRenderer {

		private static final long serialVersionUID = -1316128409362033791L;

		public ButtonAddCritiqueRenderer() {
			super();
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setIcon( (value ==null) ? new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Question-icon.png")) : new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Add 16-icon.png")) );
			return this;
		}
	}

	public class ButtonEditor extends DefaultCellEditor {

		private static final long serialVersionUID = -1206970316898928745L;

		protected JButton button;
		protected String    label;
		protected long 		idValue;	// Identifiant de l'objet
		protected boolean   isPushed;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int row, int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else{
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			label = "";
			button.setText( label );

			idValue = (value ==null) ? 0 : (Long)value;

			isPushed = true;
			return button;
		}


		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}		

	public class ButtonShowEditor extends ButtonEditor {

		private static final long serialVersionUID = -7371332606497617972L;

		public ButtonShowEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		@Override public Object getCellEditorValue() {
			if (isPushed)  {
				filmsController.showFilm(idValue);
			}
			isPushed = false;
			return new String( label ) ;
		}
	}

	public class ButtonEditPhotoEditor extends ButtonEditor {

		private static final long serialVersionUID = 8888663373249273337L;

		public ButtonEditPhotoEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		@Override public Object getCellEditorValue() {
			if (isPushed)  {
				filmsController.editPhoto(idValue);
			}
			isPushed = false;
			return new String( label ) ;
		}
	}

	public class ButtonAddCritiqueEditor extends ButtonEditor {

		private static final long serialVersionUID = -8884914350194770064L;

		public ButtonAddCritiqueEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		@Override public Object getCellEditorValue() {
			if (isPushed)  {
				filmsController.addCritique(idValue);
			}
			isPushed = false;
			return new String( label ) ;
		}
	}

	public FilmsListView(ControleurFilms ce) {
		filmsController= ce;

		setLayout (new BorderLayout());

		tableModel = new MyTableModel();
		table = new JTable(tableModel);
		//Nous ajoutons notre tableau ˆ notre contentPane dans un scroll
		//Sinon les titres des colonnes ne s'afficheront pas !
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		table.getColumn("Show").setCellRenderer(new ButtonShowRenderer());
		table.getColumn("Show").setCellEditor(new ButtonShowEditor(new JCheckBox()));
		table.getColumn("Edit").setCellRenderer(new ButtonEditPhotoRenderer());
		table.getColumn("Edit").setCellEditor(new ButtonEditPhotoEditor(new JCheckBox()));
		table.getColumn("Critique").setCellRenderer(new ButtonAddCritiqueRenderer());
		table.getColumn("Critique").setCellEditor(new ButtonAddCritiqueEditor(new JCheckBox()));
		table.setRowHeight(25);

		table.getColumnModel().getColumn(0).setMaxWidth(70);
		table.getColumn("Duree").setMaxWidth(50);
		table.getColumn("Photo").setMaxWidth(150);
		table.getColumn("Note").setMaxWidth(100);
		table.getColumn("Show").setMaxWidth(50);
		table.getColumn("Edit").setMaxWidth(50);
		table.getColumn("Critique").setMaxWidth(60);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);  // Colonne "Note" alognée au centre
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);  // Colonne "Durée" alognée au centre

	}

	public void addFilm (Film film) {
		tableModel.addFilm(film);
	}

	public void resetWith(java.util.List<Film> films){
		tableModel.clearAll();
		for (Film film: films) {
			tableModel.addFilm(film);
		}
	}

}


