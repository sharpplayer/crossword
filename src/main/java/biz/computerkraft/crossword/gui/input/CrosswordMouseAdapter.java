package biz.computerkraft.crossword.gui.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 
 * Mouse adapter for handling crossword mouse input.
 * 
 * @author Raymond Francis
 *
 */
public class CrosswordMouseAdapter extends MouseAdapter {

	/** Listener to receive callbacks. */
	private InputListener listener;

	/**
	 * 
	 * COnstrutor of mouse adapter.
	 * 
	 * @param newListener
	 *            input listener to retrieve events
	 */
	public CrosswordMouseAdapter(final InputListener newListener) {
		listener = newListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public final void mouseReleased(final MouseEvent e) {
		listener.selectCellAt(e.getX(), e.getY());
	}

}
