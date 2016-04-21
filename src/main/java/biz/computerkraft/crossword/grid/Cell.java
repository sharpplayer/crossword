package biz.computerkraft.crossword.grid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

	/** Blocked directions. */
	private Map<Integer, Boolean> blocks = new HashMap<>();

	/** Cell contents. */
	private String contents = "";

	/** Clues for direction. */
	private Map<Integer, String> clues = new HashMap<>();

	/** Cell marker. */
	private String marker = "";

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
		blocks.put(direction, block);
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
}
