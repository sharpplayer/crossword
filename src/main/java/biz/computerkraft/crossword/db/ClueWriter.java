package biz.computerkraft.crossword.db;

import biz.computerkraft.crossword.grid.Clue;

/**
 * 
 * Event handler to write clue to database.
 * 
 * @author Raymond Francis
 *
 */
public interface ClueWriter {

	/**
	 * 
	 * Event raised to save clue.
	 * 
	 * @param clue
	 *            clue to write
	 */
	void saveClue(Clue clue);
}
