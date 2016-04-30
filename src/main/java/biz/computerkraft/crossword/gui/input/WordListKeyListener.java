package biz.computerkraft.crossword.gui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import biz.computerkraft.crossword.gui.CellUpdateListener;

/**
 * 
 * Key listener for crossword control.
 * 
 * @author Raymond Francis
 *
 */
public class WordListKeyListener implements KeyListener {

	/** Listener to callback. */
	private CellUpdateListener listener;

	/**
	 * 
	 * COnstructor.
	 * 
	 * @param newListener
	 *            listener to receive callbacks
	 */
	public WordListKeyListener(final CellUpdateListener newListener) {
		listener = newListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public final void keyTyped(final KeyEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public final void keyPressed(final KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			listener.increaseSortLetter();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			listener.decreaseSortLetter();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public final void keyReleased(final KeyEvent e) {
	}
}
