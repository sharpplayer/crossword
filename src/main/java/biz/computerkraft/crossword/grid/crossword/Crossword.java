package biz.computerkraft.crossword.grid.crossword;

import java.util.HashMap;
import java.util.Map;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Grid;
import biz.computerkraft.crossword.grid.Symmetry;

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

	/** North direction. */
	private static final int DIRECTION_N = 1;

	/** South direction. */
	private static final int DIRECTION_S = 2;

	/** East direction. */
	private static final int DIRECTION_E = 4;

	/** West direction. */
	private static final int DIRECTION_W = 8;

	/** Default width and height. */
	private static final int DEFAULT_SIZE = 15;

	/** Properties list. */
	private static final HashMap<String, Object> PROPERTIES = new HashMap<>();

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
				grid.put(x + ":" + y, new Cell());
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
}
