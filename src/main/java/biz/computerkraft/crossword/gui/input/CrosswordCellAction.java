package biz.computerkraft.crossword.gui.input;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.gui.CellUpdateListener;

/**
 * 
 * Processes cell based menu actions.
 * 
 * @author Raymond Francis
 *
 */
public class CrosswordCellAction extends AbstractAction {

	/** Serialisation id. */
	private static final long serialVersionUID = -7466523885187579024L;

	/** Cell to action. */
	private final Cell actionCell;

	/** Callback. */
	private final CellUpdateListener listener;

	/**
	 * 
	 * Action constructor.
	 * 
	 * @param cell
	 *            cell actioning
	 * @param action
	 *            action to perform
	 * @param newListener
	 *            listener to invoke on action
	 */
	public CrosswordCellAction(final Cell cell, final String action, final CellUpdateListener newListener) {
		actionCell = cell;
		putValue(NAME, action);
		listener = newListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public final void actionPerformed(final ActionEvent e) {
		listener.cellMenuAction(actionCell, e.getActionCommand());
	}

}
