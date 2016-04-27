package biz.computerkraft.crossword.gui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 
 * Key listener for crossword control.
 * 
 * @author Raymond Francis
 *
 */
public class CrosswordKeyListener implements KeyListener {

	/** Listener to callback. */
	private InputListener listener;

	/**
	 * 
	 * COnstructor.
	 * 
	 * @param newListener
	 *            listener to receive callbacks
	 */
	public CrosswordKeyListener(final InputListener newListener) {
		listener = newListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public final void keyTyped(final KeyEvent e) {
		String key = "" + e.getKeyChar();
		if (!key.matches("\\W+")) {
			listener.addCellContent(key);
		} else if (e.getKeyChar() == KeyEvent.VK_DELETE) {
			listener.deleteCellContent();
		} else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
			listener.backspaceCellContent();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public final void keyPressed(final KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			listener.moveLeft();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			listener.moveUp();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			listener.moveRight();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			listener.moveDown();
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
