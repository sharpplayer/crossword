package biz.computerkraft.crossword.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import biz.computerkraft.crossword.db.DBConnection;

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
}
