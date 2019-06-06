package biz.computerkraft.crossword.gui.input;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

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
	private final transient Cell actionCell;

	/** Callback. */
	private final transient CellUpdateListener updateListener;

	/** Component to repaint on completed action. */
	private final JComponent component;

	/**
	 * 
	 * Action constructor.
	 * 
	 * @param cell
	 *            cell actioning
	 * @param action
	 *            action to perform
	 * @param newUpdateListener
	 *            listener to invoke on action
	 * @param crosswordPanel
	 *            panel to repaint on action
	 */
	public CrosswordCellAction(final Cell cell, final String action, final CellUpdateListener newUpdateListener,
			final JComponent crosswordPanel) {
		actionCell = cell;
		putValue(NAME, action);
		component = crosswordPanel;
		updateListener = newUpdateListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public final void actionPerformed(final ActionEvent e) {
		updateListener.cellMenuAction(actionCell, e.getActionCommand());
		component.repaint();
	}

}
