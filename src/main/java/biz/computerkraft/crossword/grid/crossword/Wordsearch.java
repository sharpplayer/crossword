package biz.computerkraft.crossword.grid.crossword;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlRootElement;

import biz.computerkraft.crossword.grid.Cell;
import biz.computerkraft.crossword.grid.Clue;
import biz.computerkraft.crossword.gui.CellRenderer;
import biz.computerkraft.crossword.gui.ClueItem;
import biz.computerkraft.crossword.gui.cluemodel.WordsearchClueModel;
import biz.computerkraft.crossword.gui.renderer.WordsearchCellRenderer;

/**
 * 
 * Classic wordsearch puzzle.
 * 
 * @author Raymond Francis
 *
 */
@XmlRootElement(name = "wordsearch")
public class Wordsearch extends MultiDirectionGrid {

	/** Search clue category. */
	private static final String CATEGORY_SEARCH = "Search";

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
	 * @see biz.computerkraft.crossword.gui.Puzzle#setProperties(java.util.Map)
	 */
	@Override
	public final void setProperties(final Map<String, Object> properties) {
		setMultiDirectionGridProperties(properties);
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
	public final List<Cell> getIndirectSelection(final Cell cell, final Point2D offset) {
		return getFreeIndirectSelection(cell, offset);
	}

}
