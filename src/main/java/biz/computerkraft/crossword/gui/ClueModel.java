package biz.computerkraft.crossword.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import biz.computerkraft.crossword.db.DBConnection;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;

/**
 * 
 * Clue model class for clue display.
 * 
 * @author Raymond Francis
 *
 */
public abstract class ClueModel extends AbstractTableModel {

	/** Serial id. */
	private static final long serialVersionUID = 676551072067562953L;

	/** Clues to display. */
	private List<ClueItem> clueItems = new ArrayList<>();

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
	public abstract Component getVisualComponent(final DBConnection connection, final CellSelectListener listener);

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

	/**
	 * 
	 * Informs a model that a clue has been updated (not from the table).
	 * 
	 * @param clue
	 *            clue updated
	 */
	public final void updateClue(final Clue clue) {
		int updatedRow = 0;
		for (ClueItem clueItem : clueItems) {
			if (clueItem.getClue().equals(clue)) {
				fireTableRowsUpdated(updatedRow, updatedRow);
				break;
			}
			updatedRow++;
		}
	}

	/**
	 * 
	 * Selects a clue for cell in direction for potential edit.
	 * 
	 * @param cell
	 *            Clue to highlight select
	 * @param direction
	 *            direction of clue
	 * @return true if clue is editable
	 */
	public abstract boolean selectClue(Cell cell, int direction);

	/**
	 * 
	 * Given a clue, returns the index of the clue item.
	 * 
	 * @param clue
	 *            clue to use for search
	 * @return clue item index
	 */
	protected final int getClueItemIndex(final Clue clue) {
		for (ClueItem clueItem : clueItems) {
			if (clueItem.getClue().equals(clue)) {
				return clueItems.indexOf(clueItem);
			}
		}
		return -1;
	}

	/**
	 * 
	 * Gets if clue is editable.
	 * 
	 * @param index
	 *            index of row in model
	 * 
	 * @return clue is editable
	 */
	public abstract boolean isClueEditable(int index);
}
