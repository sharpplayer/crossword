package biz.computerkraft.crossword.gui.input;

/**
 * 
 * Handles crossword specific inputs.
 * 
 * @author Raymond Francis
 *
 */
public interface InputListener {

	/**
	 * 
	 * Request to select a cell at given location.
	 * 
	 * @param x
	 *            x position to select at
	 * @param y
	 *            y position to select at
	 * @param withPopup
	 *            show popup
	 */
	void selectCellAt(double x, double y, boolean withPopup);

	/**
	 * 
	 * New cell content to add.
	 * 
	 * @param content
	 *            content to add to cell
	 */
	void addCellContent(String content);

	/**
	 * 
	 * Deletes cell content.
	 * 
	 */
	void deleteCellContent();

	/**
	 * 
	 * Backspace cell content.
	 * 
	 */
	void backspaceCellContent();

	/**
	 * 
	 * Move selection left a cell.
	 * 
	 */
	void moveLeft();

	/**
	 * 
	 * Move selection up a cell.
	 * 
	 */
	void moveUp();

	/**
	 * 
	 * Move selection right a cell.
	 * 
	 */
	void moveRight();

	/**
	 * 
	 * Move selection down a cell.
	 * 
	 */
	void moveDown();
}
