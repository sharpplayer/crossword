package biz.computerkraft.crossword.gui.cluemodel;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import biz.computerkraft.crossword.db.DBConnection;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.gui.CellSelectListener;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.ClueModel;
import biz.computerkraft.crossword.gui.JHighDPITable;

/**
 * 
 * Clue model for codeword.
 * 
 * @author Raymond Francis
 *
 */
public class CodewordClueModel extends ClueModel {

	/** Serial id. */
	private static final long serialVersionUID = 2319124365171969773L;

	/** Letter column index. */
	private static final int COLUMN_LETTER = 0;

	/** Code column index. */
	private static final int COLUMN_CODE = 1;

	/** Used column index. */
	private static final int COLUMN_USED = 2;

	/** Visual component to display model. */
	private JComponent component;

	/** Table showing clues. */
	private JTable table;

	/**
	 * Codeword constructor.
	 * 
	 * @param newCategory
	 *            category of clues
	 */
	public CodewordClueModel(final String newCategory) {
		super(newCategory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public final int getColumnCount() {
		return COLUMN_USED + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public final String getColumnName(final int column) {
		if (column == COLUMN_LETTER) {
			return "Letter";
		} else if (column == COLUMN_CODE) {
			return "Code";
		} else if (column == COLUMN_USED) {
			return "Used";
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public final Object getValueAt(final int rowIndex, final int columnIndex) {
		ClueItem clueItem = getClueItemAt(rowIndex);
		if (columnIndex == COLUMN_LETTER) {
			return clueItem.getClue().getClueText();
		} else if (columnIndex == COLUMN_CODE) {
			return clueItem.getClue().getClueId();
		} else {
			return clueItem.getStartCell() != null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.ClueModel#getVisualComponent(biz.
	 * computerkraft.crossword.db.DBConnection,
	 * biz.computerkraft.crossword.gui.CellSelectListener)
	 */
	@Override
	public final Component getVisualComponent(final DBConnection connection, final CellSelectListener listener) {
		if (component == null) {
			table = new JHighDPITable(this);
			component = new JScrollPane(table);
		}
		return component;
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
		String content = cell.getContents();
		for (int clue = 0; clue < getRowCount(); clue++) {
			ClueItem clueItem = getClueItemAt(clue);
			if (clueItem.getClue().getClueText().equals(content)) {
				table.setRowSelectionInterval(clue, clue);
				return true;
			}
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
