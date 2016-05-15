package biz.computerkraft.crossword.grid.crossword;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.xml.bind.annotation.XmlRootElement;

import biz.computerkraft.crossword.db.Word;
import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.cluemodel.WordsearchClueModel;
import biz.computerkraft.crossword.gui.renderer.WordsearchCellRenderer;

/**
 * 
 * Wordsearch crossword.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "wordsearch")
public class Wordsearch extends AbstractCrossword {

	/** Search clue category. */
	private static final String CATEGORY_SEARCH = "Search";

	/** Search index not known. */
	private static final int END_NOT_KNOWN = -2;

	/** Search index not known. */
	private static final int START_NOT_KNOWN = -1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getProperties()
	 */
	@Override
	public final Map<String, Object> getProperties() {
		return getBaseProperties();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#setProperties(java.util.Map)
	 */
	@Override
	public final void setProperties(final Map<String, Object> properties) {
		setBaseProperties(properties);
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

		addClueModel(new WordsearchClueModel(CATEGORY_SEARCH));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getRendererClass()
	 */
	@Override
	public final Class<? extends CellRenderer> getRendererClass() {
		return WordsearchCellRenderer.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#getIndirectSelection(biz.
	 * computerkraft.crossword.grid.Cell, java.awt.geom.Point2D)
	 */
	@Override
	public final List<Cell> getIndirectSelection(final Cell cell, final Point2D selectionSpot) {

		int forward = 0;
		int dx = (int) Math.round((selectionSpot.getX() - CELL_CENTRE) * 2);
		int dy = (int) Math.round((selectionSpot.getY() - CELL_CENTRE) * 2);
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

		return getWordWithCell(cell, 0, forward);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see biz.computerkraft.crossword.gui.Puzzle#postLoadTidyup()
	 */
	@Override
	public final void postLoadTidyup() {
		basePostLoadTidyup();
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
				clues.add(new ClueItem(clueEntry.getValue(), CATEGORY_SEARCH, cell, clueEntry.getKey(),
						clueEntry.getValue().getClueText()));
			}
		}
		return clues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.Puzzle#addWordContent(java.util.List,
	 * biz.computerkraft.crossword.db.Word)
	 */
	@Override
	public final void addWordContent(final List<Cell> cells, final Word word) {
		baseAddWordContent(cells, word);
		int direction = DIRECTION_E;
		if (cells.size() > 1) {
			direction = cells.get(0).getDirection(cells.get(1), DIRECTION_E);
		}
		cells.get(0).setClue(direction, new Clue(0, word.getWord()));
		updateClues();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * biz.computerkraft.crossword.gui.Puzzle#clearCellContent(biz.computerkraft
	 * .crossword.grid.Cell)
	 */
	@Override
	public final void clearCellContent(final Cell cell) {
		clearCellContent(cell, true);
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
	private void clearCellContent(final Cell cell, final boolean force) {
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
}
