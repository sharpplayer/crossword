package biz.computerkraft.crossword.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * 
 * Puzzle cell.
 * 
 * @author Raymond Francis
 *
 */
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
	private Map<Integer, String> clues = new HashMap<>();

	/** Cell marker. */
	private String marker = "";

	/** Anchor x position. */
	private double anchorX;

	/** Anchor y position. */
	private double anchorY;

	/**
	 * Cell constructor.
	 * 
	 * @param anchorXPc
	 *            percentage of width to anchor cell in puzzle
	 * @param anchorYPc
	 *            percentage of height to anchor cell in puzzle
	 */
	public Cell(final double anchorXPc, final double anchorYPc) {
		anchorX = anchorXPc;
		anchorY = anchorYPc;
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
	public final Optional<String> getClue(final int direction) {
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
	public final void setClue(final int direction, final String clue) {
		this.clues.put(direction, clue);
	}

	/**
	 * 
	 * Returns marker string for cell.
	 * 
	 * @return the marker
	 */
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
	 * Gets the cell location x as percentage of width.
	 * 
	 * @return the anchor percentage X
	 */
	public final double getAnchorX() {
		return anchorX;
	}

	/**
	 * 
	 * Gets the cell location y as percentage of height.
	 * 
	 * @return the anchor percentage Y
	 */
	public final double getAnchorY() {
		return anchorY;
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
}
