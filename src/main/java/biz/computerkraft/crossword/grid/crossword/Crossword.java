package biz.computerkraft.crossword.grid.crossword;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.grid.Grid;
import biz.computerkraft.crossword.grid.Symmetry;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.renderer.CrosswordCellRenderer;

/**
 * 
 * Classic crossword grid.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "crossword")
public class Crossword extends Grid {

	/** Height property. */
	private static final String PROPERTY_HEIGHT = "Height";

	/** Width property. */
	private static final String PROPERTY_WIDTH = "Width";

	/** Height property. */
	private static final String PROPERTY_SYMMETRY = "Symmetry";

	/** Fill action. */
	private static final String ACTION_FILL = "Fill";

	/** Across category. */
	private static final String CATEGORY_ACROSS = "Across";

	/** Down category. */
	private static final String CATEGORY_DOWN = "Down";

	/** Unfill action. */
	private static final String ACTION_UNFILL = "Unfill";

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
	private static final double CELL_CENTRE = 0.5;

	/** Properties list. */
	private static final HashMap<String, Object> PROPERTIES = new HashMap<>();

	/** Actual width set. */
	private int cellWidth;

	/** Actual height set. */
	private int cellHeight;

	/** Actual symmetry. */
	private Symmetry symmetry;

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getProperties()
	 */
	@XmlTransient
	@Override
	public final Map<String, Object> getProperties() {
		if (PROPERTIES.size() == 0) {
			PROPERTIES.put(PROPERTY_HEIGHT, new Integer(DEFAULT_SIZE));
			PROPERTIES.put(PROPERTY_WIDTH, new Integer(DEFAULT_SIZE));
			PROPERTIES.put(PROPERTY_SYMMETRY, Symmetry.EIGHTWAY);
		}
		return PROPERTIES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.PuzzleProperties#setProperties(java.util.
	 * Map)
	 */
	@Override
	public final void setProperties(final Map<String, Object> properties) {
		int width = (Integer) properties.get(PROPERTY_WIDTH);
		int height = (Integer) properties.get(PROPERTY_HEIGHT);
		Symmetry newSymmetry = (Symmetry) properties.get(PROPERTY_SYMMETRY);
		initialise(width, height, newSymmetry);
	}

	/**
	 * 
	 * Crossword grid constructor.
	 * 
	 * @param width
	 *            crossword width
	 * @param height
	 *            crossword height
	 * @param newSymmetry
	 *            crossword symmetry
	 */
	private void initialise(final int width, final int height, final Symmetry newSymmetry) {
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

		setCellGroups(grid, width, height);

		setCells(orderedGrid);

		setMarkers();

		cellWidth = width;
		cellHeight = height;
		symmetry = newSymmetry;
	}

	/**
	 * 
	 * Sets up the adjacency and symmetry cell groups.
	 * 
	 * @param grid
	 *            map of cells
	 * @param width
	 *            cell width of grid
	 * @param height
	 *            cell height of grid
	 */
	private void setCellGroups(final Map<String, Cell> grid, final int width, final int height) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell cell = grid.get(x + ":" + y);
				Cell north = grid.get(x + ":" + (y - 1));
				Cell south = grid.get(x + ":" + (y + 1));
				Cell east = grid.get((x + 1) + ":" + y);
				Cell west = grid.get((x - 1) + ":" + y);
				if (north != null) {
					cell.setAdjacent(DIRECTION_N, north);
				}
				if (south != null) {
					cell.setAdjacent(DIRECTION_S, south);
				}
				if (east != null) {
					cell.setAdjacent(DIRECTION_E, east);
				}
				if (west != null) {
					cell.setAdjacent(DIRECTION_W, west);
				}

				cell.addSymmetric(cell);
				if (symmetry == Symmetry.X || symmetry == Symmetry.XY || symmetry == Symmetry.EIGHTWAY) {
					cell.addSymmetric(grid.get((width - x - 1) + ":" + y));
				}
				if (symmetry == Symmetry.Y || symmetry == Symmetry.XY || symmetry == Symmetry.EIGHTWAY) {
					cell.addSymmetric(grid.get(x + ":" + (height - y - 1)));
				}
				if (symmetry == Symmetry.ROTATE90 || symmetry == Symmetry.ROTATE180 || symmetry == Symmetry.XY
						|| symmetry == Symmetry.EIGHTWAY) {
					cell.addSymmetric(grid.get((width - x - 1) + ":" + (height - y - 1)));
				}
				if (symmetry == Symmetry.EIGHTWAY) {
					cell.addSymmetric(grid.get(y + ":" + x));
				}
				if (symmetry == Symmetry.ROTATE90 || symmetry == Symmetry.EIGHTWAY) {
					cell.addSymmetric(grid.get(y + ":" + (height - x - 1)));
				}
				if (symmetry == Symmetry.ROTATE90 || symmetry == Symmetry.ROTATE180 || symmetry == Symmetry.EIGHTWAY) {
					cell.addSymmetric(grid.get((width - y - 1) + ":" + x));
				}
				if (symmetry == Symmetry.EIGHTWAY) {
					cell.addSymmetric(grid.get((width - y - 1) + ":" + (height - x - 1)));
				}
			}
		}

	}

	/**
	 * 
	 * Gets reverse of specified direction.
	 * 
	 * @param direction
	 *            direction to reverse
	 * @return reversed direction
	 */
	private int getReverseDirection(final int direction) {
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
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getRendererClass()
	 */
	@Override
	public final Class<? extends CellRenderer> getRendererClass() {
		return CrosswordCellRenderer.class;
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
	 * @see
	 * biz.computerkraft.crossword.gui.PuzzleProperties#getIndirectSelection(biz
	 * .computerkraft.crossword.grid.Cell, java.awt.geom.Point2D)
	 */
	@Override
	public final List<Cell> getIndirectSelection(final Cell cell, final Point2D selectionSpot) {
		List<Cell> selection;
		if (isCellFilled(cell)) {
			selection = new ArrayList<>();
		} else {
			int forward = DIRECTION_E;

			if (Math.abs(selectionSpot.getX() - CELL_CENTRE) < Math.abs(selectionSpot.getY() - CELL_CENTRE)) {
				forward = DIRECTION_S;
			}
			int backward = getReverseDirection(forward);
			selection = getWordWithCell(cell, backward, forward);
			if (selection.size() == 1) {
				if (forward == DIRECTION_E) {
					forward = DIRECTION_S;
				} else {
					forward = DIRECTION_E;
				}
				backward = getReverseDirection(forward);
				selection = getWordWithCell(cell, backward, forward);
			}
		}
		return selection;
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.PuzzleProperties#clearCellContent(biz.
	 * computerkraft.crossword.grid.Cell)
	 */
	@Override
	public final void clearCellContent(final Cell cell) {
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
	 * 
	 * Get adjacent cell in given direction.
	 * 
	 * @param cell
	 *            cell to move from
	 * @param direction
	 *            direction to move
	 * @return new cell
	 */
	private Cell getCell(final Cell cell, final int direction) {
		Optional<Cell> optionalCell = cell.getAdjacent(direction);
		if (optionalCell.isPresent()) {
			return optionalCell.get();
		} else {
			return cell;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#cellMenuAction(biz.
	 * computerkraft.crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final boolean cellMenuAction(final Cell cell, final String action) {

		boolean dirtyReturn = false;
		if (action.equals(ACTION_FILL)) {
			for (Cell symmetric : cell.getSymmetrics()) {
				symmetric.setBlock(DIRECTION_E, true);
				symmetric.setBlock(DIRECTION_W, true);
				symmetric.setBlock(DIRECTION_N, true);
				symmetric.setBlock(DIRECTION_S, true);
			}
			setMarkers();
			dirtyReturn = true;
		} else if (action.equals(ACTION_UNFILL)) {
			for (Cell symmetric : cell.getSymmetrics()) {
				unfill(symmetric, DIRECTION_E);
				unfill(symmetric, DIRECTION_W);
				unfill(symmetric, DIRECTION_N);
				unfill(symmetric, DIRECTION_S);
			}
			setMarkers();
			dirtyReturn = true;
		}

		return dirtyReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.PuzzleProperties#populateCellMenu(biz.
	 * computerkraft.crossword.grid.Cell, java.util.List)
	 */
	@Override
	public final void populateCellMenu(final Cell cell, final List<String> actions) {

		if (isCellFilled(cell)) {
			for (Cell adjacent : cell.getAdjacents()) {
				if (!isCellFilled(adjacent)) {
					actions.add(ACTION_UNFILL);
					break;
				}
			}
		} else {
			boolean fillable = true;
			for (Cell symmetricCell : cell.getSymmetrics()) {
				if (!symmetricCell.getContents().isEmpty()) {
					fillable = false;
					break;
				}
			}
			if (fillable) {
				actions.add(ACTION_FILL);
			}
		}
	}

	/**
	 * 
	 * Gets full blocked status of cell.
	 * 
	 * @param cell
	 *            cell to test for filled status
	 * 
	 * @return blocked status
	 */
	private boolean isCellFilled(final Cell cell) {
		return cell.isBlocked(DIRECTION_E) && cell.isBlocked(DIRECTION_N) && cell.isBlocked(DIRECTION_S)
				&& cell.isBlocked(DIRECTION_W);
	}

	/**
	 * 
	 * Unblocks an adjacent of cell.
	 * 
	 * @param cell
	 *            cell to unfill
	 * @param direction
	 *            direction to check unfill
	 */
	private void unfill(final Cell cell, final int direction) {

		Optional<Cell> adjacent = cell.getAdjacent(direction);
		if (adjacent.isPresent()) {
			cell.setBlock(direction, isCellFilled(adjacent.get()));
		}
	}

	/**
	 * Sets the markers on the cells.
	 */
	private void setMarkers() {
		int marker = 1;
		for (Cell cell : getCells()) {
			if (!isCellFilled(cell) && ((cell.isBlocked(DIRECTION_W) && !cell.isBlocked(DIRECTION_E))
					|| (cell.isBlocked(DIRECTION_N) && !cell.isBlocked(DIRECTION_S)))) {
				cell.setMarker(Integer.toString(marker));
				marker++;
			} else {
				cell.setMarker("");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getClueCategories()
	 */
	@Override
	public final List<String> getClueCategories() {
		return Arrays.asList(CATEGORY_ACROSS, CATEGORY_DOWN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getClues()
	 */
	@Override
	public final List<ClueItem> getClues() {

		List<ClueItem> clues = new ArrayList<>();
		for (Cell cell : getCells()) {
			if (cell.getMarker().isEmpty()) {
				cell.clearClue(DIRECTION_E);
				cell.clearClue(DIRECTION_S);
			} else {
				if (cell.isBlocked(DIRECTION_W) && !cell.isBlocked(DIRECTION_E)) {
					Clue clue;
					Optional<Clue> optionalClue = cell.getClue(DIRECTION_E);
					if (!optionalClue.isPresent()) {
						clue = new Clue();
						cell.setClue(DIRECTION_E, clue);
					} else {
						clue = optionalClue.get();
					}
					clues.add(new ClueItem(clue, CATEGORY_ACROSS, cell, DIRECTION_E, getStringWord(cell, DIRECTION_E)));
				}
				if (cell.isBlocked(DIRECTION_N) && !cell.isBlocked(DIRECTION_S)) {
					Clue clue;
					Optional<Clue> optionalClue = cell.getClue(DIRECTION_S);
					if (!optionalClue.isPresent()) {
						clue = new Clue();
						cell.setClue(DIRECTION_S, clue);
					} else {
						clue = optionalClue.get();
					}
					clues.add(new ClueItem(clue, CATEGORY_DOWN, cell, DIRECTION_S, getStringWord(cell, DIRECTION_S)));
				}
			}
		}
		return clues;
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
	private String getStringWord(final Cell cell, final int direction) {
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
	 * 
	 * Gets symmetry.
	 * 
	 * @return crossword symmetry.
	 */
	@XmlElement(name = "symmetry")
	public final Symmetry getSymmetry() {
		return symmetry;
	}

	/**
	 * 
	 * XML deserialisation helper.
	 * 
	 * @param newSymmetry
	 *            new symmetry for crossword
	 */
	public final void setSymmetry(final Symmetry newSymmetry) {
		symmetry = newSymmetry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#postLoadTidyup()
	 */
	@Override
	public final void postLoadTidyup() {
		PROPERTIES.put(PROPERTY_HEIGHT, getCellHeight());
		PROPERTIES.put(PROPERTY_WIDTH, getCellWidth());
		PROPERTIES.put(PROPERTY_SYMMETRY, getSymmetry());
		Map<String, Cell> grid = new HashMap<>();
		for (Cell cell : getCells()) {
			grid.put(cell.getName(), cell);
		}
		setCellGroups(grid, getCellWidth(), getCellHeight());

	}
}
