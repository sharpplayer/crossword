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
	 */
	void selectCellAt(double x, double y);

}
