package biz.computerkraft.crossword.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.util.List;

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
public class GridDialog extends JDialog implements CellUpdateListener {

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

	/** Cell centre. */
	private static final double CELL_CENTRE = 0.5;

	/** Properties dialog. */
	private PropertyDialog propertyDialog;

	/** Database connection. */
	private DBConnection connection = new DBConnection();

	/** Panel for crossword. */
	private CrosswordPanel crosswordGrid = new CrosswordPanel(this);

	/** Word list model. */
	private WordListModel wordlListModel = new WordListModel();

	/** Crossword viewer. */
	private JScrollPane crosswordViewer;

	/** Puzzle. */
	private PuzzleProperties puzzle;

	/** Last offset for getting direction of indirect selections. */
	private Point2D lastOffset = new Point2D.Double(CELL_CENTRE, 0.0);

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
		SpringUtilities.makeCompactGrid(wordGrid, 1, 1, MARGIN, MARGIN, MARGIN, MARGIN);
	}

	/**
	 * Activates puzzle.
	 * 
	 * @param newPuzzle
	 *            puzzle to set up
	 * @param properties
	 *            puzzle properties
	 */
	public final void activatePuzzle(final PuzzleProperties newPuzzle, final PropertyDialog properties) {
		propertyDialog = properties;
		puzzle = newPuzzle;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellUpdateListener#selectCell(biz.
	 * computerkraft.crossword.grid.Cell, java.awt.geom.Point2D)
	 */
	@Override
	public final void selectCell(final Cell cell, final Point2D offset) {
		lastOffset = offset;
		crosswordGrid.setDirectSelection(cell);
		List<Cell> indirectSelection = puzzle.getIndirectSelection(cell, offset);
		crosswordGrid.setIndirectSelection(indirectSelection);
		updateWordList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#addCellContent(biz.
	 * computerkraft.crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final void addCellContent(final Cell cell, final String content) {
		puzzle.addCellContent(cell, content);
		updateWordList();
	}

	/**
	 * Updates word list.
	 */
	private void updateWordList() {
		String word = "";
		for (Cell indirectCell : crosswordGrid.getFirstIndirectSelection()) {
			if (indirectCell.getContents().isEmpty()) {
				word += " ";
			} else {
				word += indirectCell.getContents();
			}
		}
		wordlListModel.setWordList(connection.getWords(word, 1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#clearCellContent(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void clearCellContent(final Cell cell) {
		puzzle.clearCellContent(cell);
		updateWordList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#selectCellLeft(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void selectCellLeft(final Cell cell) {
		Cell newCell = puzzle.getCellLeft(cell);
		selectCell(newCell, lastOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.CellUpdateListener#selectCellUp(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void selectCellUp(final Cell cell) {
		Cell newCell = puzzle.getCellUp(cell);
		selectCell(newCell, lastOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#selectCellRight(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void selectCellRight(final Cell cell) {
		Cell newCell = puzzle.getCellRight(cell);
		selectCell(newCell, lastOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#selectCellDown(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void selectCellDown(final Cell cell) {
		Cell newCell = puzzle.getCellDown(cell);
		selectCell(newCell, lastOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#cellMenuAction(biz.
	 * computerkraft.crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final void cellMenuAction(final Cell cell, final String action) {
		puzzle.cellMenuAction(cell, action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.CellUpdateListener#populateCellMenu(biz.
	 * computerkraft.crossword.grid.Cell, java.util.List)
	 */
	@Override
	public final void populateCellMenu(final Cell cell, final List<String> actions) {
		puzzle.populateCellMenu(cell, actions);
	}
}
