package biz.computerkraft.crossword.db;

import java.util.Map;

/**
 * 
 * Verse object.
 * 
 * @author Raymond Francis
 *
 */
public class Verse {

	/** Id of verse. */
	private int verseId;

	/** Id of book. */
	private int bookId;

	/** Chapter number. */
	private int chapter;

	/** Verse number. */
	private int verse;

	/** Verse text. */
	private String text = "";

	/**
	 * Constructor for verse.
	 * 
	 * @param newVerseId
	 *            verse id
	 * @param newBookId
	 *            book id
	 * @param newChapter
	 *            chapter number
	 * @param newVerse
	 *            verse number
	 */
	public Verse(final int newVerseId, final int newBookId, final int newChapter, final int newVerse) {
		verseId = newVerseId;
		bookId = newBookId;
		chapter = newChapter;
		verse = newVerse;
	}

	/**
	 * 
	 * Returns string representation of book.
	 * 
	 * @param bookRefs
	 *            gets reference given a book list
	 * @return reference string
	 */
	public final String getReference(final Map<Integer, String> bookRefs) {
		return bookRefs.get(bookId) + " " + chapter + ":" + verse;
	}

	/**
	 * 
	 * Gets text for verse.
	 * 
	 * @param connection
	 *            database connection
	 * @return text of verse
	 */
	public final String getText(final DBConnection connection) {
		if (text.isEmpty()) {
			text = connection.getVerse(verseId);
		}
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return getReference(DBConnection.getFullBookNames());
	}
}
