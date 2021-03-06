package biz.computerkraft.crossword.gui.cluemodel;

import java.awt.Component;
import java.util.Optional;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import biz.computerkraft.crossword.db.DBConnection;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.gui.CellSelectListener;
import biz.computerkraft.crossword.gui.ClueModel;
import biz.computerkraft.crossword.gui.JHighDPITable;

/**
 * 
 * Crossword clue model.
 * 
 * @author Raymond Francis
 *
 */
public class WordsearchClueModel extends ClueModel {

	/** Serial id. */
	private static final long serialVersionUID = -8508461576811177563L;

	/** Clue column. */
	private static final int COLUMN_CLUE = 0;

	/** Component to render. */
	private Component component;

	/** Table for clues. */
	private JTable table;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public final int getColumnCount() {
		return COLUMN_CLUE + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public final Object getValueAt(final int rowIndex, final int columnIndex) {
		if (columnIndex == COLUMN_CLUE) {
			return getClueItemAt(rowIndex).getClue();
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
		return false;
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
	public WordsearchClueModel(final String newCategory) {
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
			}
		} else {
			table.clearSelection();
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
		return false;
	}

}
