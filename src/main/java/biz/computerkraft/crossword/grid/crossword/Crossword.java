package biz.computerkraft.crossword.grid.crossword;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Grid;
import biz.computerkraft.crossword.grid.Symmetry;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.renderer.CrosswordCellRenderer;

/**
 * 
 * Classic crossword grid.
 * 
 * @author Raymond Francis
 *
 */
public class Crossword extends Grid {

	/** Height property. */
	private static final String PROPERTY_HEIGHT = "Height";

	/** Width property. */
	private static final String PROPERTY_WIDTH = "Width";

	/** Height property. */
	private static final String PROPERTY_SYMMETRY = "Symmetry";

	/** Fill action. */
	private static final String ACTION_FILL = "Fill";

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getProperties()
	 */
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
		Symmetry symmetry = (Symmetry) properties.get(PROPERTY_SYMMETRY);
		initialise(width, height, symmetry);
	}

	/**
	 * 
	 * Crossword grid constructor.
	 * 
	 * @param width
	 *            crossword width
	 * @param height
	 *            crossword height
	 * @param symmetry
	 *            crossword symmetry
	 */
	private void initialise(final int width, final int height, final Symmetry symmetry) {
		Map<String, Cell> grid = new HashMap<>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				grid.put(x + ":" + y, new Cell(x / (double) width, y / (double) height));
			}
		}

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

		setCells(grid.values());

		cellWidth = width;
		cellHeight = height;
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
	public final int getCellWidth() {
		return cellWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.PuzzleProperties#getCellHeight()
	 */
	@Override
	public final int getCellHeight() {
		return cellHeight;
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
		int forward = DIRECTION_E;
		if (Math.abs(selectionSpot.getX() - CELL_CENTRE) < Math.abs(selectionSpot.getY() - CELL_CENTRE)) {
			forward = DIRECTION_S;
		}
		int backward = getReverseDirection(forward);
		List<Cell> selection = getWordWithCell(cell, backward, forward);
		if (selection.size() == 1) {
			if (forward == DIRECTION_E) {
				forward = DIRECTION_S;
			} else {
				forward = DIRECTION_E;
			}
			backward = getReverseDirection(forward);
			selection = getWordWithCell(cell, backward, forward);
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
			if (isCellFilled(optionalCell.get())) {
				return cell;
			} else {
				return optionalCell.get();
			}
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
	public final void cellMenuAction(final Cell cell, final String action) {
		if (action.equals(ACTION_FILL)) {
			for (Cell symmetric : cell.getSymmetrics()) {
				symmetric.setBlock(DIRECTION_E, true);
				symmetric.setBlock(DIRECTION_W, true);
				symmetric.setBlock(DIRECTION_N, true);
				symmetric.setBlock(DIRECTION_S, true);
			}
		} else if (action.equals(ACTION_UNFILL)) {
			for (Cell symmetric : cell.getSymmetrics()) {
				unfill(symmetric, DIRECTION_E);
				unfill(symmetric, DIRECTION_W);
				unfill(symmetric, DIRECTION_N);
				unfill(symmetric, DIRECTION_S);
			}
		}
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
}
