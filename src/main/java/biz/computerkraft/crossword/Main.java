package biz.computerkraft.crossword;

import java.awt.Font;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import biz.computerkraft.crossword.grid.crossword.Barword;
import biz.computerkraft.crossword.grid.crossword.Crossword;
import biz.computerkraft.crossword.grid.crossword.Sudoku;
import biz.computerkraft.crossword.grid.crossword.Wordsearch;
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

		for (Object key : UIManager.getLookAndFeelDefaults().keySet()) {
			if (key instanceof String) {
				if (key.toString().endsWith(".font")) {
					Font font = UIManager.getFont(key);
					Font biggerFont = font.deriveFont(2.0f * font.getSize2D());
					UIManager.put(key, biggerFont);
				}
			}
		}
		UIManager.put("ScrollBar.width", 2 * UIManager.getInt("ScrollBar.width"));

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				GridDialog mainFrame = new GridDialog();
				PropertyDialog dialog = new PropertyDialog(mainFrame);
				dialog.registerPuzzle(Crossword.class);
				dialog.registerPuzzle(Barword.class);
				dialog.registerPuzzle(Wordsearch.class);
				dialog.registerPuzzle(Sudoku.class);
				dialog.setVisible(true);
			}
		});
	}
}
