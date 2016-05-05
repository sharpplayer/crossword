package biz.computerkraft.crossword.gui.input;

import java.awt.Component;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import biz.computerkraft.crossword.db.ClueWriter;
import biz.computerkraft.crossword.db.DBConnection;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.gui.CellUpdateListener;
import biz.computerkraft.crossword.gui.ClueEditor;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.ClueModel;

/**
 * 
 * Implementation of the cell editor.
 * 
 * @author Raymond Francis
 *
 */
public class ClueCellEditor extends AbstractCellEditor implements TableCellEditor, ClueWriter {

	/** Serial id. */
	private static final long serialVersionUID = -686861361499643521L;

	/** Clue editor control. */
	private ClueEditor editor = new ClueEditor(this);

	/** Database connection. */
	private DBConnection connection;

	/** Call back listener for cell selection. */
	private CellUpdateListener listener;

	/** Current word. */
	private String word;

	/**
	 * 
	 * Constructor for jtable cell editor.
	 * 
	 * @param newConnection
	 *            database connection
	 * @param newListener
	 *            cell selection listener
	 */
	public ClueCellEditor(final DBConnection newConnection, final CellUpdateListener newListener) {
		connection = newConnection;
		listener = newListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public final Object getCellEditorValue() {
		return editor.getSelectedItem(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing
	 * .JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public final Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
			final int row, final int column) {
		ClueItem clue = ((ClueModel) table.getModel()).getClueItemAt(row);
		word = clue.getWord();
		List<Clue> clues = connection.getClues(word);
		listener.selectCell(clue.getStartCell(), clue.getDirection());
		editor.setClue(clues, (Clue) value);
		return editor;
	}

	/**
	 * 
	 * Adds the tabs action overrides to the table.
	 * 
	 * @param table
	 *            table to add actions to
	 * @param column
	 *            column number that has clue editor
	 */
	public final void addTableActions(final JTable table, final int column) {
		TableCellTabAction action = new TableCellTabAction(table, column, true, editor);
		table.getActionMap().put(action.getKey(), action);
		action = new TableCellTabAction(table, column, false, editor);
		table.getActionMap().put(action.getKey(), action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.db.ClueWriter#saveClue(biz.computerkraft.
	 * crossword.grid.Clue)
	 */
	@Override
	public final void saveClue(final Clue clue) {
		connection.saveClue(word, clue);

	}
}
