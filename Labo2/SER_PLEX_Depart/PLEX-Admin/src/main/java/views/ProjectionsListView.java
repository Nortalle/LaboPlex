package views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;

import models.*;
import controllers.ControleurProjections;

public class ProjectionsListView extends JPanel {

	private static final long serialVersionUID = 1565921629355468211L;

	private ControleurProjections controleurProjections;

	private JTable table;
	private MyTableModel tableModel;


	class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 8464754473313347968L;

		private String[] columnNames = {"id", "DateHeure", "Film", "Salle", "Duree", "Show", "Edit", "Delete"};
		private ArrayList<Projection> data;

		public MyTableModel() {
			data = new ArrayList<Projection>();
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
			Projection projection = data.get(row);
			Object value="";
			switch (col) {
			case 0: value= projection.getId(); break;
			case 1: value= (new SimpleDateFormat("dd-MM-yyyy ' ' HH:mm")).format(projection.getDateHeure().getTime());
			break;
			case 2: value= projection.getFilm().getTitre(); break;
			case 3: value= projection.getSalle().getNo(); break;
			case 4: value= projection.getFilm().getDuree(); break;	    	
			case 5: value= projection.getId(); break;
			case 6: value= projection.getId(); break;
			case 7: value= projection.getId(); break;
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
			data = new ArrayList<Projection>();
		}

		public void addProjection(Projection projection){
			data.add(projection);
			this.fireTableDataChanged();
		}

	}

	public class ButtonRenderer extends JButton implements TableCellRenderer {

		private static final long serialVersionUID = -3274513548346839201L;

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
			setIcon( (value ==null) ? new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Question-icon.png")) : new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Show-icon.png")) );
			return this;
		}
	}

	public class ButtonShowRenderer extends ButtonRenderer {

		private static final long serialVersionUID = 255409035087345099L;

		public ButtonShowRenderer() {
			super();
		}

		@Override public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setIcon( (value ==null) ? new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Question-icon.png")) : new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Show-icon.png")) );
			return this;
		}
	}

	public class ButtonEditRenderer extends ButtonRenderer {

		private static final long serialVersionUID = -8971088829722332535L;

		public ButtonEditRenderer() {
			super();
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setIcon( (value ==null) ? new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Question-icon.png")) : new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Edit-icon.png")) );
			return this;
		}
	}

	public class ButtonDeleteRenderer extends ButtonRenderer {

		private static final long serialVersionUID = -8224942801169469132L;

		public ButtonDeleteRenderer() {
			super();
		}

		@Override public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setIcon( (value ==null) ? new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Question-icon.png")) : new ImageIcon(ProjectionsGUIMainView.class.getResource("/images/Delete-icon.png")) );
			return this;
		}
	}


	public class ButtonEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 2989704025033130384L;
		
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

		private static final long serialVersionUID = -1633670546352458008L;

		public ButtonShowEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		@Override public Object getCellEditorValue() {
			if (isPushed)  {
				//JOptionPane.showMessageDialog(button, "Afficher " + idValue);
				controleurProjections.showProjection(idValue);
			}
			isPushed = false;
			return new String( label ) ;
		}
	}

	public class ButtonEditEditor extends ButtonEditor {
		
		private static final long serialVersionUID = -3898553216912264831L;

		public ButtonEditEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		@Override public Object getCellEditorValue() {
			if (isPushed)  {
				//JOptionPane.showMessageDialog(button, "Editer " + idValue);
				controleurProjections.editProjection(idValue);
			}
			isPushed = false;
			return new String( label ) ;
		}
	}

	public class ButtonDeleteEditor extends ButtonEditor {

		private static final long serialVersionUID = 3369247668343095552L;

		public ButtonDeleteEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		@Override public Object getCellEditorValue() {
			if (isPushed)  {
				//JOptionPane.showMessageDialog(button, "Afficher " + idValue);
				controleurProjections.deleteProjection(idValue);
			}
			isPushed = false;
			return new String( label ) ;
		}
	}


	public ProjectionsListView(ControleurProjections ce) {
		controleurProjections= ce;

		setLayout (new BorderLayout());

		tableModel = new MyTableModel();
		table = new JTable(tableModel);
		//Nous ajoutons notre tableau ˆ notre contentPane dans un scroll
		//Sinon les titres des colonnes ne s'afficheront pas !
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		table.getColumn("Show").setCellRenderer(new ButtonShowRenderer());
		table.getColumn("Show").setCellEditor(new ButtonShowEditor(new JCheckBox()));
		table.getColumn("Edit").setCellRenderer(new ButtonEditRenderer());
		table.getColumn("Edit").setCellEditor(new ButtonEditEditor(new JCheckBox()));
		table.getColumn("Delete").setCellRenderer(new ButtonDeleteRenderer());
		table.getColumn("Delete").setCellEditor(new ButtonDeleteEditor(new JCheckBox()));
		table.setRowHeight(25);

		table.getColumnModel().getColumn(0).setMaxWidth(30);
		table.getColumn("DateHeure").setWidth(150);
		table.getColumn("Duree").setMaxWidth(50);
		table.getColumn("Salle").setMaxWidth(50);
		table.getColumn("Show").setMaxWidth(50);
		table.getColumn("Edit").setMaxWidth(50);
		table.getColumn("Delete").setMaxWidth(50);


		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);  // Colonne "Durée" alignée au centre
	}

	public void addProjection (Projection projection) {
		tableModel.addProjection(projection);
	}

	public void resetWith(java.util.List<Projection> projections){
		tableModel.clearAll();
		for (Projection projection: projections) {
			tableModel.addProjection(projection);
		}
		tableModel.fireTableDataChanged();
	}


}


