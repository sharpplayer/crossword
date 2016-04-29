package biz.computerkraft.crossword.grid;

/**
 * 
 * Class to manage a clue.
 * 
 * @author Raymond Francis
 *
 */
public class Clue {

	/** Text of clue. */
	private String clueText = "";

	/** Database id of clue. */
	private int clueId = 0;

	/**
	 * 
	 * Gets the clue text.
	 * 
	 * @return the clueText
	 */
	public final String getClueText() {
		return clueText;
	}

	/**
	 * 
	 * Sets the clue text.
	 * 
	 * @param newClueText
	 *            the clueText to set
	 */
	public final void setClueText(final String newClueText) {
		this.clueText = newClueText;
	}

	/**
	 * Get the database id of clue.
	 * 
	 * @return the clueId
	 */
	public final int getClueId() {
		return clueId;
	}

	/**
	 * Sets the clue id from database.
	 * 
	 * @param newClueId
	 *            the clueId to set
	 */

	public final void setClueId(final int newClueId) {
		this.clueId = newClueId;
	}

}
