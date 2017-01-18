package biz.computerkraft.crossword.grid.crossword;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;

/**
 * 
 * Multi-direction words grid.
 * 
 * @author Raymond Francis
 *
 */
public abstract class MultiDirectionGrid extends RectangleGrid {

	/** Search index not known. */
	private static final int END_NOT_KNOWN = -2;

	/** Search index not known. */
	private static final int START_NOT_KNOWN = -1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.grid.crossword.RectangleGrid#
	 * setRectangleGridProperties(java.util.Map)
	 */
	@Override
	protected final void setRectangleGridProperties(final Map<String, Object> properties) {
		setCellGroups();
		setMultiDirectionGridProperties(properties);
	}

	/**
	 * 
	 * Sets properties.
	 * 
	 * @param properties
	 *            properties to set
	 */
	protected abstract void setMultiDirectionGridProperties(Map<String, Object> properties);

	/**
	 * 
	 * Gets a free indirect selection.
	 * 
	 * @param cell
	 *            cell to start indirect selection
	 * @param selectionSpot
	 *            selection spot in cell
	 * @return indirect selection
	 */
	protected final List<Cell> getFreeIndirectSelection(final Cell cell, final Point2D selectionSpot) {
		int forward = getDirectionFromPoint(selectionSpot);
		return getWordWithCell(cell, 0, forward);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.Puzzle#getDirectionFromPoint(java.awt.
	 * geom.Point2D)
	 */
	@Override
	public final int getDirectionFromPoint(final Point2D point) {
		int forward = 0;
		int dx = (int) Math.round((point.getX() - CELL_CENTRE) * 2);
		int dy = (int) Math.round((point.getY() - CELL_CENTRE) * 2);
		if (dx < 0) {
			forward = DIRECTION_W;
		} else if (dx > 0) {
			forward = DIRECTION_E;
		}
		if (dy < 0) {
			forward += DIRECTION_N;
		} else if (dy > 0) {
			forward += DIRECTION_S;
		}
		if (forward == 0) {
			forward = DIRECTION_E;
		}
		return forward;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.Puzzle#cellMenuAction(biz.computerkraft.
	 * crossword.grid.Cell, java.lang.String)
	 */
	@Override
	public final boolean cellMenuAction(final Cell cell, final String action) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.Puzzle#populateCellMenu(biz.computerkraft
	 * .crossword.grid.Cell, java.util.List)
	 */
	@Override
	public final void populateCellMenu(final Cell cell, final List<String> actions) {
	}

	/**
	 * Post load tidyup.
	 */
	@Override
	protected final void rectangleGridPostLoadTidyup() {
		setCellGroups();
		setClueModels();
		multiDirectionGridPostLoadTidyup();
	}

	/**
	 * Multi direction grid tidy up.
	 */
	protected void multiDirectionGridPostLoadTidyup() {
	}

	/**
	 * 
	 * Adds word and clue to cell.
	 * 
	 * @param cells
	 *            cells to set
	 * @param word
	 *            word to assign clue to
	 */
	public final void addCluedWordContent(final List<Cell> cells, final Word word) {
		rectangleGridAddWordContent(cells, word);
		int direction = DIRECTION_E;
		if (cells.size() > 1) {
			direction = cells.get(0).getDirection(cells.get(1), DIRECTION_E);
		}
		cells.get(0).setClue(direction, new Clue(0, word.getWord()));
		updateClues();
	}

	/**
	 * 
	 * Clears content and clues from cell and related cells.
	 * 
	 * @param cell
	 *            cell to clear
	 * @param force
	 *            for clear of cell
	 */
	protected final void clearCellContent(final Cell cell, final boolean force) {
		int[] directions = { DIRECTION_E, DIRECTION_S | DIRECTION_E, DIRECTION_S, DIRECTION_S | DIRECTION_W };
		boolean clear = force;
		for (int direction : directions) {
			int reverse = getReverseDirection(direction);
			List<Cell> word = getWordWithCell(cell, reverse, direction);
			int cellIndex = word.indexOf(cell);
			int start = START_NOT_KNOWN;
			int end = END_NOT_KNOWN;
			for (int letter = 0; letter < word.size(); letter++) {
				Cell cellOfInterest = word.get(letter);
				if (letter <= cellIndex && start == START_NOT_KNOWN) {
					Optional<Clue> clue = cellOfInterest.getClue(direction);
					if (clue.isPresent()) {
						if (clue.get().getClueText().length() > (cellIndex - letter)) {
							start = letter;
							end = start + clue.get().getClueText().length() - 1;
						}
					}
				}
				if (letter >= cellIndex) {
					Optional<Clue> clue = cellOfInterest.getClue(reverse);
					if (clue.isPresent()) {
						if (clue.get().getClueText().length() > (letter - cellIndex)) {
							if (start == -1) {
								start = letter;
								end = start + clue.get().getClueText().length() - 1;
							} else {
								end = letter;
							}
						}
					}
				}
			}

			if (start != START_NOT_KNOWN && end != END_NOT_KNOWN) {
				if (force) {
					word.get(start).clearClue(direction);
					word.get(end).clearClue(reverse);
					for (int letter = start; letter <= end; letter++) {
						clearCellContent(word.get(letter), false);
					}
				} else {
					break;
				}
			} else if (!force) {
				clear = true;
			}
		}
		if (!cell.hasClues() && clear) {
			cell.setContents("");
		}

		updateClues();
	}

	/**
	 * Sets up cell groups.
	 */
	protected final void setCellGroups() {
		Map<String, Cell> grid = getCellMap();
		int width = getCellWidth();
		int height = getCellHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Cell cell = grid.get(x + ":" + y);
				Cell north = grid.get(x + ":" + (y - 1));
				Cell south = grid.get(x + ":" + (y + 1));
				Cell east = grid.get((x + 1) + ":" + y);
				Cell west = grid.get((x - 1) + ":" + y);
				Cell ne = grid.get((x + 1) + ":" + (y - 1));
				Cell se = grid.get((x + 1) + ":" + (y + 1));
				Cell nw = grid.get((x - 1) + ":" + (y - 1));
				Cell sw = grid.get((x - 1) + ":" + (y + 1));
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
				if (ne != null) {
					cell.setAdjacent(DIRECTION_N + DIRECTION_E, ne);
				}
				if (nw != null) {
					cell.setAdjacent(DIRECTION_N + DIRECTION_W, nw);
				}
				if (se != null) {
					cell.setAdjacent(DIRECTION_S + DIRECTION_E, se);
				}
				if (sw != null) {
					cell.setAdjacent(DIRECTION_S + DIRECTION_W, sw);
				}
			}
		}

	}
}
