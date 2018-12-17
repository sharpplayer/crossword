package biz.computerkraft.crossword.grid;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import biz.computerkraft.crossword.grid.crossword.RectangleGrid;

/**
 * 
 * Puzzle cell.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "cell")
public class Cell {

	/** Adjacent cells. */
	private Map<Integer, Cell> adjacents = new HashMap<>();

	/** Symmetric cells. */
	private List<Cell> symmetrics = new ArrayList<>();

	/** Blocked directions. */
	private Map<Integer, Boolean> blocks = new HashMap<>();

	/** Cell contents. */
	private String contents = "";

	/** Clues for direction. */
	private Map<Integer, Clue> clues = new HashMap<>();

	/** Cell marker. */
	private String marker = "";

	/** Anchor x position. */
	private double anchorX;

	/** Anchor y position. */
	private double anchorY;

	/** Useful cell identifier. */
	private String name;

	/** Transient cell content. */
	private String transientContents = "";

	/**
	 * Constructor for JAXB.
	 */
	public Cell() {
	}

	/**
	 * Cell constructor.
	 * 
	 * @param newName
	 *            name of cell
	 * @param anchorXPc
	 *            percentage of width to anchor cell in puzzle
	 * @param anchorYPc
	 *            percentage of height to anchor cell in puzzle
	 */
	public Cell(final String newName, final double anchorXPc, final double anchorYPc) {
		anchorX = anchorXPc;
		anchorY = anchorYPc;
		name = newName;
	}

	/**
	 * 
	 * Gets adjacent cell in given direction.
	 * 
	 * @param direction
	 *            direction to check
	 * 
	 * @return the adjacent cell
	 */
	public final Optional<Cell> getAdjacent(final int direction) {
		if (adjacents.containsKey(direction)) {
			return Optional.of(adjacents.get(direction));
		} else {
			return Optional.empty();
		}
	}

	/**
	 * 
	 * Sets an adjacent cell in given direction.
	 * 
	 * @param direction
	 *            direction of adjacent
	 * @param cell
	 *            cell to put
	 */
	public final void setAdjacent(final int direction, final Cell cell) {
		adjacents.put(direction, cell);
	}

	/**
	 * 
	 * Gets the block in given direction.
	 * 
	 * @param direction
	 *            direction to check block
	 * 
	 * @return the block in given direction
	 */
	public final boolean isBlocked(final int direction) {
		if (blocks.containsKey(direction)) {
			return blocks.get(direction);
		} else {
			return !adjacents.containsKey(direction);
		}
	}

	/**
	 * 
	 * Blocks in a given direction from cell.
	 * 
	 * @param direction
	 *            of block
	 * 
	 * @param block
	 *            the block to set
	 */
	public final void setBlock(final int direction, final boolean block) {
		boolean oldBlock = false;
		if (blocks.containsKey(direction)) {
			oldBlock = blocks.get(direction);
		}
		if (oldBlock != block) {
			blocks.put(direction, block);
			Optional<Cell> adjacent = getAdjacent(direction);
			if (adjacent.isPresent()) {
				Cell adjacentCell = adjacent.get();
				for (Entry<Integer, Cell> adjacentEntry : adjacentCell.adjacents.entrySet()) {
					if (adjacentEntry.getValue().equals(this)) {
						adjacentCell.setBlock(adjacentEntry.getKey(), block);
						break;
					}
				}
			}
		}
	}

	/**
	 * 
	 * Gets the cell contents.
	 * 
	 * @return the contents
	 */
	@XmlElement(name = "contents")
	public final String getContents() {
		return contents;
	}

	/**
	 * 
	 * Sets the cell contents.
	 * 
	 * @param newContents
	 *            the contents to set
	 */
	public final void setContents(final String newContents) {
		this.contents = newContents;
	}

	/**
	 * 
	 * Gets clue in specified direction.
	 * 
	 * @param direction
	 *            direction of desired clue.
	 * 
	 * @return the clue in given direction
	 */
	public final Optional<Clue> getClue(final int direction) {
		if (clues.containsKey(direction)) {
			return Optional.of(clues.get(direction));
		} else {
			return Optional.empty();
		}
	}

	/**
	 * 
	 * Sets clue in given direction.
	 * 
	 * @param direction
	 *            clue direction
	 * @param clue
	 *            the clue to set
	 */
	public final void setClue(final int direction, final Clue clue) {
		this.clues.put(direction, clue);
	}

	/**
	 * 
	 * Clears clue in given direction.
	 * 
	 * @param direction
	 *            clue direction
	 */
	public final void clearClue(final int direction) {
		this.clues.remove(direction);
	}

	/**
	 * 
	 * Returns marker string for cell.
	 * 
	 * @return the marker
	 */
	@XmlElement(name = "marker")
	public final String getMarker() {
		return marker;
	}

	/**
	 * 
	 * Sets the marker for cell.
	 * 
	 * @param newMarker
	 *            the marker to set
	 */
	public final void setMarker(final String newMarker) {
		marker = newMarker;
	}

	/**
	 * 
	 * Returns transient contents of cell.
	 * 
	 * @return the special status
	 */
	@XmlTransient
	public final String getTransientContents() {
		return transientContents;
	}

	/**
	 * 
	 * Sets the transient contents for cell.
	 * 
	 * @param newContents
	 *            the content to set
	 */
	public final void setTransientContents(final String newContents) {
		transientContents = newContents;
	}

	/**
	 * 
	 * Gets the cell location x as percentage of width.
	 * 
	 * @return the anchor percentage X
	 */
	@XmlElement(name = "anchorX")
	public final double getAnchorX() {
		return anchorX;
	}

	/**
	 * 
	 * Sets anchoring x position.
	 * 
	 * @param newX
	 *            new anchor x position
	 */
	public final void setAnchorX(final double newX) {
		anchorX = newX;
	}

	/**
	 * 
	 * Gets the cell location y as percentage of height.
	 * 
	 * @return the anchor percentage Y
	 */
	@XmlElement(name = "anchorY")
	public final double getAnchorY() {
		return anchorY;
	}

	/**
	 * 
	 * Sets anchoring y position.
	 * 
	 * @param newY
	 *            new anchor y position
	 */
	public final void setAnchorY(final double newY) {
		anchorY = newY;
	}

	/**
	 * 
	 * Get symmetric cell group.
	 * 
	 * @return cells in symmetric group
	 */

	public final List<Cell> getSymmetrics() {
		return symmetrics;
	}

	/**
	 * 
	 * Get adjacents group.
	 * 
	 * @return adjacent cells
	 */
	@XmlTransient
	public final Collection<Cell> getAdjacents() {
		return adjacents.values();
	}

	/**
	 * 
	 * Adds a symmetric cell.
	 * 
	 * @param symmetric
	 *            cell to add to symmetry group
	 */
	public final void addSymmetric(final Cell symmetric) {
		if (!symmetrics.contains(symmetric)) {
			symmetrics.add(symmetric);
		}
	}

	/**
	 * XML serialisation of clues.
	 * 
	 * @return clues to serialise
	 */
	@XmlElement(name = "clues")
	public final Map<Integer, Clue> getClues() {
		return clues;
	}

	/**
	 * XML deserialisation of clues.
	 * 
	 * @param newClues
	 *            deserialised clues
	 */
	public final void setClues(final Map<Integer, Clue> newClues) {
		clues = newClues;
	}

	/**
	 * XML serialisation of blocks.
	 * 
	 * @return blocks to serialise
	 */
	@XmlElement(name = "blocks")
	public final Map<Integer, Boolean> getBlocks() {
		return blocks;
	}

	/**
	 * XML deserialisation of blocks.
	 * 
	 * @param newBlocks
	 *            blocks to set
	 */
	public final void setBlocks(final Map<Integer, Boolean> newBlocks) {
		blocks = newBlocks;
	}

	/**
	 * 
	 * Gets cell name.
	 * 
	 * @return the name of the cell
	 */
	@XmlElement(name = "name")
	public final String getName() {
		return name;
	}

	/**
	 * 
	 * Sets cell name.
	 * 
	 * @param newName
	 *            the name of cell
	 */
	public final void setName(final String newName) {
		this.name = newName;
	}

	/**
	 * 
	 * Gets direction of assumed adjacent cell.
	 * 
	 * @param cell
	 *            cell to get direction from this cell
	 * @param defaultDirection
	 *            if cell not adjacent, default return value
	 * @return direction of cell
	 */
	public final int getDirection(final Cell cell, final int defaultDirection) {
		for (Entry<Integer, Cell> entry : adjacents.entrySet()) {
			if (entry.getValue().equals(cell)) {
				return entry.getKey();
			}
		}
		return defaultDirection;
	}

	/**
	 * 
	 * Gets the status of clues.
	 * 
	 * @return true if there are clues.
	 */
	public final boolean hasClues() {
		return clues.size() > 0;
	}

	/**
	 * 
	 * Gets cell emptiness.
	 * 
	 * @return true if cell is empty
	 */
	@XmlTransient
	public final boolean isEmpty() {
		return transientContents.isEmpty() && contents.isEmpty();
	}

	/**
	 * 
	 * Gets display contents.
	 * 
	 * @return contents for display
	 */
	@XmlTransient
	public final String getDisplayContents() {
		if (contents.isEmpty()) {
			return transientContents;
		} else {
			return contents;
		}
	}

	/**
	 * Gets the cell rectangle based on a grid.
	 * 
	 * @param width
	 *            full width of grid
	 * @param height
	 *            full height of grid
	 * @param border
	 *            width of cell border
	 * @param adjustSEOnly
	 *            only adjust the SE border
	 * @return shape that forms cell
	 */
	public final Rectangle getRectangleGridShape(final double width, final double height, final double border,
			final boolean adjustSEOnly) {
		double widthPc = 1 - anchorX;
		double heightPc = 1 - anchorY;
		if (getAdjacent(RectangleGrid.DIRECTION_E).isPresent()) {
			widthPc = getAdjacent(RectangleGrid.DIRECTION_E).get().getAnchorX() - anchorX;
		}
		if (getAdjacent(RectangleGrid.DIRECTION_S).isPresent()) {
			heightPc = getAdjacent(RectangleGrid.DIRECTION_S).get().getAnchorY() - anchorY;
		}

		int adjustNW = 0;
		int adjustSE = 0;
		if (border != 0.0) {
			adjustSE = 1;
			if (!adjustSEOnly) {
				adjustNW = 1;
			}
		}
		return new Rectangle((int) (Math.round(anchorX * width) + border) + adjustNW,
				(int) (Math.round(anchorY * height) + border) + adjustNW,
				(int) (Math.round(widthPc * width) - 2 * border) - adjustSE,
				(int) (Math.round(heightPc * height) - 2 * border) - adjustSE);
	}

	/**
	 * Gets edges bitwise pattern.
	 * 
	 * @param allEdges
	 *            if true, requires all edges to be set otherwise 0 returned.
	 * @return bitwise map of edges pattern
	 */
	public final int getFill(final boolean allEdges) {
		int fill = 0;
		for (Entry<Integer, Boolean> block : blocks.entrySet()) {
			if (block.getValue()) {
				fill |= block.getKey();
			} else if (allEdges) {
				fill = 0;
				break;
			}
		}
		return fill;
	}
}
