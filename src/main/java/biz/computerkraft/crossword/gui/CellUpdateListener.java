package biz.computerkraft.crossword.gui;

import java.util.List;

import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;

/**
 * 
 * Interface to listen to cell updates.
 * 
 * @author Raymond Francis
 *
 */
public interface CellUpdateListener {

	/**
	 * 
	 * Event raised when cell content to be added to cell.
	 * 
	 * @param cell
	 *            selected cell.
	 * @param content
	 *            content to add to cell
	 */
	void addCellContent(Cell cell, String content);

	/**
	 * 
	 * Resets cell content.
	 * 
	 * @param cell
	 *            selected cell.
	 */
	void clearCellContent(Cell cell);

	/**
	 * 
	 * Performs an action on a cell.
	 * 
	 * @param cell
	 *            selected cell.
	 * @param action
	 *            action to perform
	 */
	void cellMenuAction(Cell cell, String action);

	/**
	 * 
	 * Retrieves an action list for a cell.
	 * 
	 * @param cell
	 *            selected cell.
	 * @param actions
	 *            actions to perform
	 */
	void populateCellMenu(Cell cell, List<String> actions);

	/**
	 * 
	 * Sets current selection to word.
	 * 
	 * @param word
	 *            word to set.
	 */
	void setWord(Word word);

	/**
	 * Increases word list sort letter.
	 */
	void increaseSortLetter();

	/**
	 * Decreases word list sort letter.
	 */
	void decreaseSortLetter();

}
