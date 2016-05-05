package biz.computerkraft.crossword.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import biz.computerkraft.crossword.db.DBConnection;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.gui.input.ClueCellEditor;

/**
 * 
 * Clue model class for clue display.
 * 
 * @author Raymond Francis
 *
 */
public class ClueModel extends AbstractTableModel {

	/** Serial id. */
	private static final long serialVersionUID = 676551072067562953L;

	/** Marker column. */
	private static final int COLUMN_MARKER = 0;

	/** Clue column. */
	private static final int COLUMN_CLUE = 1;

	/** Word column. */
	private static final int COLUMN_WORD = 2;

	/** Marker column width. */
	private static final int WIDTH_MARKER = 50;

	/** Clue column width. */
	private static final int WIDTH_CLUE = 500;

	/** Clues to display. */
	private List<ClueItem> clueItems = new ArrayList<>();

	/** Component to render. */
	private Component component;

	/** Category of clues. */
	private String category;

	/**
	 * 
	 * Constructs model for clue display.
	 * 
	 * @param newCategory
	 *            catgory for clue list
	 */
	public ClueModel(final String newCategory) {
		category = newCategory;
	}

	/**
	 * 
	 * Sets the clue table.
	 * 
	 * @param clues
	 *            all clues to be filtered by category
	 */
	public final void setClues(final List<ClueItem> clues) {
		clueItems.clear();
		for (ClueItem item : clues) {
			if (item.getCategory().equals(category)) {
				clueItems.add(item);
			}
		}
		fireTableDataChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public final int getRowCount() {
		return clueItems.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public final int getColumnCount() {
		return COLUMN_WORD + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public final Object getValueAt(final int rowIndex, final int columnIndex) {
		if (columnIndex == COLUMN_MARKER) {
			return clueItems.get(rowIndex).getStartCell().getMarker();
		} else if (columnIndex == COLUMN_CLUE) {
			return clueItems.get(rowIndex).getClue();
		} else if (columnIndex == COLUMN_WORD) {
			return clueItems.get(rowIndex).getWord();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public final boolean isCellEditable(final int rowIndex, final int columnIndex) {
		if (columnIndex == COLUMN_CLUE) {
			return !clueItems.get(rowIndex).getWord().isEmpty();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
	 * int, int)
	 */
	@Override
	public final void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
		if (columnIndex == COLUMN_CLUE) {
			clueItems.get(rowIndex).setClue((Clue) aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public final String getColumnName(final int column) {
		if (column == COLUMN_CLUE) {
			return "Clue";
		} else if (column == COLUMN_MARKER) {
			return "Marker";
		} else if (column == COLUMN_WORD) {
			return "Word";
		}
		return "";
	}

	/**
	 * 
	 * Get visualisation of model.
	 * 
	 * @param connection
	 *            db connection
	 * @param listener
	 *            cell selection listener
	 * 
	 * @return a visual component for the model.
	 */
	public final Component getVisualComponent(final DBConnection connection, final CellUpdateListener listener) {
		if (component == null) {
			JTable table = new JHighDPITable(this);
			TableColumn clueColumn = table.getColumnModel().getColumn(COLUMN_MARKER);
			clueColumn.setMaxWidth(WIDTH_MARKER);
			clueColumn = table.getColumnModel().getColumn(COLUMN_CLUE);
			clueColumn.setMinWidth(WIDTH_CLUE);
			ClueCellEditor editor = new ClueCellEditor(connection, listener);
			editor.addTableActions(table, COLUMN_CLUE);
			clueColumn.setCellEditor(editor);
			component = new JScrollPane(table);
		}
		return component;
	}

	/**
	 * 
	 * Gets clue model category.
	 * 
	 * @return category
	 */
	public final String getCategory() {
		return category;
	}

	/**
	 * Gets clue item for given index.
	 * 
	 * @param index
	 *            index of clue item
	 * @return clue item at index
	 */
	public final ClueItem getClueItemAt(final int index) {
		if (index >= 0 && index < clueItems.size()) {
			return clueItems.get(index);
		} else {
			return null;
		}
	}
}
