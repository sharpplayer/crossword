package biz.computerkraft.crossword.gui.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import biz.computerkraft.crossword.gui.CellUpdateListener;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.ClueModel;

/**
 * 
 * Mouse adapter for word list.
 * 
 * @author Raymond Francis
 *
 */
public class TableMouseAdapter extends MouseAdapter {

	/** Listener for callbacks. */
	private CellUpdateListener listener;

	/**
	 * 
	 * Constructor for mouse adapter.
	 * 
	 * @param newListener
	 *            listener to cell updates.
	 */
	public TableMouseAdapter(final CellUpdateListener newListener) {
		listener = newListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public final void mouseClicked(final MouseEvent event) {
		JTable table = (JTable) event.getSource();
		int index = table.rowAtPoint(event.getPoint());
		if (index != -1) {
			ClueModel model = (ClueModel) table.getModel();
			ClueItem item = model.getClueItemAt(index);
			listener.selectCell(item.getStartCell(), item.getDirection());
		}
	}

}
