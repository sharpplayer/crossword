package biz.computerkraft.crossword.gui;

import java.util.List;

import javax.swing.AbstractListModel;

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
	 * Sets new list for model.
	 * 
	 * @param list
	 *            new list
	 */
	public final void setWordList(final List<Word> list) {
		wordList = list;
		fireContentsChanged(this, 0, wordList.size());
	}

}
