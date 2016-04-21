package biz.computerkraft.crossword.db;

/**
 * 
 * Database word object.
 * 
 * @author Raymond Francis
 *
 */
public class Word {

	/** Word identfier. */
	private final int identifier;

	/** Word text. */
	private final String word;

	/**
	 * Contructor for new word.
	 * 
	 * @param newIdentifier
	 *            word id
	 * @param newWord
	 *            word text
	 */
	public Word(final int newIdentifier, final String newWord) {
		identifier = newIdentifier;
		word = newWord;
	}

	/**
	 * 
	 * Gets word identfier.
	 * 
	 * @return the identifier
	 */
	public final int getIdentifier() {
		return identifier;
	}

	/**
	 * 
	 * Gets word text.
	 * 
	 * @return the word
	 */
	public final String getWord() {
		return word;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return getWord();
	}

}
