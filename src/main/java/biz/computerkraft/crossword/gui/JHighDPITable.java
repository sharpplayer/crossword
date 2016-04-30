package biz.computerkraft.crossword.gui;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * 
 * Helper table class for High DPI.
 * 
 * @author Raymond Francis
 *
 */
public class JHighDPITable extends JTable {

	/** Serial id. */
	private static final long serialVersionUID = -1359846094122079647L;

	/**
	 * 
	 * Most useful constructor.
	 * 
	 * @param model
	 *            table model
	 */
	public JHighDPITable(final TableModel model) {
		super(model);
		setRowHeight(getRowHeight() * 2);
	}
}
