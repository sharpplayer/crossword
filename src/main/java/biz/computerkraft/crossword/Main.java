package biz.computerkraft.crossword;

import javax.swing.SwingUtilities;

import biz.computerkraft.crossword.grid.crossword.Crossword;
import biz.computerkraft.crossword.gui.GridDialog;
import biz.computerkraft.crossword.gui.PropertyDialog;

/**
 * 
 * Main entry class for application.
 * 
 * @author Raymond Francis
 *
 */
public final class Main {

	/**
	 * Constructor.
	 */
	private Main() {

	}

	/**
	 * 
	 * Startup class and function for the crossword puzzle designer.
	 * 
	 * @param args
	 *            Main arguments.
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				GridDialog mainFrame = new GridDialog();
				PropertyDialog dialog = new PropertyDialog(mainFrame);
				dialog.registerPuzzle(Crossword.class);
				dialog.setVisible(true);
			}
		});
	}
}
