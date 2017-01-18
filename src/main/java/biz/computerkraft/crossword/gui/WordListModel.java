package biz.computerkraft.crossword.gui;

import java.util.List;

import javax.swing.AbstractListModel;

import biz.computerkraft.crossword.db.DBConnection;
import biz.computerkraft.crossword.db.Word;

/**
 * 
 * Model to manage words.
 * 
 * @author Raymond Francis
 *
 */
public class WordListModel extends AbstractListModel<Word> {

	/** Serial id. */
	private static final long serialVersionUID = 4665629640243684057L;

	/** Word list being managed. */
	private List<Word> wordList;

	/** Connection to database. */
	private DBConnection connection;

	/** Sort letter index. */
	private int sortLetterIndex = 1;

	/** Last word searched for. */
	private String lastWord = "";

	/**
	 * 
	 * Constructs a word list.
	 * 
	 * @param newConnection
	 *            DB connection
	 */
	public WordListModel(final DBConnection newConnection) {
		connection = newConnection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public final int getSize() {
		if (wordList == null) {
			return 0;
		} else {
			return wordList.size();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public final Word getElementAt(final int index) {
		return wordList.get(index);
	}

	/**
	 * 
	 * Sets new template for word model.
	 * 
	 * @param word
	 *            new tempate for populating word list
	 * @return true if list change
	 */
	public final boolean setWordList(final String word) {
		if (word.length() != lastWord.length()) {
			return updateList(word, 1);
		} else {
			return updateList(word, sortLetterIndex);
		}
	}

	/**
	 * Updates list.
	 * 
	 * @param word
	 *            wildcarded word to search for.
	 * @param sortIndex
	 *            sort on index.
	 * @return true if change of word
	 */
	private boolean updateList(final String word, final int sortIndex) {
		boolean returnValue = !lastWord.equals(word) || (sortLetterIndex != sortIndex);
		if (returnValue) {
			sortLetterIndex = sortIndex;
			wordList = connection.getWords(word, sortLetterIndex);
			lastWord = word;
			fireContentsChanged(this, 0, wordList.size());
		}
		return returnValue;
	}

	/**
	 * 
	 * Increases sort letter.
	 * 
	 * @return true if list changes
	 * 
	 */
	public final boolean increaseSortLetter() {
		if (sortLetterIndex < lastWord.length()) {
			return updateList(lastWord, sortLetterIndex + 1);
		}
		return false;
	}

	/**
	 * 
	 * Decreases sort letter.
	 * 
	 * @return true if list changed.
	 * 
	 */
	public final boolean decreaseSortLetter() {
		if (sortLetterIndex > 1) {
			return updateList(lastWord, sortLetterIndex - 1);
		}
		return false;
	}

}
