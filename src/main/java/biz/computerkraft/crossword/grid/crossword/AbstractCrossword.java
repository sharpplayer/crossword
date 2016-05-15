package biz.computerkraft.crossword.grid.crossword;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Grid;

/**
 * 
 * Classic crossword grid.
 * 
 * @author Raymond Francis
 *
 */
public abstract class AbstractCrossword extends Grid {

	/** Height property. */
	private static final String PROPERTY_HEIGHT = "Height";

	/** Width property. */
	private static final String PROPERTY_WIDTH = "Width";

	/** North direction. */
	public static final int DIRECTION_N = 1;

	/** South direction. */
	public static final int DIRECTION_S = 2;

	/** East direction. */
	public static final int DIRECTION_E = 4;

	/** West direction. */
	public static final int DIRECTION_W = 8;

	/** Default width and height. */
	private static final int DEFAULT_SIZE = 15;

	/** Cell centre. */
	protected static final double CELL_CENTRE = 0.5;

	/** Properties list. */
	private final Map<String, Object> properties = new HashMap<>();

	/** Actual width set. */
	private int cellWidth;

	/** Actual height set. */
	private int cellHeight;

	/**
	 * 
	 * Get base properties.
	 * 
	 * @return base properties map
	 */
	@XmlTransient
	protected final Map<String, Object> getBaseProperties() {
		if (properties.size() == 0) {
			properties.put(PROPERTY_HEIGHT, new Integer(DEFAULT_SIZE));
			properties.put(PROPERTY_WIDTH, new Integer(DEFAULT_SIZE));
		}
		return properties;
	}

	/**
	 * 
	 * Sets base properties.
	 * 
	 * @param newProperties
	 *            properties to set.
	 */
	protected final void setBaseProperties(final Map<String, Object> newProperties) {
		int width = (Integer) newProperties.get(PROPERTY_WIDTH);
		int height = (Integer) newProperties.get(PROPERTY_HEIGHT);

		initialiseCells(width, height);
	}

	/**
	 * 
	 * Crossword grid constructor.
	 * 
	 * @param width
	 *            crossword width
	 * @param height
	 *            crossword height
	 */
	private void initialiseCells(final int width, final int height) {
		Map<String, Cell> grid = new HashMap<>();
		List<Cell> orderedGrid = new ArrayList<>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				String name = x + ":" + y;
				Cell newCell = new Cell(name, x / (double) width, y / (double) height);
				grid.put(name, newCell);
				orderedGrid.add(newCell);
			}
		}

		cellWidth = width;
		cellHeight = height;
		setCells(orderedGrid);
	}

	/**
	 * 
	 * Gets reverse of specified direction.
	 * 
	 * @param direction
	 *            direction to reverse
	 * @return reversed direction
	 */
	protected final int getReverseDirection(final int direction) {
		int reverse = 0;
		if ((direction & DIRECTION_N) != 0) {
			reverse |= DIRECTION_S;
		}
		if ((direction & DIRECTION_S) != 0) {
			reverse |= DIRECTION_N;
		}

		if ((direction & DIRECTION_W) != 0) {
			reverse |= DIRECTION_E;
		}
		if ((direction & DIRECTION_E) != 0) {
			reverse |= DIRECTION_W;
		}
		return reverse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getName()
	 */
	@Override
	public final String getName() {
		return getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getCellWidth()
	 */
	@Override
	@XmlElement(name = "width")
	public final int getCellWidth() {
		return cellWidth;
	}

	/**
	 * 
	 * XML deserialisation setter.
	 * 
	 * @param newWidth
	 *            new width to set.
	 */
	public final void setCellWidth(final int newWidth) {
		cellWidth = newWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getCellHeight()
	 */
	@Override
	@XmlElement(name = "height")
	public final int getCellHeight() {
		return cellHeight;
	}

	/**
	 * 
	 * XML deserialisation setter.
	 * 
	 * @param newHeight
	 *            new height of grid
	 */
	public final void setCellHeight(final int newHeight) {
		cellHeight = newHeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#addCellContent(biz.
	 * computerkraft.crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final void addCellContent(final Cell cell, final String content) {
		cell.setContents(content.toUpperCase());
		updateClues();
	}

	/**
	 * 
	 * Clears the content of a cell.
	 * 
	 * @param cell
	 *            cell to clear
	 */
	protected final void baseClearCellContent(final Cell cell) {
		cell.setContents("");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getCellLeft(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final Cell getCellLeft(final Cell cell) {
		return getCell(cell, DIRECTION_W);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getCellDown(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final Cell getCellDown(final Cell cell) {
		return getCell(cell, DIRECTION_S);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getCellRight(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final Cell getCellRight(final Cell cell) {
		return getCell(cell, DIRECTION_E);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getCellUp(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final Cell getCellUp(final Cell cell) {
		return getCell(cell, DIRECTION_N);
	}

	/**
	 * Get the string word.
	 * 
	 * @param cell
	 *            start cell of word
	 * @param direction
	 *            direction of word
	 * @return word string if full, blank if any letter missing
	 */
	protected final String getStringWord(final Cell cell, final int direction) {
		List<Cell> word = getWordWithCell(cell, getReverseDirection(direction), direction);
		String wordString = "";
		for (Cell letter : word) {
			if (letter.getContents().isEmpty()) {
				return "";
			} else {
				wordString += letter.getContents();
			}
		}
		return wordString;
	}

	/**
	 * Processes post load of global variables.
	 */
	protected final void basePostLoadTidyup() {
		properties.put(PROPERTY_HEIGHT, getCellHeight());
		properties.put(PROPERTY_WIDTH, getCellWidth());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getPointFromDirection(int)
	 */
	@Override
	public final Point2D getPointFromDirection(final int direction) {
		double x = CELL_CENTRE;
		double y = CELL_CENTRE;
		if ((direction & DIRECTION_E) != 0) {
			x += CELL_CENTRE;
		}
		if ((direction & DIRECTION_W) != 0) {
			x -= CELL_CENTRE;
		}
		if ((direction & DIRECTION_N) != 0) {
			y -= CELL_CENTRE;
		}
		if ((direction & DIRECTION_S) != 0) {
			y += CELL_CENTRE;
		}
		return new Point2D.Double(x, y);
	}

	/**
	 * 
	 * Basic addition of word content to grid.
	 * 
	 * @param cells
	 *            cells to put word into
	 * @param word
	 *            word to put
	 */
	protected final void baseAddWordContent(final List<Cell> cells, final Word word) {
		String wordString = word.getWord();
		for (int letter = 0; letter < wordString.length(); letter++) {
			cells.get(letter).setContents(wordString.substring(letter, letter + 1));
		}
	}
}
