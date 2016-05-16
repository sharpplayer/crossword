package biz.computerkraft.crossword.grid.crossword;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.cluemodel.WordsearchClueModel;
import biz.computerkraft.crossword.gui.renderer.BarwordCellRenderer;

/**
 * 
 * Clued sukoku variant.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "sudoku")
public class Sudoku extends MultiDirectionGrid {

	/** Search clue category. */
	private static final String CATEGORY_CLUES = "Clues";

	/** Default cell group width. */
	private static final int DEFAULT_CELL_GROUP_WIDTH = 3;

	/** Default cell group height. */
	private static final int DEFAULT_CELL_GROUP_HEIGHT = 3;

	/** Property name for cell group height. */
	private static final String PROPERTY_CELL_GROUP_WIDTH = "Cell Group Width";

	/** Property name for cell group height. */
	private static final String PROPERTY_CELL_GROUP_HEIGHT = "Cell Group Height";

	/** Cell group width. */
	private int cellGroupWidth = DEFAULT_CELL_GROUP_WIDTH;

	/** Cell group width. */
	private int cellGroupHeight = DEFAULT_CELL_GROUP_HEIGHT;

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getProperties()
	 */
	@Override
	public final Map<String, Object> getProperties() {
		Map<String, Object> properties = getBaseProperties();
		if (!properties.containsKey(PROPERTY_CELL_GROUP_WIDTH)) {
			properties.remove(RectangleGrid.PROPERTY_WIDTH);
			properties.remove(RectangleGrid.PROPERTY_HEIGHT);
			properties.put(PROPERTY_CELL_GROUP_WIDTH, cellGroupWidth);
			properties.put(PROPERTY_CELL_GROUP_HEIGHT, cellGroupHeight);
		}
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.Grid#getClues()
	 */
	@Override
	public final List<ClueItem> getClues() {
		List<ClueItem> clues = new ArrayList<>();
		for (Cell cell : getCells()) {
			for (Entry<Integer, Clue> clueEntry : cell.getClues().entrySet()) {
				clues.add(new ClueItem(clueEntry.getValue(), CATEGORY_CLUES, cell, clueEntry.getKey(),
						clueEntry.getValue().getClueText()));
			}
		}
		return clues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#setProperties(java.util.Map)
	 */
	@Override
	public final void setProperties(final Map<String, Object> properties) {
		cellGroupWidth = (int) properties.get(PROPERTY_CELL_GROUP_HEIGHT);
		cellGroupHeight = (int) properties.get(PROPERTY_CELL_GROUP_WIDTH);
		properties.put(PROPERTY_WIDTH, cellGroupWidth * cellGroupHeight);
		properties.put(PROPERTY_HEIGHT, cellGroupWidth * cellGroupHeight);
		setMultiDirectionGridProperties(properties);
		Map<String, Cell> grid = getCellMap();
		for (int x = 1; x < cellGroupHeight; x++) {
			for (int y = 0; y < cellGroupWidth * cellGroupHeight; y++) {
				String name = (x * cellGroupWidth - 1) + ":" + y;
				grid.get(name).setBlock(DIRECTION_E, true);
			}
		}
		for (int x = 0; x < cellGroupHeight * cellGroupHeight; x++) {
			for (int y = 1; y < cellGroupWidth; y++) {
				String name = x + ":" + (y * cellGroupHeight - 1);
				grid.get(name).setBlock(DIRECTION_S, true);
			}
		}
		addClueModel(new WordsearchClueModel(CATEGORY_CLUES));
	}

	/**
	 * Gets the cell group width.
	 * 
	 * @return the cellGroupWidth
	 */
	@XmlElement(name = "cellGroupWidth")
	public final int getCellGroupWidth() {
		return cellGroupWidth;
	}

	/**
	 * Sets the cell group width.
	 * 
	 * @param newCellGroupWidth
	 *            the cell group width
	 */
	public final void setCellGroupWidth(final int newCellGroupWidth) {
		cellGroupWidth = newCellGroupWidth;
	}

	/**
	 * Gets cell group height.
	 * 
	 * @return the cell group height
	 */
	@XmlElement(name = "cellGroupHeight")
	public final int getCellGroupHeight() {
		return cellGroupHeight;
	}

	/**
	 * 
	 * Sets cell group height.
	 * 
	 * @param newCellGroupHeight
	 *            the new cell group height
	 */
	public final void setCellGroupHeight(final int newCellGroupHeight) {
		cellGroupHeight = newCellGroupHeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getRendererClass()
	 */
	@Override
	public final Class<? extends CellRenderer> getRendererClass() {
		return BarwordCellRenderer.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getIndirectSelection(biz.
	 * computerkraft.crossword.grid.Cell, java.awt.geom.Point2D)
	 */
	@Override
	public final List<Cell> getIndirectSelection(final Cell cell, final Point2D offset) {
		return getRestrictedIndirectSelection(cell, offset, true, true);
	}

}
