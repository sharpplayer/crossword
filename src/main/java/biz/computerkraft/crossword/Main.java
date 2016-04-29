package biz.computerkraft.crossword;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import biz.computerkraft.crossword.grid.crossword.Crossword;
import biz.computerkraft.crossword.gui.GridDialog;
import biz.computerkraft.crossword.gui.PropertyDialog;

public class Main {

	public static void main(String[] args) {
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
