package biz.computerkraft.crossword.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import biz.computerkraft.crossword.db.DBConnection;
import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;

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

	/** Default font size for word list. */
	private static final int DEFAULT_FONT_SIZE = 40;

	/** Margin around controls. */
	private static final int MARGIN = 10;

	/** Default cell size on screen. */
	private static final int DEFAULT_CELL_SIZE = 40;

	/** Default scrollpane border. */
	private static final int SCROLLPANE_BORDER = 3;

	/** Properties dialog. */
	private PropertyDialog propertyDialog;

	/** Database connection. */
	private DBConnection connection = new DBConnection();

	/** Panel for crossword. */
	private CrosswordPanel crosswordGrid = new CrosswordPanel();

	/** Word list model. */
	private WordListModel wordlListModel = new WordListModel();

	/** Crossword viewer. */
	private JScrollPane crosswordViewer;

	/**
	 * Constructor.
	 */
	public GridDialog() {
		super((JDialog) null, "Crossword");
		Container pane = getContentPane();
		pane.setLayout(new SpringLayout());
		crosswordViewer = new JScrollPane(crosswordGrid);
		pane.add(crosswordViewer);

		JPanel wordGrid = new JPanel(new SpringLayout());
		pane.add(wordGrid);
		JList<Word> wordList = new JList<>(wordlListModel);
		wordGrid.add(new JScrollPane(wordList));
		wordList.setFont(new Font("Courier New", Font.BOLD, DEFAULT_FONT_SIZE));
		wordlListModel.setWordList(connection.getWords("A   ", 1));
		SpringUtilities.makeCompactGrid(wordGrid, 1, 1, MARGIN, MARGIN, MARGIN, MARGIN);
	}

	/**
	 * Activates puzzle.
	 * 
	 * @param puzzle
	 *            puzzle to set up
	 * @param properties
	 *            puzzle properties
	 */
	public final void activatePuzzle(final PuzzleProperties puzzle, final PropertyDialog properties) {
		propertyDialog = properties;
		crosswordGrid.reset(
				new Dimension(puzzle.getCellWidth() * DEFAULT_CELL_SIZE, puzzle.getCellHeight() * DEFAULT_CELL_SIZE));
		crosswordViewer.setPreferredSize(new Dimension(puzzle.getCellWidth() * DEFAULT_CELL_SIZE + SCROLLPANE_BORDER,
				puzzle.getCellHeight() * DEFAULT_CELL_SIZE + SCROLLPANE_BORDER));
		Class<? extends CellRenderer> renderClass = puzzle.getRendererClass();
		try {
			for (Cell cell : puzzle.getCells()) {
				CellRenderer renderer = renderClass.newInstance();
				renderer.setCell(cell);
				crosswordGrid.addRenderer(renderer);
			}
			SpringUtilities.makeCompactGrid(getContentPane(), 1, 2, MARGIN, MARGIN, MARGIN, MARGIN);
			pack();
			setModal(true);
			setVisible(true);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
