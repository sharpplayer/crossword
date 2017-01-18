package biz.computerkraft.crossword.gui.input;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;

import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.gui.CellUpdateListener;

/**
 * 
 * Mouse adapter for word list.
 * 
 * @author Raymond Francis
 *
 */
public class WordListMouseAdapter extends MouseAdapter {

	/** Listener for callbacks. */
	private CellUpdateListener listener;

	/**
	 * 
	 * Constructor for mouse adapter.
	 * 
	 * @param newListener
	 *            listener to cell updates.
	 */
	public WordListMouseAdapter(final CellUpdateListener newListener) {
		listener = newListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public final void mouseClicked(final MouseEvent event) {
		JList<?> list = (JList<?>) event.getSource();
		Rectangle bounds = list.getCellBounds(0, list.getLastVisibleIndex());
		if (bounds != null && bounds.contains(event.getPoint())) {
			int index = list.locationToIndex(event.getPoint());
			if (event.getClickCount() == 2) {
				listener.setWord((Word) list.getModel().getElementAt(index));
			} else if (event.getClickCount() == 1) {
				listener.setVersesWithWord((Word) list.getModel().getElementAt(index));
				list.setSelectedIndex(index);
			}
		}
	}

}
