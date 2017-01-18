package biz.computerkraft.crossword.gui.cluemodel;

import java.awt.Component;
import java.util.Optional;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import biz.computerkraft.crossword.db.DBConnection;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.gui.CellSelectListener;
import biz.computerkraft.crossword.gui.ClueModel;
import biz.computerkraft.crossword.gui.JHighDPITable;
import biz.computerkraft.crossword.gui.input.ClueCellEditor;

/**
 * 
 * Crossword clue model.
 * 
 * @author Raymond Francis
 *
 */
public class CrosswordClueModel extends ClueModel {

	/** Serial id. */
	private static final long serialVersionUID = -6192808124952758416L;

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

	/** Component to render. */
	private Component component;

	/** Clue table. */
	private JTable table;

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
			return getClueItemAt(rowIndex).getStartCell().getMarker();
		} else if (columnIndex == COLUMN_CLUE) {
			return getClueItemAt(rowIndex).getClue();
		} else if (columnIndex == COLUMN_WORD) {
			return getClueItemAt(rowIndex).getWord();
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
			return !getClueItemAt(rowIndex).getWord().isEmpty();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.ClueModel#isClueEditable(int)
	 */
	@Override
	public final boolean isClueEditable(final int index) {
		if (index >= 0 && index < getRowCount()) {
			return isCellEditable(index, COLUMN_CLUE);
		} else {
			return false;
		}
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
			getClueItemAt(rowIndex).setClue((Clue) aValue);
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
	public final Component getVisualComponent(final DBConnection connection, final CellSelectListener listener) {
		if (component == null) {
			table = new JHighDPITable(this);
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
	 * COnstructor for crossword clue model.
	 * 
	 * @param newCategory
	 *            category of clue model
	 */
	public CrosswordClueModel(final String newCategory) {
		super(newCategory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.ClueModel#selectClue(biz.computerkraft.
	 * crossword.grid.Cell, int)
	 */
	@Override
	public final boolean selectClue(final Cell cell, final int direction) {
		Optional<Clue> clue = cell.getClue(direction);
		if (clue.isPresent()) {
			int index = getClueItemIndex(clue.get());
			if (index == -1) {
				table.clearSelection();
			} else {
				table.setRowSelectionInterval(index, index);
				return isCellEditable(index, COLUMN_CLUE);
			}
		} else {
			table.clearSelection();
		}
		return false;
	}

}
