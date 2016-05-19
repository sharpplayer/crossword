package biz.computerkraft.crossword.grid.crossword;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;

import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.grid.crossword.enumeration.Symmetry;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.cluemodel.CrosswordClueModel;

/**
 * 
 * Classic crossword base class.
 * 
 * @author Raymond Francis
 *
 */
public abstract class AbstractCrossword extends RectangleGrid {

	/** Across category. */
	private static final String CATEGORY_ACROSS = "Across";

	/** Down category. */
	private static final String CATEGORY_DOWN = "Down";

	/** Height property. */
	private static final String PROPERTY_SYMMETRY = "Symmetry";

	/** Actual symmetry. */
	private Symmetry symmetry = Symmetry.EIGHTWAY;

	/**
	 * 
	 * Sets the abstract crossword properties.
	 * 
	 * @param properties
	 *            property container
	 */
	protected final void setAbstractCrosswordProperties(final Map<String, Object> properties) {
		setBaseProperties(properties);
		symmetry = (Symmetry) properties.get(PROPERTY_SYMMETRY);
		setCellGroups();
		setMarkers();
		setClueModels();
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
		return getRestrictedIndirectSelection(cell, selectionSpot, false, false);
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
	protected final void unfill(final Cell cell, final int direction) {

		Optional<Cell> adjacent = cell.getAdjacent(direction);
		if (adjacent.isPresent()) {
			cell.setBlock(direction, isCellFilled(adjacent.get()));
		}
	}

	/**
	 * Sets the markers on the cells.
	 */
	protected final void setAcrossDownMarkers() {
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
		updateClues();
	}

	/**
	 * Sets the markers on the cells.
	 */
	protected final void setAcrossDownClueModel() {
		addClueModel(new CrosswordClueModel(CATEGORY_ACROSS));
		addClueModel(new CrosswordClueModel(CATEGORY_DOWN));
	}

	/**
	 * 
	 * Get classic across down clue list.
	 * 
	 * @return classic across down clue list
	 */
	protected final List<ClueItem> getAcrossDownClues() {

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

	/**
	 * 
	 * Gets the abstract crossword properties.
	 * 
	 * @return property map
	 */
	protected final Map<String, Object> getAbstractCrosswordProperties() {
		Map<String, Object> properties = getBaseProperties();
		if (!properties.containsKey(PROPERTY_SYMMETRY)) {
			properties.put(PROPERTY_SYMMETRY, symmetry);
		}
		return properties;
	}

	/**
	 * Post tidy up for abstract crossword.
	 */
	protected final void abstractCrosswordPostLoadTidyup() {
		basePostLoadTidyup();
		getBaseProperties().put(PROPERTY_SYMMETRY, getSymmetry());
		setCellGroups();
		setClueModels();
	}

	/**
	 * Sets up cell symmetry and adjacency.
	 */
	private void setCellGroups() {
		Map<String, Cell> grid = new HashMap<>();
		for (Cell cell : getCells()) {
			grid.put(cell.getName(), cell);
		}
		int width = getCellWidth();
		int height = getCellHeight();
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
}
