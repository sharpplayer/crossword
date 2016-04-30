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
	 */
	public final void setWordList(final String word) {
		if (word.length() != lastWord.length()) {
			updateList(word, 1);
		} else {
			updateList(word, sortLetterIndex);
		}
	}

	/**
	 * Updates list.
	 * 
	 * @param word
	 *            wildcarded word to search for.
	 * @param sortIndex
	 *            sort on index.
	 */
	private void updateList(final String word, final int sortIndex) {
		sortLetterIndex = sortIndex;
		lastWord = word;
		wordList = connection.getWords(word, sortLetterIndex);
		fireContentsChanged(this, 0, wordList.size());

	}

	/**
	 * 
	 * Increases sort letter.
	 * 
	 */
	public final void increaseSortLetter() {
		if (sortLetterIndex < lastWord.length()) {
			updateList(lastWord, sortLetterIndex + 1);
		}
	}

	/**
	 * 
	 * Decreases sort letter.
	 * 
	 */
	public final void decreaseSortLetter() {
		if (sortLetterIndex > 1) {
			updateList(lastWord, sortLetterIndex - 1);
		}
	}

}
