package biz.computerkraft.crossword.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * 
 * Main puzzle renderer.
 * 
 * @author Raymond Francis
 *
 */
public class GridDialog extends JDialog {

	/** Serial id. */
	private static final long serialVersionUID = -1887552377378707619L;

	/** Properties dialog. */
	private PropertyDialog propertyDialog;

	public GridDialog() {
		super((JDialog) null, "Crossword");
	}

	public void activatePuzzle(PuzzleProperties puzzle, PropertyDialog properties) {
		propertyDialog = properties;
		setModal(true);
		setVisible(true);
	}
}
